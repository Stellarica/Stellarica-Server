package net.stellarica.core.events.multiblocks

import net.stellarica.core.multiblocks.MultiblockInstance
import net.stellarica.oxidizer.event.Event

object MultiblockDetectEvent : Event<MultiblockDetectEvent.EventData>() {
	data class EventData(val multiblock: MultiblockInstance)
}