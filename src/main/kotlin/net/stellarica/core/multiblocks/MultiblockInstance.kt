package net.stellarica.core.multiblocks

import net.minecraft.block.Block
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

	/**
	 * @return the world location of [position]
	 */
	fun getLocation(position: OriginRelative): BlockPos {
		return when (direction) {
			Direction.NORTH -> BlockPos(position.x, position.y, position.z)
			Direction.SOUTH -> BlockPos(-position.x, position.y, -position.z)
			Direction.EAST -> BlockPos(position.z, position.y, position.x)
			Direction.WEST -> BlockPos(-position.z, position.y, -position.x)
			else -> throw Exception("wtf happened here you dummy")
		}.add(origin)
	}

	/**
	 * @return the origin relative position of [loc]
	 */
	fun getOriginRelative(loc: BlockPos): OriginRelative {
		val relative = loc.subtract(origin)
		return when (direction) {
			Direction.NORTH -> OriginRelative(relative.x, relative.y, relative.z)
			Direction.SOUTH -> OriginRelative(-relative.x, relative.y, -relative.z)
			Direction.EAST -> OriginRelative(relative.z, relative.y, relative.x)
			Direction.WEST -> OriginRelative(-relative.z, relative.y, -relative.x)
			else -> throw Exception("wtf happened here you dummy")
		}
	}

	/**
	 * @return whether this contains a block at [loc].
	 * Does not take into account world
	 */
	fun contains(loc: BlockPos): Boolean {
		type.blocks.keys.forEach {
			if (it == getOriginRelative(loc)) return true
		}
		return false
	}

}