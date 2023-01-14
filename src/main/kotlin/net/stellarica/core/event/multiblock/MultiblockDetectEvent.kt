package net.stellarica.core.event.multiblock

import net.stellarica.core.multiblocks.MultiblockInstance
import net.stellarica.oxidiser.event.Event

object MultiblockDetectEvent: Event<MultiblockDetectEvent.EventData>() {
	data class EventData(val multiblock: MultiblockInstance)
}