package io.github.hydrazinemc.hzmod.event

import io.github.hydrazinemc.hzmod.multiblocks.MultiblockInstance
import io.github.hydrazinemc.hzmod.util.event.CancellableEvent
import io.github.hydrazinemc.hzmod.util.event.Event

val MultiblockUndetectEvent = Event<MultiblockInstance>()
val MultiblockDetectEvent = CancellableEvent<MultiblockInstance>()