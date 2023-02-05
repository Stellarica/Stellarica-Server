package net.stellarica.core

import com.mojang.brigadier.CommandDispatcher
import eu.pb4.polymer.blocks.api.BlockModelType
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.Identifier
import net.stellarica.core.blocks.InterfaceBlock
import net.stellarica.core.blocks.ShipBlock
import net.stellarica.core.commands.registerMiscCommands
import net.stellarica.core.crafts.starships.Starship
import net.stellarica.core.multiblocks.MultiblockHandler
import net.stellarica.core.multiblocks.MultiblockType
import net.stellarica.core.utils.coordinates.OriginRelative
import net.stellarica.core.blocks.SimpleBlock
import net.stellarica.core.blocks.SimpleBlockItem
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.server.DedicatedServerModInitializer
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings
import org.quiltmc.qsl.command.api.CommandRegistrationCallback
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings


class Stellarica : DedicatedServerModInitializer {
	companion object {
		val MODID = "stellarica"
		fun identifier(id: String?): Identifier {
			return Identifier(MODID, id)
		}

		// todo: find a better place for this
		var ships: MutableSet<Starship> = mutableSetOf()
	}

	override fun onInitializeServer(mod: ModContainer?) {
		PolymerResourcePackUtils.markAsRequired()
		PolymerResourcePackUtils.addModAssets(MODID)

		// todo: temporary code
		val inter = Registry.register(
			Registries.BLOCK,
			identifier("interface_block"),
			InterfaceBlock(
				QuiltBlockSettings.copy(Blocks.IRON_BLOCK),
				BlockModelType.FULL_BLOCK,
				"blocks/interface_block"
			)
		)
		registerSimpleBlockItem(inter, "interface_block")
		val ship = Registry.register(
			Registries.BLOCK,
			identifier("ship_block"),
			ShipBlock()
		)
		registerSimpleBlockItem(ship, "ship_block")
		MultiblockHandler.types.add(
			MultiblockType(
				identifier("test_multiblock"),
				mapOf(
					OriginRelative(0, -1, 0) to Blocks.IRON_BLOCK,
					OriginRelative(1, 0, 0) to Blocks.REDSTONE_BLOCK
				)
			)
		)

		MultiblockHandler.types.add(
			MultiblockType(
				identifier("test_weapon"),
				mapOf(
					OriginRelative(0, 0, 0) to inter,
					OriginRelative(1, 0, 0) to Blocks.IRON_BLOCK,
					OriginRelative(2, 0, 0) to Blocks.BLAST_FURNACE
				)
			)
		)


		// temporary code, but don't remove these blocks
		registerSimpleBlockItemPair(BlockModelType.FULL_BLOCK, "adamantite_block")
		registerSimpleBlockItemPair(BlockModelType.FULL_BLOCK, "iridium_block")
		registerSimpleBlockItemPair(BlockModelType.FULL_BLOCK, "steel_block")
		registerSimpleBlockItemPair(BlockModelType.FULL_BLOCK, "palladium_block")
		registerSimpleBlockItemPair(BlockModelType.FULL_BLOCK, "titanium_block")
		registerSimpleBlockItemPair(BlockModelType.FULL_BLOCK, "tungsten_block")

		CommandRegistrationCallback.EVENT.register( CommandRegistrationCallback{ dispatcher, context, _ ->
			registerMiscCommands(dispatcher, context)
		})
	}

	fun registerSimpleBlockItemPair(type: BlockModelType?, id: String?): Pair<Block, Item> {
		val ident = identifier(id)
		val block = Registry.register(
			Registries.BLOCK,
			ident,
			SimpleBlock(QuiltBlockSettings.copy(Blocks.DIAMOND_BLOCK), type, "blocks/$id")
		)
		val item = registerSimpleBlockItem(block, id)
		return Pair(block, item)
	}

	fun registerSimpleBlockItem(block: Block, id: String?): Item {
		val ident = identifier(id)
		val item = Registry.register<Item, Item>(
			Registries.ITEM,
			ident,
			SimpleBlockItem(QuiltItemSettings(), block, "blocks/$id")
		)
		return item
	}
}

