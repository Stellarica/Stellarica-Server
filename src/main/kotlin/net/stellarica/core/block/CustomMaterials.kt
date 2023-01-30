package net.stellarica.core.block

import eu.pb4.polymer.blocks.api.BlockModelType
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.Material
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.stellarica.core.Stellarica.Companion.identifier
import net.stellarica.core.util.SimpleBlock
import net.stellarica.core.util.SimpleItem
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings

enum class CustomMaterials: CustomMaterial{
	TEST {
		override val id = identifier("test_block")
		override val block = SimpleBlock(QuiltBlockSettings.copy(Blocks.STONE), BlockModelType.FULL_BLOCK, id.path)
		override val item = SimpleItem(QuiltItemSettings(), id.path)
	}
}




interface CustomMaterial {
	val id: Identifier
	val block: Block?
	val item: Item?
}

