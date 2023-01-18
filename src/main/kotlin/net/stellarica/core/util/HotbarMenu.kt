package net.stellarica.core.util

import net.minecraft.server.network.ServerPlayerEntity
import net.stellarica.oxidizer.event.Event
import net.stellarica.oxidizer.event.block.BlockPlaceEvent
import net.stellarica.oxidizer.event.block.BlockUseEvent
import net.stellarica.oxidizer.event.entity.EntityUseEvent
import net.stellarica.oxidizer.event.item.ItemPickupEvent
import net.stellarica.oxidizer.event.item.ItemThrowEvent
import net.stellarica.oxidizer.event.player.PlayerDeathEvent
import net.stellarica.oxidizer.event.player.PlayerInventoryActionEvent
import net.stellarica.oxidizer.event.player.PlayerSwapWithOffhandEvent
import net.stellarica.oxidizer.event.player.PlayerSwingHandEvent


abstract class HotbarMenu(val player: ServerPlayerEntity) {

	private val originalHotbar = (0..8).map{ player.inventory.getStack(it) }

	// maybe there's a way to make these less... painful?
	val listeners = setOf (
		PlayerInventoryActionEvent.listen(EventPriority.LOWEST.ordinal) {
			if (it.player == player) return@listen Event.Result.CANCEL
			else return@listen Event.Result.CONTINUE
		},
		PlayerDeathEvent.listen(EventPriority.LOWEST.ordinal) {
			if (it.player == player) close()
			return@listen Event.Result.CONTINUE
		},
		ItemPickupEvent.listen(EventPriority.LOWEST.ordinal) {
			if (it.player == player) return@listen Event.Result.CANCEL
			else return@listen Event.Result.CONTINUE
		},
		PlayerSwapWithOffhandEvent.listen(EventPriority.LOWEST.ordinal) {
			if (it.player == player) return@listen Event.Result.CANCEL
			else return@listen Event.Result.CONTINUE
		},
		PlayerSwingHandEvent.listen(EventPriority.LOWEST.ordinal) {
			if (it.player == player) {
				onButtonClicked(player.inventory.selectedSlot)
				return@listen Event.Result.CANCEL
			}
			else return@listen Event.Result.CONTINUE
		},
		BlockPlaceEvent.listen(EventPriority.LOWEST.ordinal) {
			if (it.player == player) return@listen Event.Result.CANCEL
			else return@listen Event.Result.CONTINUE
		},
		BlockUseEvent.listen(EventPriority.LOWEST.ordinal) {
			if (it.player == player) return@listen Event.Result.CANCEL
			else return@listen Event.Result.CONTINUE
		},
		EntityUseEvent.listen(EventPriority.LOWEST.ordinal) {
			if (it.player == player) return@listen Event.Result.CANCEL
			else return@listen Event.Result.CONTINUE
		},
		ItemThrowEvent.listen(EventPriority.LOWEST.ordinal) {
			if (it.player == player) return@listen Event.Result.CANCEL
			else return@listen Event.Result.CONTINUE
		},

		// todo: handle player quit
	)

	/**
	 * Called when the player selects the item at index
	 */
	open fun onButtonClicked(index: Int) {}

	/**
	 * Called when the player changes their slot selection
	 */
	open fun onChangeSelectedSlot(oldIndex: Int, newIndex: Int) {}

	/**
	 * Called before the menu closes
	 */
	open fun onMenuClosed() {}

	/**
	 * Close this menu
	 */
	fun close() {
		onMenuClosed()
		listeners.forEach { it.unregister() }
		for (i in 0..8) {
			player.inventory.setStack(i, originalHotbar[i])
		}
	}
}