package net.stellarica.server.crafts.starships.weapons

import net.minecraft.util.Identifier
import net.stellarica.server.Stellarica.Companion.identifier
import net.stellarica.server.crafts.starships.weapons.projectiles.Projectile
import net.stellarica.server.crafts.starships.weapons.projectiles.TestProjectile
import net.stellarica.server.multiblocks.MultiblockHandler
import net.stellarica.server.utils.coordinates.OriginRelative
import kotlin.math.PI

enum class WeaponType(
	val projectile: Projectile,
	val direction: OriginRelative,
	val cone: Double,
	val mount: OriginRelative,
	val priority: Int,

	private val multiblockId: Identifier
) {
	TEST_WEAPON(TestProjectile, OriginRelative(3, 0, 0), PI / 8, OriginRelative(2, 0, 0), 1, identifier("test_weapon"));

	val multiblockType by lazy {
		MultiblockHandler.types.first { it.id == multiblockId }
	}
}
