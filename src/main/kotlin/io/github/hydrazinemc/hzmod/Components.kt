package io.github.hydrazinemc.hzmod

import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry
import io.github.hydrazinemc.hzmod.Hydrazine.Companion.identifier
import io.github.hydrazinemc.hzmod.multiblocks.ChunkMultiblocksComponent

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
