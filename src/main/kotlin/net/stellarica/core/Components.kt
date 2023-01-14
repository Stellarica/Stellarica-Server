package net.stellarica.core

import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry
import net.stellarica.core.Stellarica.Companion.identifier
import net.stellarica.core.multiblocks.ChunkMultiblocksComponent

class Components : ChunkComponentInitializer {
	companion object {
		val MULTIBLOCKS = ComponentRegistry.getOrCreate<ChunkMultiblocksComponent>(
			identifier("multiblocks"),
			ChunkMultiblocksComponent::class.java
		)
	}

	override fun registerChunkComponentFactories(registry: ChunkComponentFactoryRegistry) {
		registry.register(net.stellarica.core.Components.Companion.MULTIBLOCKS) { ChunkMultiblocksComponent(it) }
	}
}
