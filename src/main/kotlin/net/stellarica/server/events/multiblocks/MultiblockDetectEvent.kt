package net.stellarica.server.events.multiblocks

import net.stellarica.server.multiblocks.MultiblockInstance
import net.stellarica.events.Event

object MultiblockDetectEvent : Event<MultiblockDetectEvent.EventData>() {
	data class EventData(val multiblock: MultiblockInstance)
}