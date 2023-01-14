package net.stellarica.core.block

import eu.pb4.polymer.blocks.api.BlockModelType
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.stellarica.core.crafts.starships.Starship
import net.stellarica.core.util.SimpleBlock

// TODO: delete this whole thing: it's temporary
class ShipBlock(settings: Settings?, type: BlockModelType?, modelId: String?) : SimpleBlock(
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
		val craft = Starship(pos!!, world as ServerWorld, player as ServerPlayerEntity)
		craft.detect()
		craft.pilot(player)
		craft.passengers.add(player)

		return ActionResult.SUCCESS
	}
}

