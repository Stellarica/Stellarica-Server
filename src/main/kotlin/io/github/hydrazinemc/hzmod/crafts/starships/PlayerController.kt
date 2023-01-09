package io.github.hydrazinemc.hzmod.crafts.starships

import io.github.hydrazinemc.hzmod.util.setRichName
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockRotation
import net.minecraft.util.TypedActionResult
import org.quiltmc.qkl.library.math.toVec3i
import org.quiltmc.qsl.lifecycle.api.event.ServerTickEvents
import xyz.nucleoid.stimuli.Stimuli
import xyz.nucleoid.stimuli.event.item.ItemThrowEvent
import xyz.nucleoid.stimuli.event.item.ItemUseEvent
import xyz.nucleoid.stimuli.event.player.PlayerInventoryActionEvent
import xyz.nucleoid.stimuli.event.player.PlayerSwapWithOffhandEvent

class PlayerController(ship: Starship): Controller(ship) {
	var counter = 0
	private val itemUse =  ItemUseEvent { player, _ ->
		if (player != pilot) return@ItemUseEvent TypedActionResult.success(ItemStack.EMPTY)
		when (player.inventory.selectedSlot) {
			0 -> {
				cruiseDirection = player.eyePos.normalize()
				cruiseSpeed = 10
			}
			1 -> cruiseSpeed = 0
			4 -> ship.rotate(BlockRotation.CLOCKWISE_90)
			5 -> ship.rotate(BlockRotation.COUNTERCLOCKWISE_90)
			8 -> ship.unpilot()
		}
		return@ItemUseEvent TypedActionResult.fail(ItemStack.EMPTY)
	}
	private val itemThrow = ItemThrowEvent { player, slot, stack  ->
		if (player == pilot) {
			ship.rotate(BlockRotation.CLOCKWISE_90)
			return@ItemThrowEvent ActionResult.FAIL
		}
		return@ItemThrowEvent ActionResult.SUCCESS
	}
	private val itemSwap = PlayerSwapWithOffhandEvent { player ->
		if (player == pilot) {
			ship.rotate(BlockRotation.COUNTERCLOCKWISE_90)
			return@PlayerSwapWithOffhandEvent ActionResult.FAIL
		}
		return@PlayerSwapWithOffhandEvent ActionResult.SUCCESS
	}
	private val itemMove = PlayerInventoryActionEvent { player, _, _, _ ->
		if (player == pilot) {
			return@PlayerInventoryActionEvent ActionResult.FAIL
		}
		return@PlayerInventoryActionEvent ActionResult.SUCCESS
	}
	private val serverTick = ServerTickEvents.Start { _ ->
		if (cruiseSpeed > 0 && counter % 20 == 0) {
			cruiseDirection?.multiply(cruiseSpeed.toDouble())?.let { ship.move(it.toVec3i()) }
			counter = 0
		}
		counter++
	}

	private var hotbar = mutableListOf<ItemStack>()
	lateinit var pilot: ServerPlayerEntity

	// these could definitely be cleaned up a bit
	override fun onPilot(player: ServerPlayerEntity) {
		this.pilot = player
		for (slot in 0..10) {
			hotbar.add(player.inventory.getStack(slot))
			player.inventory.setStack(slot, when (slot) {
				0 -> ItemStack(Items.GREEN_CONCRETE).also { it.setRichName("<b>Cruise") }
				1 -> ItemStack(Items.RED_CONCRETE).also { it.setRichName("<b>Stop") }
				2 -> ItemStack(Items.YELLOW_CONCRETE).also { it.setRichName("<b>Precision") }
				4 -> ItemStack(Items.BLUE_CONCRETE).also { it.setRichName("<b>Left") }
				5 -> ItemStack(Items.BLUE_CONCRETE).also { it.setRichName("<b>Right") }
				7 -> ItemStack(Items.NETHER_STAR).also { it.setRichName("<b>Fire") }
				8 -> ItemStack(Items.BARRIER).also { it.setRichName("<b>Unpilot") }
				else -> ItemStack.EMPTY
			})
		}
		Stimuli.global().listen(ItemUseEvent.EVENT, itemUse)
		Stimuli.global().listen(ItemThrowEvent.EVENT, itemThrow)
		Stimuli.global().listen(PlayerSwapWithOffhandEvent.EVENT, itemSwap)
		Stimuli.global().listen(PlayerInventoryActionEvent.EVENT, itemMove)
		ServerTickEvents.START.register(serverTick)
	}

	override fun onUnpilot() {
		Stimuli.global().unlisten(ItemUseEvent.EVENT, itemUse)
		Stimuli.global().unlisten(ItemThrowEvent.EVENT, itemThrow)
		Stimuli.global().unlisten(PlayerSwapWithOffhandEvent.EVENT, itemSwap)
		Stimuli.global().unlisten(PlayerInventoryActionEvent.EVENT, itemMove)
		for (slot in 0..10) {
			pilot.inventory.setStack(slot, hotbar[slot])
		}
		// todo: unregister serverTick
	}
}