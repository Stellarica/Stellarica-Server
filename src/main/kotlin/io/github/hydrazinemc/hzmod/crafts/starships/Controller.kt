package io.github.hydrazinemc.hzmod.crafts.starships

import net.minecraft.util.math.Vec3d

open class Controller(val ship: Starship): ShipComponent {
	var cruiseDirection: Vec3d? = null
	var cruiseSpeed: Int = 0

}