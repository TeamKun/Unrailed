package net.kunmc.lab.unrailed.util

import org.bukkit.block.BlockFace
import org.bukkit.util.Vector

enum class Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST;

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
}