package net.stellarica.core.crafts.starships

import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.stellarica.core.crafts.Craft


class Starship(origin: BlockPos, world: ServerWorld, owner: ServerPlayerEntity) : Craft(origin, world, owner) {
	val components = mutableSetOf<ShipComponent>()

	init {
		components.add(PlayerController(this))
	}

	fun pilot(player: ServerPlayerEntity) {
		components.forEach { it.onPilot(player) }
	}

	fun unpilot() {
		components.forEach { it.onUnpilot() }
	}
}