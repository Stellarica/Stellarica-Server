package io.github.hydrazinemc.hzmod

import eu.pb4.polymer.blocks.api.BlockModelType
import eu.pb4.polymer.blocks.api.PolymerBlockModel
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock
import eu.pb4.polymer.core.api.item.PolymerBlockItem
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings

class Hydrazine: ModInitializer {
	companion object {
		val TEST_BLOCK = TestBlock(QuiltBlockSettings.of(Material.STONE))
		val TEST_BLOCK_ITEM = PolymerBlockItem(TEST_BLOCK, QuiltItemSettings(), Items.NOTE_BLOCK)
	}

	override fun onInitialize(mod: ModContainer?) {
		Registry.register(Registries.BLOCK, Identifier("hydrazine", "test_block"), TEST_BLOCK)
		Registry.register(Registries.ITEM, Identifier("hydrazine", "test_block"), TEST_BLOCK_ITEM)
		PolymerResourcePackUtils.addModAssets("hydrazine")
		PolymerResourcePackUtils.requestModel(TEST_BLOCK_ITEM.getPolymerItem(TEST_BLOCK_ITEM.defaultStack, null), Identifier("hydrazine", "test_block"))
	}
}

//class TestItem(settings: QuiltItemSettings, block: Block?, virtualItem: Item?): PolymerBlockItem(block, settings, virtualItem)


class TestBlock(settings: Settings?) : Block(settings), PolymerTexturedBlock {
	override fun getPolymerBlock(state: BlockState?): Block {
		return PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, PolymerBlockModel.of(Identifier("hydrazine", "test_block")))!!.block
	}
}