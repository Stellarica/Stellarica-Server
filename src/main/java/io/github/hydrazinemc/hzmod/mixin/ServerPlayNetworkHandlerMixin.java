package io.github.hydrazinemc.hzmod.mixin;

import io.github.hydrazinemc.hzmod.event.InteractType;
import io.github.hydrazinemc.hzmod.event.PlayerChangeSelectedSlotData;
import io.github.hydrazinemc.hzmod.event.PlayerClickInventoryEventData;
import io.github.hydrazinemc.hzmod.event.PlayerInteractEventData;
import io.github.hydrazinemc.hzmod.event.PlayerKt;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
	@Shadow
	public ServerPlayerEntity player;

	@Inject(method = "onClickSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/c2s/play/ClickSlotC2SPacket;getRevision()I"), cancellable = true)
	private void onInventoryAction(ClickSlotC2SPacket packet, CallbackInfo ci) {
		PlayerKt.getPlayerClickInsideInventoryEvent().call(new PlayerClickInventoryEventData(
				player,
				packet.getSlot(),
				packet.getActionType()
		));
		if (PlayerKt.getPlayerClickInsideInventoryEvent().getCancelled()) {
			ci.cancel();
		}
	}

	@Inject(method = "onUpdateSelectedSlot", at = @At(value = "INVOKE", target="Lnet/minecraft/network/packet/c2s/play/UpdateSelectedSlotC2SPacket;getSelectedSlot()I"), cancellable = true)
	private void onChangeSlot(UpdateSelectedSlotC2SPacket packet, CallbackInfo ci) {
		PlayerKt.getPlayerChangeSelectedSlotEvent().call(new PlayerChangeSelectedSlotData(
				player,
				player.getInventory().selectedSlot,
				packet.getSelectedSlot()
		));
		if (PlayerKt.getPlayerChangeSelectedSlotEvent().getCancelled()) {
			ci.cancel();
		}
	}

	@Inject(
			method = "onPlayerInteractItem",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/server/world/ServerWorld;)V",
					shift = At.Shift.AFTER
			),
			cancellable = true
	)
	private void onPlayerInteractItem(PlayerInteractItemC2SPacket packet, CallbackInfo ci) {
		PlayerKt.getPlayerInteractEvent().call(new PlayerInteractEventData(
				player,
				InteractType.CLICK_ITEM,
				null,
				player.getStackInHand(packet.getHand())
		));
		if (PlayerKt.getPlayerInteractEvent().getCancelled()) {
			ci.cancel();
		}
	}

	@Inject(
			method = "onPlayerInteractBlock",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/server/world/ServerWorld;)V",
					shift = At.Shift.AFTER
			),
			cancellable = true

	)
	private void onPlayerInteractBlock(PlayerInteractBlockC2SPacket packet, CallbackInfo ci) {
		PlayerKt.getPlayerInteractEvent().call(new PlayerInteractEventData(
				player,
				InteractType.CLICK_BLOCK,
				null,
				player.getStackInHand(packet.getHand())
		));
		if (PlayerKt.getPlayerInteractEvent().getCancelled()) {
			ci.cancel();
		}
	}

	@Inject(
			method = "onPlayerAction",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/server/world/ServerWorld;)V",
					shift = At.Shift.AFTER
			),
			cancellable = true
	)
	private void onPlayerAction(PlayerActionC2SPacket packet, CallbackInfo ci) {
		var action = packet.getAction();
		if (action == PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND) {
			PlayerKt.getPlayerSwitchItemHandEvent().call(player);
			if (PlayerKt.getPlayerInteractEvent().getCancelled()) {
				ci.cancel();
			}
		}
		// drop item is handled in ServerPlayerEntityMixin
	}
}
