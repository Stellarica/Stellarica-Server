package io.github.hydrazinemc.hzmod.block

import eu.pb4.polymer.blocks.api.BlockModelType
import io.github.hydrazinemc.hzmod.crafts.Craft
import io.github.hydrazinemc.hzmod.util.SimpleBlock
import io.github.hydrazinemc.hzmod.util.sendMiniMessage
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.message.MessageType
import net.minecraft.network.message.SignedChatMessage
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockRotation
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.quiltmc.qkl.library.math.times

// TODO: delete this whole thing: it's temporary
class ShipBlock(settings: Settings?, type: BlockModelType?, modelId: String?) : SimpleBlock(settings, type,
	modelId
) {

	companion object {
		val m = mutableMapOf<ServerPlayerEntity, Craft>()
		init {
			ServerMessageEvents.CHAT_MESSAGE.register { message, player, params ->
				val craft = m.get(player) ?: return@register
				when (message.content) {
					"left" -> {
						craft.rotate(BlockRotation.COUNTERCLOCKWISE_90)
						player.sendMiniMessage("lefted")
					}
					"right" -> {
						craft.rotate(BlockRotation.CLOCKWISE_90)
						player.sendMiniMessage("righted")
					}
					"go" -> {
						craft.move(player.eyePos.normalize().times(5.0))
						player.sendMiniMessage("goed")
					}
				}
			}
		}
	}

	@Suppress("OVERRIDE_DEPRECATION") // todo: figure out why it's deprecated!
	override fun onUse(
		state: BlockState?,
		world: World?,
		pos: BlockPos?,
		player: PlayerEntity?,
		hand: Hand?,
		hit: BlockHitResult?
	): ActionResult {
		val craft = Craft(pos!!, world as ServerWorld, player as ServerPlayerEntity)
		craft.detect()
		craft.passengers.add(player)
		m[player] = craft

		return ActionResult.SUCCESS
	}
}

