package net.stellarica.core.event

import net.stellarica.core.multiblocks.MultiblockInstance
import net.stellarica.core.util.event.CancellableEvent
import net.stellarica.core.util.event.Event

val MultiblockUndetectEvent = Event<MultiblockInstance>()
val MultiblockDetectEvent = CancellableEvent<MultiblockInstance>()