package net.stellarica.server.crafts.starships.weapons

import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Vec3d
import net.stellarica.server.crafts.starships.ShipComponent
import net.stellarica.server.crafts.starships.Starship
import net.stellarica.server.utils.coordinates.OriginRelative
import net.stellarica.server.utils.dot
import net.stellarica.server.utils.toVec3d
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class Weapons(val ship: Starship) : ShipComponent {
	val weapons = mutableMapOf<WeaponType, MutableSet<OriginRelative>>()

	override fun onPilot(player: ServerPlayerEntity) {
		ship.multiblocks.forEach { pos ->
			val type = ship.getMultiblock(pos).type
			WeaponType.values().firstOrNull {it.multiblockType == type}?.let{
				weapons.getOrPut(it) { mutableSetOf() }.add(pos)
			}
		}
	}

	fun fire() {
		// the direction the pilot is facing
		val eye = ship.controller.player.rotationVector.normalize()
		weapons.forEach { (type, u) ->
			u.map { ship.getMultiblock(it) }.forEach { multiblocks ->
				// the direction the weapon is facing
				val direction = multiblocks.getLocation(type.direction).subtract(multiblocks.getLocation(type.mount)).toVec3d().normalize()

				// if the angleBetween (in radians) is less than the type's cone, fire
				val angle = acos((eye.dot(direction) / (eye.length() * direction.length())).coerceIn(-1.0, 1.0))
				if (abs(angle) > type.cone + (PI / 4)) return@forEach
				else if(abs(angle) < type.cone) {
					type.projectile.shoot(
						ship,
						ship.controller.player,
						multiblocks.getLocation(type.mount).toVec3d().add(0.5, 0.5, 0.5).add(eye),
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

				type.projectile.shoot(
					ship,
					ship.controller.player,
					multiblocks.getLocation(type.mount).toVec3d().add(0.5, 0.5, 0.5).add(dir),
					dir
				)
			}
		}
	}
}