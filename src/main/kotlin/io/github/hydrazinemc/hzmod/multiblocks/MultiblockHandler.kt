package io.github.hydrazinemc.hzmod.multiblocks

import io.github.hydrazinemc.hzmod.Components.Companion.MULTIBLOCKS
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object MultiblockHandler {
	val types = mutableListOf<MultiblockType>()

	fun detect(origin: BlockPos, world: World): MultiblockInstance? {
		val possible = mutableListOf<MultiblockInstance>()
		types.forEach {
			val instance = it.detect(origin, world)
			if (instance != null) {
				possible.add(instance)
			}
		}
		// return the largest possible, in case there are multiple
		return possible.maxByOrNull { it.type.blocks.size }?.also {
			MultiblockDetectEvent.call(it)
			if (MultiblockDetectEvent.cancelled) return null // maybe check for a smaller one?
			val chunk = world.getChunk(origin)
			MULTIBLOCKS.get(chunk).multiblocks.add(it)
			chunk.setNeedsSaving(true) // this should be moved to ChunkMultiblocksComponent
		}
	}
}