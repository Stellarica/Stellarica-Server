package net.stellarica.core.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.model.BlockStateModelGenerator
import net.minecraft.data.client.model.Models
import net.stellarica.core.block.CustomMaterials

class ModelGenerator(output: FabricDataOutput?) : FabricModelProvider(output) {
	override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator?) {
		CustomMaterials.values().forEach {mat ->
			blockStateModelGenerator!!.registerSimpleCubeAll(mat.block)
		}
	}

	override fun generateItemModels(itemModelGenerator: ItemModelGenerator?) {
		CustomMaterials.values().forEach { mat ->
			itemModelGenerator!!.register(mat.item, Models.GENERATED)
		}
	}
}