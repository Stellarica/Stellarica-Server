package net.stellarica.server.events.multiblocks

import net.stellarica.server.multiblocks.MultiblockInstance
import net.stellarica.events.Event

object MultiblockUndetectEvent : Event<MultiblockUndetectEvent.EventData>() {
	data class EventData(val multiblock: MultiblockInstance)
}