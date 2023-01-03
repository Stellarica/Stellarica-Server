package io.github.hydrazinemc.hzmod.multiblocks.event

import io.github.hydrazinemc.hzmod.multiblocks.MultiblockInstance
import net.fabricmc.fabric.api.event.EventFactory

fun interface MultiblockDetectEvent {
	companion object {
		val EVENT = EventFactory.createArrayBacked(MultiblockDetectEvent::class.java) { listeners ->
			MultiblockDetectEvent { multiblock ->
				listeners.forEach { it.onDetect(multiblock) }
			}
		}
	}

	fun onDetect(multiblock: MultiblockInstance)
}