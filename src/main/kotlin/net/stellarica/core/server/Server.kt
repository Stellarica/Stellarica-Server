package net.stellarica.core.server

import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.server.DedicatedServerModInitializer

class Server : DedicatedServerModInitializer {

	override fun onInitializeServer(mod: ModContainer?) {
		println("Hello from the server!")
	}
}