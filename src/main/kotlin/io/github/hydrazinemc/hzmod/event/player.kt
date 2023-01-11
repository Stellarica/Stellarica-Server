package io.github.hydrazinemc.hzmod.event

import io.github.hydrazinemc.hzmod.util.event.CancellableEvent
import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.server.network.ServerPlayerEntity

val PlayerClickInsideInventoryEvent = CancellableEvent<PlayerClickInventoryEventData>()
data class PlayerClickInventoryEventData(val player: ServerPlayerEntity, val slot: Int, val actionType: SlotActionType)
val PlayerDropItemEvent = CancellableEvent<Pair<ServerPlayerEntity, ItemStack>>()

val PlayerSwitchItemHandEvent = CancellableEvent<ServerPlayerEntity>()

data class PlayerChangeSelectedSlotData(val player: ServerPlayerEntity, val oldSlot: Int, val newSlot: Int)
val PlayerChangeSelectedSlotEvent = CancellableEvent<PlayerChangeSelectedSlotData>()

enum class InteractType {
	CLICK_ITEM,
	CLICK_BLOCK
}
data class PlayerInteractEventData(val player: ServerPlayerEntity, val type: InteractType, val targetedBlock: BlockState?, val item: ItemStack?)
val PlayerInteractEvent = CancellableEvent<PlayerInteractEventData>()

