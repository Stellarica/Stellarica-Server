package io.github.hydrazinemc.hzmod.multiblocks


import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent
import io.github.hydrazinemc.hzmod.multiblocks.event.MultiblockUndetectEvent
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World


class ChunkMultiblocksComponent: ServerTickingComponent {
	var multiblocks = mutableSetOf<MultiblockInstance>()
		private set

	// can't use multiblockinstance as we don't want to serialize the world or the type
	@Serializable
	private data class MultiblockData(@Contextual val type: Identifier, @Contextual val origin: BlockPos, val direction: Direction) {
		constructor(multiblock: MultiblockInstance): this(multiblock.type.id, multiblock.origin, multiblock.direction)
		fun toInstance(world: World) = MultiblockInstance(origin, world, direction, MultiblockHandler.types.first { it.id == type })
	}

	override fun readFromNbt(tag: NbtCompound) {
		// read multiblocks from tag
		// todo: don't use a string in the nbt, better to just serialize to nbt
		val world = multiblocks.firstOrNull()?.world ?: return // <- this is also dumb
		multiblocks = Json.decodeFromString<Set<MultiblockData>>(tag.getString("multiblocks")).map { it.toInstance(world) }.toMutableSet()
	}

	override fun writeToNbt(tag: NbtCompound) {
		// serialize multiblocks to tag
		// todo: see above todo
		tag.putString("multiblocks", Json.encodeToString(multiblocks.map { MultiblockData(it) }))
	}

	override fun serverTick() {
		// validate this chunk's multiblocks
		val invalid = mutableSetOf<MultiblockInstance>()
		multiblocks.forEach {
			if (!it.validate()) {
				MultiblockUndetectEvent.EVENT.invoker().onUndetect(it)
				invalid.add(it)
			}
		}
		multiblocks.removeAll(invalid)
	}
}