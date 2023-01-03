package io.github.hydrazinemc.hzmod.util

import eu.pb4.polymer.core.api.item.PolymerItem
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import io.github.hydrazinemc.hzmod.Hydrazine
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity

class SimpleBlockItem(settings: Settings?, block: Block?, modelId: String?) : BlockItem(block, settings), PolymerItem {
	private val polymerModel = PolymerResourcePackUtils.requestModel(Items.BARRIER, Hydrazine.identifier(modelId))

	override fun getPolymerItem(itemStack: ItemStack, player: ServerPlayerEntity?): Item {
		return polymerModel.item()
	}

	override fun getPolymerCustomModelData(itemStack: ItemStack, player: ServerPlayerEntity?): Int {
		return polymerModel.value()
	}
}