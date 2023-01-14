package net.stellarica.core.util

import eu.pb4.polymer.core.api.item.PolymerItem
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.stellarica.core.Stellarica

class SimpleItem(settings: Settings?, modelId: String?) : Item(settings), PolymerItem {
	private val polymerModel = PolymerResourcePackUtils.requestModel(Items.BARRIER, Stellarica.identifier(modelId))

	override fun getPolymerItem(itemStack: ItemStack, player: ServerPlayerEntity?): Item {
		return polymerModel.item()
	}

	override fun getPolymerCustomModelData(itemStack: ItemStack, player: ServerPlayerEntity?): Int {
		return polymerModel.value()
	}
}