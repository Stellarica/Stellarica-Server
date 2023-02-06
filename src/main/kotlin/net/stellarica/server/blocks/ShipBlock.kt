package net.stellarica.server.blocks

import eu.pb4.polymer.core.api.block.PolymerBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.LecternBlock
import net.minecraft.block.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.stellarica.server.crafts.starships.Starship
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings

// TODO: delete this whole thing: it's temporary
class ShipBlock() : LecternBlock(QuiltBlockSettings.of(Material.METAL)), PolymerBlock {
	@Suppress("OVERRIDE_DEPRECATION") // todo: figure out why it's deprecated!
	override fun onUse(
		state: BlockState?,
		world: World?,
		pos: BlockPos?,
		player: PlayerEntity?,
		hand: Hand?,
		hit: BlockHitResult?
	): ActionResult {
		val craft = Starship(pos!!, world as ServerWorld, player as ServerPlayerEntity, state!!.get(LecternBlock.FACING))
		craft.detect()
		if (craft.blockCount == 0) return ActionResult.FAIL // in case it didn't detect
		craft.pilot(player)
		craft.passengers.add(player)

		return ActionResult.SUCCESS
	}

	override fun getPolymerBlock(state: BlockState?): Block {
		return Blocks.LECTERN
	}

	override fun getPolymerBlockState(state: BlockState): BlockState {
		return Blocks.LECTERN.defaultState.with(LecternBlock.FACING, state.get(LecternBlock.FACING))
	}
}

