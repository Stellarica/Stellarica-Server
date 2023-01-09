package io.github.hydrazinemc.hzmod.multiblocks


import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent
import io.github.hydrazinemc.hzmod.event.MultiblockUndetectEvent
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.WorldChunk


class ChunkMultiblocksComponent(private val chunk: Chunk) : ServerTickingComponent {
	var multiblocks = mutableSetOf<MultiblockInstance>()
		private set

	// can't use multiblockinstance as we don't want to serialize the world or the type
	@Serializable
	private data class MultiblockData(
		val type: String,
		val oX: Int,
		val oY: Int,
		val oZ: Int,
		val direction: Direction
	) {

		constructor(multiblock: MultiblockInstance) :
				this(
					multiblock.type.id.path,
					multiblock.origin.x,
					multiblock.origin.y,
					multiblock.origin.z,
					multiblock.direction
				)

		fun toInstance(world: World) =
			MultiblockInstance(
				BlockPos(oX, oY, oZ),
				world,
				direction,
				MultiblockHandler.types.first { it.id.path == type })
	}

	override fun readFromNbt(tag: NbtCompound) {
		// read multiblocks from tag
		// todo: don't use a string in the nbt, better to just serialize to nbt
		if (tag.getString("multiblocks") == "") return
		val world = (chunk as WorldChunk).world
		multiblocks.addAll(
			Json.decodeFromString<Set<MultiblockData>>(tag.getString("multiblocks")).map { it.toInstance(world) }
				.toMutableSet()
		)
	}

	override fun writeToNbt(tag: NbtCompound) {
		// serialize multiblocks to tag
		// todo: see above todo
		if (multiblocks.size == 0) return
		tag.putString("multiblocks", Json.encodeToString(multiblocks.map { MultiblockData(it) }))
	}

	override fun serverTick() {
		// validate this chunk's multiblocks
		val invalid = mutableSetOf<MultiblockInstance>()
		multiblocks.forEach {
			if (!it.validate()) {
				MultiblockUndetectEvent.call(it)
				invalid.add(it)
				chunk.setNeedsSaving(true)
			}
		}
		multiblocks.removeAll(invalid)
	}
}