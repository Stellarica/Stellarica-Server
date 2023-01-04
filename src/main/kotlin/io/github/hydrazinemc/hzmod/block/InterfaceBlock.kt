package io.github.hydrazinemc.hzmod.block

import eu.pb4.polymer.blocks.api.BlockModelType
import io.github.hydrazinemc.hzmod.Components.Companion.MULTIBLOCKS
import io.github.hydrazinemc.hzmod.multiblocks.MultiblockHandler
import io.github.hydrazinemc.hzmod.util.SimpleBlock
import io.github.hydrazinemc.hzmod.util.sendMiniMessage
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class InterfaceBlock(settings: Settings?, type: BlockModelType?, modelId: String?) : SimpleBlock(settings, type,
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
			player!!.sendMiniMessage("<gold>${it.type.id} is already detected", true)
			return ActionResult.SUCCESS
		}
		val mb = MultiblockHandler.detect(pos!!, world)
		if (mb != null) {
			player!!.sendMiniMessage("<green>Detected ${mb.type.id}", true)
		} else {
			player!!.sendMiniMessage("<red>No multiblock detected", true)
		}
		return ActionResult.SUCCESS
	}
}