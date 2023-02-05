package net.stellarica.core.blocks

import eu.pb4.polymer.blocks.api.BlockModelType
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.stellarica.core.components.Components.Companion.MULTIBLOCKS
import net.stellarica.core.multiblocks.MultiblockHandler
import net.stellarica.core.utils.sendRichMessage

class InterfaceBlock(settings: Settings?, type: BlockModelType?, modelId: String?) : SimpleBlock(
	settings, type,
	modelId
) {
	@Suppress("OVERRIDE_DEPRECATION") // todo: figure out why it's deprecated!
	override fun onUse(
		state: BlockState?,
		world: World?,
		pos: BlockPos?,
		player: PlayerEntity?,
		hand: Hand?,
		hit: BlockHitResult?
	): ActionResult {
		// todo: this is jank
		MULTIBLOCKS.get(world!!.getChunk(pos)).multiblocks.firstOrNull { it.origin == pos }?.let {
			player!!.sendRichMessage("<gold>${it.type.id} is already detected", true)
			return ActionResult.SUCCESS
		}
		val mb = MultiblockHandler.detect(pos!!, world)
		if (mb != null) {
			player!!.sendRichMessage("<green>Detected ${mb.type.id}", true)
		} else {
			player!!.sendRichMessage("<red>No multiblocks detected", true)
		}
		return ActionResult.SUCCESS
	}
}