package net.stellarica.core.util

import eu.pb4.polymer.blocks.api.BlockModelType
import eu.pb4.polymer.blocks.api.PolymerBlockModel
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.stellarica.core.Stellarica

open class SimpleBlock(settings: Settings?, type: BlockModelType?, modelId: String?) : Block(settings),
	PolymerTexturedBlock {
	private val polymerBlockState = PolymerBlockResourceUtils.requestBlock(
		type,
		PolymerBlockModel.of(Stellarica.identifier(modelId))
	)!!

	override fun getPolymerBlock(state: BlockState): Block {
		return polymerBlockState.block
	}

	override fun getPolymerBlockState(state: BlockState): BlockState {
		return polymerBlockState
	}
}