package net.stellarica.core.crafts.starships.weapons

import net.minecraft.server.network.ServerPlayerEntity
import net.stellarica.core.crafts.starships.ShipComponent
import net.stellarica.core.crafts.starships.Starship
import net.stellarica.core.multiblocks.OriginRelative

class Weapons(val ship: Starship) : ShipComponent {
	val weapons = mutableMapOf<WeaponType, MutableSet<OriginRelative>>()

	override fun onPilot(player: ServerPlayerEntity) {
		// this could be improved
		ship.multiblocks.filter { it.type in WeaponType.values().map { it.multiblockType } }.forEach { multiblock ->
			//weapons.getOrPut(WeaponType.values().first{it.multiblockType == multiblock.get()?.type}) { mutableSetOf() }.add(multiblock)
		}
		println(weapons)
	}

	fun fire() {

		// the direction the pilot is facing
		val eye = ship.controller.player.rotationVector.normalize()
		/*
		weapons.forEach { (type, u) ->
			u.forEach { multiblock ->
				// the direction the weapon is facing
				val direction = multiblock.getLocation(type.direction).subtract(multiblock.getLocation(type.mount)).toVec3d().normalize()

				// if the angleBetween (in radians) is less than the type's cone, fire
				val angle = acos((eye.dot(direction) / (eye.length() * direction.length())).coerceIn(-1.0, 1.0))
				if (abs(angle) > type.cone + (PI / 4)) return@forEach
				else if(abs(angle) < type.cone) {
					type.projectile.shoot(
						ship,
						ship.controller.player,
						multiblock.getLocation(type.mount).toVec3d().add(0.5, 0.5, 0.5).add(eye),
						eye
					)
				}

				// this whole thing could definitely be improved
				val dirPitch = asin(-direction.y)
				val dirYaw = atan2(direction.x, direction.z)
				val eyePitch = asin(-eye.y)
				val eyeYaw = atan2(eye.x, eye.z)

				val pitch = eyePitch.coerceIn(dirPitch - type.cone, dirPitch + type.cone)
				val yaw = if (eyeYaw - type.cone > -PI) {
					eyeYaw.coerceIn(dirYaw - type.cone, dirYaw + type.cone)
				} else { // fix for atan2 range not going beneath -PI, breaking coerceIn
					eyeYaw
				}

				var dir = Vec3d(
					sin(yaw) * cos(pitch),
					-sin(pitch),
					cos(yaw) * cos(pitch)
				)

				val adjDirYaw = dirYaw - (2 * PI)
				if (eyeYaw != eyeYaw.coerceIn(adjDirYaw - type.cone, adjDirYaw + type.cone) && direction.z < 0 && eye.z < 0 && eye.x < 0) {
					// more fix for arctan range issues
					// literal duct tape
					dir = Vec3d(-dir.x, dir.y, dir.z)
				}
				//println("e: $eyeYaw, d: $adjDirYaw, c: ${type.cone} d-e${adjDirYaw - type.cone} d+e${adjDirYaw + type.cone}")

				type.projectile.shoot(
					ship,
					ship.controller.player,
					multiblock.getLocation(type.mount).toVec3d().add(0.5, 0.5, 0.5).add(dir),
					dir
				)
			}
		}
		*/
	}
}