package io.github.hydrazinemc.hzmod.server

import io.github.hydrazinemc.hzmod.multiblocks.MultiblockHandler
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.server.DedicatedServerModInitializer

class Server : DedicatedServerModInitializer {
	val multiblocks: MultiblockHandler = MultiblockHandler()

	override fun onInitializeServer(mod: ModContainer?) {
		println("Hello from the server!")
	}
}