package net.stellarica.core.crafts.starships.weapons.projectiles

import net.minecraft.entity.LivingEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.RaycastContext
import net.stellarica.core.Stellarica.Companion.ships
import net.stellarica.core.crafts.Craft
import net.stellarica.core.utils.toBlockPos
import net.stellarica.events.Event
import net.stellarica.events.server.StartServerTickEvent

abstract class Projectile {
	abstract val range: Int
	abstract val time: Int
	val speed: Double
		get() = range / time.toDouble()

	fun shoot(shooter: Craft, pilot: ServerPlayerEntity, from: Vec3d, dir: Vec3d) {
		val world = shooter.world
		var currentPos = from
		StartServerTickEvent.listen(0) {
			val hit = world.raycast(
				RaycastContext(
					from,
					from.add(dir.multiply(speed)),
					RaycastContext.ShapeType.OUTLINE,
					RaycastContext.FluidHandling.ANY,
					pilot
				)
			)

			// todo: handle entity raycast
			if (hit.type == HitResult.Type.BLOCK) {
				val pos = hit.pos.toBlockPos()
				val craft = ships.filter { it.origin.getSquaredDistance(pos) < 100 * 100 }
					.firstOrNull { it.contains(pos) }
				if (craft != null) {
					if (craft != shooter && !onHitCraft(pos, craft)) this.unregister()
				} else {
					if (!onHitBlock(world, pos)) this.unregister()
				}
			} else {
				currentPos = currentPos.add(dir.multiply(onTick(world, currentPos, dir) ?: speed))
			}

			return@listen Event.Result.CONTINUE
		}
	}

	/**
	 * Called every tick this projectile is active
	 * @return the distance to travel
	 */
	abstract fun onTick(world: ServerWorld, pos: Vec3d, dir: Vec3d): Double?

	/**
	 * Called when the projectile hits a craft
	 * @return whether to continue moving
	 */
	abstract fun onHitCraft(pos: BlockPos, craft: Craft): Boolean

	/**
	 * Called when the projectile hits an entity
	 * @return whether to continue moving
	 */
	abstract fun onHitEntity(entity: LivingEntity): Boolean

	/**
	 * Called when the projectile hits a blocks
	 * @return whether to continue moving
	 */
	abstract fun onHitBlock(world: ServerWorld, pos: BlockPos): Boolean
}