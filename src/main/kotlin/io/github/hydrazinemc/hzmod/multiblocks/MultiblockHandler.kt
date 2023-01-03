package io.github.hydrazinemc.hzmod.multiblocks

import io.github.hydrazinemc.hzmod.multiblocks.event.MultiblockUndetectEvent
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.chunk.WorldChunk
import org.quiltmc.qsl.lifecycle.api.event.ServerTickEvents

class MultiblockHandler {
	val types = mutableListOf<MultiblockType>()

	val active = mutableSetOf<MultiblockInstance>() // set to avoid duplicates

	init {
		ServerTickEvents.END.register { validateActiveMultiblocks() }
		ServerChunkEvents.CHUNK_UNLOAD.register { world, chunk ->
			storeMultiblocks(world, chunk)
		}
	}

	fun detect(origin: BlockPos, world: World): MultiblockInstance? {
		val possible = mutableListOf<MultiblockInstance>()
		types.forEach {
			val instance = it.detect(origin, world)
			if (instance != null) {
				possible.add(instance)
			}
		}
		// return the largest possible, in case there are multiple
		return possible.maxByOrNull { it.type.blocks.size }
	}

	fun validateActiveMultiblocks() {
		val invalid = mutableSetOf<MultiblockInstance>()
		active.forEach {
			if (!it.validate()) {
				MultiblockUndetectEvent.EVENT.invoker().onUndetect(it)
				invalid.add(it)
			}
		}
		active.removeAll(invalid)
	}

	private fun storeMultiblocks(world: ServerWorld, chunk: WorldChunk) {
		active.filter { world.getChunk(it.origin) == chunk }.forEach {
			TODO()
		}
	}

	private fun loadMultiblocks(world: ServerWorld, chunk: WorldChunk) {
		TODO()
	}
}