package io.github.hydrazinemc.hzmod.multiblocks.event

import io.github.hydrazinemc.hzmod.multiblocks.MultiblockInstance
import net.fabricmc.fabric.api.event.EventFactory


fun interface MultiblockUndetectEvent {
	companion object {
		val EVENT = EventFactory.createArrayBacked(MultiblockUndetectEvent::class.java) { listeners ->
			MultiblockUndetectEvent { multiblock ->
				listeners.forEach { it.onUndetect(multiblock) }
			}
		}
	}

	fun onUndetect(multiblock: MultiblockInstance)
}