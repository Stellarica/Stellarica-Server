package net.stellarica.core.datagen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

class Datagen: DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator?) {
		fabricDataGenerator!!.createPack().addProvider(::ModelGenerator)
	}
}