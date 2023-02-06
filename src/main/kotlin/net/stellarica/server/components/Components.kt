package net.stellarica.server.components

import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry
import net.stellarica.server.Stellarica.Companion.identifier
import net.stellarica.server.multiblocks.ChunkMultiblocksComponent

class Components : ChunkComponentInitializer {
	companion object {
		val MULTIBLOCKS = ComponentRegistry.getOrCreate<ChunkMultiblocksComponent>(
			identifier("multiblocks"),
			ChunkMultiblocksComponent::class.java
		)
	}

	override fun registerChunkComponentFactories(registry: ChunkComponentFactoryRegistry) {
		registry.register(MULTIBLOCKS) { ChunkMultiblocksComponent(it) }
	}
}
