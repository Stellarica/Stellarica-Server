package net.stellarica.core.event.multiblock

import net.stellarica.core.multiblocks.MultiblockInstance
import net.stellarica.oxidiser.event.Event

object MultiblockUndetectEvent: Event<MultiblockUndetectEvent.EventData>() {
	data class EventData(val multiblock: MultiblockInstance)
}