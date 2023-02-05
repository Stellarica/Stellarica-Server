package net.stellarica.core.utils

import net.minecraft.util.BlockRotation
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

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