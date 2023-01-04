package io.github.hydrazinemc.hzmod.util

import net.kyori.adventure.text.minimessage.MiniMessage
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity

fun ServerPlayerEntity.sendMiniMessage(message: String, actionBar: Boolean = false) {
	if (actionBar) this.sendActionBar(MiniMessage.miniMessage().deserialize(message))
	else this.sendMessage(MiniMessage.miniMessage().deserialize(message))
}

fun PlayerEntity.sendMiniMessage(message: String, actionBar: Boolean = false) {
	// todo: if we ever get a client mod this will crash and burn
	(this as ServerPlayerEntity).sendMiniMessage(message, actionBar)
}