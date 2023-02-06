package net.stellarica.server.crafts.starships

import net.minecraft.server.network.ServerPlayerEntity

interface ShipComponent {
	fun onPilot(player: ServerPlayerEntity) {}
	fun onTick() {}
	fun onUnpilot() {}
}