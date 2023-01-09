package io.github.hydrazinemc.hzmod.crafts.starships

import net.minecraft.server.network.ServerPlayerEntity

interface ShipComponent {
	fun onPilot(player: ServerPlayerEntity) {}
	fun onTick() {}
	fun onUnpilot() {}
}