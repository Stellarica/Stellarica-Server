package io.github.hydrazinemc.hzmod

import eu.pb4.polymer.blocks.api.BlockModelType
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import io.github.hydrazinemc.hzmod.util.SimpleBlock
import io.github.hydrazinemc.hzmod.util.SimpleBlockItem
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings


class Hydrazine: ModInitializer {
	companion object {
		val MODID = "hydrazine"
		fun identifier(id: String?): Identifier {
			return Identifier(MODID, id)
		}
	}

	override fun onInitialize(mod: ModContainer?) {
		PolymerResourcePackUtils.markAsRequired()
		PolymerResourcePackUtils.addModAssets(MODID)
		registerSimpleBlock(BlockModelType.FULL_BLOCK, "block/test_block");
	}

	fun registerSimpleBlock(type: BlockModelType?, modelId: String?): Pair<Block, Item> {
		val id = identifier(modelId)
		val block = Registry.register(
			Registries.BLOCK,
			id,
			SimpleBlock(QuiltBlockSettings.copy(Blocks.DIAMOND_BLOCK), type, modelId)
		)
		val item = Registry.register<Item, Item>(
			Registries.ITEM,
			id,
			SimpleBlockItem(QuiltItemSettings(), block, modelId)
		)
		return Pair(block, item)
	}
}

