package io.github.hydrazinemc.hzmod.client;

import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer

class Client : ClientModInitializer {
	override fun onInitializeClient(mod: ModContainer?) {
		println("Hello from the client")
	}
}
