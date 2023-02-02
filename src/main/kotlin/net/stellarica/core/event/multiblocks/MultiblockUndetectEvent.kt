package net.stellarica.core.event.multiblocks

import net.stellarica.core.multiblocks.MultiblockInstance
import net.stellarica.oxidizer.event.Event

object MultiblockUndetectEvent : Event<MultiblockUndetectEvent.EventData>() {
	data class EventData(val multiblock: MultiblockInstance)
}