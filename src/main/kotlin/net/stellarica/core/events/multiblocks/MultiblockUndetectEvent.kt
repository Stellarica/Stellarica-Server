package net.stellarica.core.events.multiblocks

import net.stellarica.core.multiblocks.MultiblockInstance
import net.stellarica.events.Event

object MultiblockUndetectEvent : Event<MultiblockUndetectEvent.EventData>() {
	data class EventData(val multiblock: MultiblockInstance)
}