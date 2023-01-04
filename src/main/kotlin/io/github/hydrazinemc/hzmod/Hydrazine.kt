package io.github.hydrazinemc.hzmod

import eu.pb4.polymer.blocks.api.BlockModelType
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import io.github.hydrazinemc.hzmod.util.SimpleBlock
import io.github.hydrazinemc.hzmod.util.SimpleBlockItem
import io.github.hydrazinemc.hzmod.block.InterfaceBlock
import io.github.hydrazinemc.hzmod.block.ShipBlock
import io.github.hydrazinemc.hzmod.multiblocks.MultiblockHandler
import io.github.hydrazinemc.hzmod.multiblocks.MultiblockType
import io.github.hydrazinemc.hzmod.multiblocks.OriginRelative
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
		registerSimpleBlockItemPair(BlockModelType.FULL_BLOCK, "block/test_block")


		// todo: temporary code
		val inter = Registry.register(
			Registries.BLOCK,
			identifier("interface_block"),
			InterfaceBlock(
				QuiltBlockSettings.copy(Blocks.IRON_BLOCK),
				BlockModelType.FULL_BLOCK,
				"block/interface_block"
			)
		)
		registerSimpleBlockItem(inter, "block/interface_block")
		val ship = Registry.register(
			Registries.BLOCK,
			identifier("ship_block"),
			ShipBlock(
				QuiltBlockSettings.copy(Blocks.IRON_BLOCK),
				BlockModelType.FULL_BLOCK,
				"block/ship_block"
			)
		)
		registerSimpleBlockItem(ship, "block/ship_block")
		MultiblockHandler.types.add(MultiblockType(
			identifier("test_multiblock"),
			mapOf(
				OriginRelative(0,-1,0) to Blocks.IRON_BLOCK,
				OriginRelative(1, 0, 0) to Blocks.REDSTONE_BLOCK
			)
		))
	}

	fun registerSimpleBlockItemPair(type: BlockModelType?, modelId: String?): Pair<Block, Item> {
		val id = identifier(modelId)
		val block = Registry.register(
			Registries.BLOCK,
			id,
			SimpleBlock(QuiltBlockSettings.copy(Blocks.DIAMOND_BLOCK), type, modelId)
		)
		val item = registerSimpleBlockItem(block, modelId)
		return Pair(block, item)
	}

	fun registerSimpleBlockItem(block: Block, modelId: String?): Item {
		val id = identifier(modelId)
		val item = Registry.register<Item, Item>(
			Registries.ITEM,
			id,
			SimpleBlockItem(QuiltItemSettings(), block, modelId)
		)
		return item
	}
}

