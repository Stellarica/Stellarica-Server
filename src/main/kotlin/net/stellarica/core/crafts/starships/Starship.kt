package net.stellarica.core.crafts.starships

import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.stellarica.core.crafts.Craft


class Starship(origin: BlockPos, world: ServerWorld, owner: ServerPlayerEntity) : Craft(origin, world, owner) {
	var cruiseDirection: Vec3d? = null
	var cruiseSpeed: Int = 0

	val components = mutableSetOf<ShipComponent>()

	lateinit var controller: PlayerController

	fun pilot(player: ServerPlayerEntity) {
		components.forEach { it.onPilot(player) }
		controller = PlayerController(this, player)
	}

	fun unpilot() {
		controller.close()
		components.forEach { it.onUnpilot() }
	}
}