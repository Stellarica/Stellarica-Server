package net.stellarica.server.crafts.starships.weapons.projectiles

import net.minecraft.entity.LivingEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.stellarica.server.crafts.Craft

object TestProjectile : Projectile() {
	override val range = 100
	override val time = 40

	override fun onTick(world: ServerWorld, pos: Vec3d, dir: Vec3d): Double? {
		world.spawnParticles(ParticleTypes.FLAME, pos.x, pos.y, pos.z, 1, 0.0, 0.0, 0.0, 0.0)
		return null
	}

	override fun onHitCraft(pos: BlockPos, craft: Craft): Boolean {
		return false
	}

	override fun onHitEntity(entity: LivingEntity): Boolean {
		return false
	}

	override fun onHitBlock(world: ServerWorld, pos: BlockPos): Boolean {
		world.createExplosion(
			null,
			pos.x.toDouble(),
			pos.y.toDouble(),
			pos.z.toDouble(),
			4f,
			false,
			World.ExplosionSourceType.TNT
		)
		return false
	}
}