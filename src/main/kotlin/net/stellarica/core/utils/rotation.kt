package net.stellarica.core.utils

import net.minecraft.util.BlockRotation
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import kotlin.math.cos
import kotlin.math.sin

/**
 * Rotate [loc] around [origin] by [theta] radians.
 * Note, [theta] positive = clockwise, negative = counter clockwise
 */
fun rotateCoordinates(loc: Vec3d, origin: Vec3d, theta: Double): Vec3d = Vec3d(
	origin.x + (((loc.x - origin.x) * cos(theta)) - ((loc.z - origin.z) * sin(theta))),
	loc.y,  // too many parentheses is better than too few
	origin.z + (((loc.x - origin.x) * sin(theta)) + ((loc.z - origin.z) * cos(theta))),
)

/**
 * Rotate [loc] [rotation] around [origin]
 */
fun rotateCoordinates(loc: Vec3d, origin: Vec3d, rotation: BlockRotation): Vec3d =
	rotateCoordinates(loc, origin, rotation.asRadians)

val BlockRotation.asRadians: Double
	get() = when (this) {
		BlockRotation.NONE -> 0.0
		BlockRotation.CLOCKWISE_90 -> Math.PI / 2
		BlockRotation.CLOCKWISE_180 -> Math.PI
		BlockRotation.COUNTERCLOCKWISE_90 -> -Math.PI / 2
	}

val BlockRotation.asDegrees: Double
	get() = Math.toDegrees(asRadians) // :iea:

fun Direction.rotate(rot: BlockRotation) = when (rot) {
	BlockRotation.NONE -> this
	BlockRotation.CLOCKWISE_90 -> this.rotateYClockwise()
	BlockRotation.CLOCKWISE_180 -> this.opposite
	BlockRotation.COUNTERCLOCKWISE_90 -> this.rotateYCounterclockwise()
}