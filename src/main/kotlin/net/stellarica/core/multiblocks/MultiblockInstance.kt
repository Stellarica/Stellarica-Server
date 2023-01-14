package net.stellarica.core.multiblocks

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

data class MultiblockInstance(
	val origin: BlockPos,
	val world: World,
	val direction: Direction,
	val type: MultiblockType
) {
	fun validate() = type.validate(direction, origin, world)
}