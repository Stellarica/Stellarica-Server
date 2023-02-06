package net.stellarica.server.utils

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i

@Deprecated("Placeholder until QKL's math module returns")
fun Vec3d.toBlockPos() = BlockPos(x, y, z)

@Deprecated("Placeholder until QKL's math module returns")
fun Vec3i.toBlockPos() = BlockPos(x, y, z)

@Deprecated("Placeholder until QKL's math module returns")
fun BlockPos.toVec3i() = Vec3i(x, y, z)

@Deprecated("Placeholder until QKL's math module returns")
fun BlockPos.toVec3d() = Vec3d(x.toDouble(), y.toDouble(), z.toDouble())

@Deprecated("Placeholder until QKL's math module returns")
fun Vec3i.toVec3d() = Vec3d(x.toDouble(), y.toDouble(), z.toDouble())

@Deprecated("Placeholder until QKL's math module returns")
fun Vec3d.toVec3i() = Vec3i(x, y, z)

@Deprecated("Placeholder until QKL's math module returns")
fun Vec3d.dot(other: Vec3d) = x * other.x + y * other.y + z * other.z