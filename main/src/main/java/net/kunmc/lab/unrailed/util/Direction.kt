package net.kunmc.lab.unrailed.util

import org.bukkit.Axis
import org.bukkit.block.BlockFace
import org.bukkit.util.Vector
import kotlin.math.max
import kotlin.math.min

enum class Direction(val num: Int) {
    NORTH(0),
    WEST(1),
    SOUTH(2),
    EAST(3);

    companion object {
        fun fromNum(num: Int): Direction {
            return values().filter { it.num == Math.floorMod(num, 4) }[0]
        }

        fun fromBlockFace(blockFace: BlockFace): Direction? {
            return when (blockFace) {
                BlockFace.NORTH -> NORTH
                BlockFace.EAST -> EAST
                BlockFace.SOUTH -> SOUTH
                BlockFace.WEST -> WEST
                else -> null
            }
        }
    }

    fun toBlockFace(): BlockFace {
        return when (this) {
            NORTH -> {
                BlockFace.NORTH
            }
            SOUTH -> {
                BlockFace.SOUTH
            }
            EAST -> {
                BlockFace.EAST
            }
            WEST -> {
                BlockFace.WEST
            }
        }
    }

    fun toVector(): Vector {
        return toBlockFace().toVector()
    }

    fun toVector(scale: Double): Vector {
        return toVector().scale(scale)
    }

    fun rotate(times: Int = 1): Direction {
        return rotateRight(times)
    }

    fun rotateLeft(times: Int = 1): Direction {
        return fromNum(this.num + times)
    }

    fun rotateRight(times: Int = 1): Direction {
        return fromNum(this.num - times)
    }

    fun opposite(): Direction {
        return when(this){
            NORTH -> SOUTH
            WEST -> EAST
            SOUTH -> NORTH
            EAST -> WEST
        }
    }

    /**
     * @return このDirectionから見た角度
     */
    fun getDegree(to: Direction): Int {
        return 90 * (max(num, to.num) - min(num, to.num))
    }

    fun toAxis(): Axis {
        return when (this) {
            NORTH, SOUTH -> Axis.Z
            WEST, EAST -> Axis.X
        }
    }
}