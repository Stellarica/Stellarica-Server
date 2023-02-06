package net.stellarica.server.crafts.starships

import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.stellarica.server.Stellarica.Companion.ships
import net.stellarica.server.crafts.Craft
import net.stellarica.server.crafts.starships.weapons.Weapons


class Starship(origin: BlockPos, world: ServerWorld, owner: ServerPlayerEntity, direction: Direction) :
	Craft(origin, world, owner, direction){
	var cruiseDirection = Vec3d.ZERO!!
	var cruiseSpeed = 0

	val components = mutableSetOf<ShipComponent>(Weapons(this))

	lateinit var controller: PlayerController

	fun pilot(player: ServerPlayerEntity) {
		components.forEach { it.onPilot(player) }
		controller = PlayerController(this, player)
		ships.add(this)
	}

	fun unpilot() {
		controller.close()
		components.forEach { it.onUnpilot() }
		ships.remove(this)
	}
}