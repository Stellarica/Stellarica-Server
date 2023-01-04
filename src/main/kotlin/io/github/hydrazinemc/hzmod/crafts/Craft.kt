package io.github.hydrazinemc.hzmod.crafts

import io.github.hydrazinemc.hzmod.Components.Companion.MULTIBLOCKS
import io.github.hydrazinemc.hzmod.multiblocks.MultiblockInstance
import io.github.hydrazinemc.hzmod.multiblocks.OriginRelative
import io.github.hydrazinemc.hzmod.util.asDegrees
import io.github.hydrazinemc.hzmod.util.rotateCoordinates
import io.github.hydrazinemc.hzmod.util.sendMiniMessage
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.LivingEntity
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket.Flag
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerChunkManager
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.ChunkSection
import net.minecraft.world.chunk.WorldChunk
import org.quiltmc.qkl.library.math.toBlockPos
import org.quiltmc.qkl.library.math.toVec3d
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap
import kotlin.system.measureTimeMillis

class Craft(var origin: BlockPos, var world: ServerWorld, var owner: ServerPlayerEntity) {
	val sizeLimit = 10000

	var multiblocks = mutableSetOf<WeakReference<MultiblockInstance>>()
	var detectedBlocks = mutableSetOf<BlockPos>()

	var passengers = mutableSetOf<LivingEntity>()

	val blockCount: Int
		get() = detectedBlocks.size

	/**
	 * The blocks considered to be "inside" of the ship, but not neccecarily detected.
	 */
	protected var bounds = mutableSetOf<OriginRelative>()

	/**
	 * @return Whether [block] is considered to be inside this craft
	 */
	fun contains(block: BlockPos?): Boolean {
		block ?: return false
		return detectedBlocks.contains(block) || bounds.contains(block.subtract(origin).let {
			OriginRelative(
				it.x,
				it.y,
				it.z
			)
		})
	}

	fun calculateHitbox() {
		detectedBlocks
			.map { pos -> pos.subtract(origin)
				.let { OriginRelative(it.x, it.y, it.z) }
			}
			.sortedBy { -it.y }
			.forEach { block ->
				val max = bounds.filter { it.x == block.x && it.z == block.z }.maxByOrNull { it.y }?.y ?: block.y
				for (y in block.y..max) {
					bounds.add(OriginRelative(block.x, y, block.z))
				}
			}
	}

	fun detect() {
		var nextBlocksToCheck = detectedBlocks
		nextBlocksToCheck.add(origin)
		detectedBlocks = mutableSetOf()
		val checkedBlocks = nextBlocksToCheck.toMutableSet()

		val startTime = System.currentTimeMillis()

		val chunks = mutableSetOf<Chunk>()

		while (nextBlocksToCheck.size > 0) {
			val blocksToCheck = nextBlocksToCheck
			nextBlocksToCheck = mutableSetOf()

			for (currentBlock in blocksToCheck) {

				if (undetectableBlocks.contains(world.getBlockState(currentBlock).block)) continue

				if (detectedBlocks.size > sizeLimit) {
					owner.sendMiniMessage("<gold>Detection limit reached. (${sizeLimit} blocks)")
					nextBlocksToCheck.clear()
					detectedBlocks.clear()
					break
				}

				detectedBlocks.add(currentBlock)
				chunks.add(world.getChunk(currentBlock))

				// Slightly condensed from MSP's nonsense, but this could be improved
				for (x in -1..1) {
					for (y in -1..1) {
						for (z in -1..1) {
							if (x == y && z == y && y == 0) continue
							val block = currentBlock.add(x, y, z)
							if (!checkedBlocks.contains(block)) {
								checkedBlocks.add(block)
								nextBlocksToCheck.add(block)
							}
						}
					}
				}
			}
		}

		val elapsed = System.currentTimeMillis() - startTime
		owner.sendMiniMessage("<green>Craft detected! (${detectedBlocks.size} blocks)")
		owner.sendMiniMessage(
			"<gray>Detected ${detectedBlocks.size} blocks in ${elapsed}ms. " +
					"(${detectedBlocks.size / elapsed.coerceAtLeast(1)} blocks/ms)"
		)
		owner.sendMiniMessage(
			"<gray>Calculated Hitbox in ${
				measureTimeMillis {
					calculateHitbox()
				}
			}ms. (${bounds.size} blocks)")

		// Detect all multiblocks
		multiblocks.clear()
		// this is probably slow
		multiblocks.addAll(chunks.map { MULTIBLOCKS.get(it).multiblocks }.flatten().filter { detectedBlocks.contains(it.origin) }.map { WeakReference(it) })

		owner.sendMiniMessage("<gray>Detected ${multiblocks.size} multiblocks")
	}

	fun movePassengers(offset: (Vec3d) -> Vec3d, rotation: BlockRotation) {
		passengers.forEach {
			// TODO: FIX
			// this is not a good solution because if there is any rotation, the player will not be translated by the offset
			// The result is that any ship movement that attempts to rotate and move in the same action will break.
			// For now there aren't any actions like that, but if there are in the future, this will need to be fixed.
			//
			// Rotating the whole ship around the adjusted origin will not work,
			// as rotating the ship 4 times does not bring it back to the original position
			//
			// However, without this dumb fix players do not rotate to the proper relative location
			val destination =
				if (rotation != BlockRotation.NONE) rotateCoordinates(
					it.pos,
					Vec3d(
						0.5,
						0.0,
						0.5
					).add(origin.toVec3d()),
					rotation
				)
				else offset(it.pos)

			// todo: handle teleporting to a different world
			if (it is ServerPlayerEntity) {
				it.networkHandler.requestTeleport(destination.x, destination.y, destination.z, it.yaw + rotation.asDegrees.toFloat(), it.pitch, Flag.ALL_FLAGS, true)
			} else {
				it.teleport(destination.x, destination.y, destination.z)
			}
		}
	}

	fun sendMiniMessage(message: String) {
		passengers.forEach {
			if (it is ServerPlayerEntity) {
				it.sendMiniMessage(message)
			}
		}
	}

	private fun setBlockFast(pos: BlockPos, state: BlockState, world: ServerWorld) {
		val chunk = world.getChunk(pos) as WorldChunk
		val chunkSection = (pos.y shr 4) - chunk.bottomSectionCoord
		var section = chunk.sectionArray[chunkSection]
		if (section == null) {
			// Put a GLASS block to initialize the section. It will be replaced next with the real block.
			chunk.setBlockState(pos, Blocks.GLASS.defaultState, false)
			section = chunk.sectionArray[chunkSection]
		}
		if (section!!.getBlockState(pos.x and 15, pos.y and 15, pos.z and 15) == state) {
			//Block is already of correct type and data, don't overwrite
			return
		}
		section.setBlockState(pos.x and 15, pos.y and 15, pos.z and 15, state)
		world.updateListeners(pos, state, state, 3)
		// world.lightEngine.checkBlock(position) // boolean corresponds to if chunk section empty
		//todo: LIGHTING IS FOR CHUMPS!
		chunk.setNeedsSaving(true)
	}

	/**
	 * Translate the craft by [offset] blocks
	 * @see queueChange
	 */
	fun move(offset: Vec3d) {
		change({ current ->
			return@change current.add(offset)
		}, "Movement", world)
	}

	/**
	 * Rotate the craft and contents by [rotation]
	 * @see queueChange
	 */
	fun rotate(rotation: BlockRotation) {
		change({ current ->
			return@change rotateCoordinates(current, origin.toVec3d(), rotation)
		}, "Rotation", world, rotation) {
			calculateHitbox() // rather than keep track of a hitbox rotation, just recacluate it when we rotate.
		}
	}

	private fun change(
		modifier: (Vec3d) -> Vec3d,
		name: String,
		targetWorld: ServerWorld,
		rotation: BlockRotation = BlockRotation.NONE,
		callback: () -> Unit = {}
	) {
		val entities = ConcurrentHashMap<BlockPos, BlockPos>()
		val blocksToSet = ConcurrentHashMap<BlockPos, BlockState>()
		var collision: BlockPos? = null

		// calculate blocks to set and find entities to move
		runBlocking {
			val jobs = mutableListOf<Job>()
			detectedBlocks.chunked(500).forEach { section ->
				// chunk into sections to process parallel
				jobs.add(async(Dispatchers.Default) {
					// calculate new position and state for each block in this section
					section.forEach { current ->
						val target = modifier(current.toVec3d()).toBlockPos()
						if (!detectedBlocks.contains(target) && world.getBlockState(target).block != Blocks.AIR)
							collision = target
						val currentBlock = world.getBlockState(current)
						blocksToSet.putIfAbsent(current, Blocks.AIR.defaultState)
						blocksToSet[target] = currentBlock.rotate(rotation)
						if (currentBlock.hasBlockEntity()) {
							entities[current] = target
						}
					}
				})
			}
			jobs.forEach { it.join() } // there's probably a better way to do this
		}
		// check for collision
		if (collision != null) {
			sendMiniMessage("<gold>Blocked by ${world.getBlockState(collision!!).block.name} at <bold>(${collision!!.x}, ${collision!!.y}, ${collision!!.z}</bold>)!\"")
			return
		}
		// move blocks
		blocksToSet.forEach { setBlockFast(it.key, it.value, targetWorld) } // todo: only set to target world if its a new block. if its air we want the old world

		// move entities
		movePassengers(modifier, rotation)
		entities.forEach { (from, to) ->
			val entity = world.getBlockEntity(from) ?: return@forEach
			entity::class.java.getDeclaredField("pos").set(entity, to)
			entity.world = targetWorld
			entity.markDirty()
			world.getChunk(to).setBlockEntity(entity)
		}

		// move multiblocks
		multiblocks.map {
			val mb = it.get() ?: return@map null
			MULTIBLOCKS.get(world.getChunk(mb.origin)).multiblocks.remove(mb)
			val new = mb.copy(origin = modifier(mb.origin.toVec3d()).toBlockPos())
			MULTIBLOCKS.get(targetWorld.getChunk(new.origin)).multiblocks.add(new)
			return@map new
		}

		world = targetWorld
	}
}