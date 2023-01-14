package net.stellarica.core.mixin

import kotlin.Pair;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity
import net.stellarica.core.event.PlayerKt;
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
	@Inject(
		method = "dropItem",
		at = @At("HEAD"),
			cancellable = true
	)
	private void onDropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> ci) {
		PlayerKt.getPlayerDropItemEvent().call(new Pair<ServerPlayerEntity, ItemStack>(this, stack));
		if (PlayerKt.getPlayerDropItemEvent().getCancelled()) ci.cancel();
	}
}