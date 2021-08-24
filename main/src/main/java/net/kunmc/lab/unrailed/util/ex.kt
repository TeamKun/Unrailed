package net.kunmc.lab.unrailed.util

import net.kunmc.lab.unrailed.rail.Rails
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Rail
import org.bukkit.entity.Minecart
import org.bukkit.util.Vector

fun Location.copy(): Location {
    return Location(this.world, this.x, this.y, this.z, this.yaw, this.pitch)
}

/**
 * lengthがscaleになるように調整
 */
fun Vector.scale(scale: Double): Vector {
    return multiply(scale / length())
}

/**
 * Copy the vector
 */
fun Vector.copy(): Vector {
    return Vector(this.x,this.y,this.z)
}

/**
 * Minecartがレールに乗っているかどうか
 */
fun Minecart.isOnRail(): Boolean {
    return location.block.isRail()
}

fun Block.isRail(): Boolean {
    return Rails.contains(this.type)
}

fun Block.getAllRelative(): List<Block> {
    return BlockFace.values().map {
        getRelative(it)
    }
}

/**
 * RailがつながれるBlockFace選択
 */
@Suppress("SpellCheckingInspection")
fun Block.getRailableRelative(): List<Block> {
    val l = listOf(BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST)
    return l.map { getRelative(it) } + l.map { getRelative(it).getRelative(BlockFace.UP) } + l.map { getRelative(it).getRelative(
        BlockFace.DOWN) }
}

/**
 * RailがつながれるBlockFaceにあるRail
 */
fun Block.getRailableRails(): List<Block> {
    return getRailableRelative().filter { it.isRail() }
}

fun Block.getRelatives(vararg blockFaces: BlockFace): List<Block> {
    return blockFaces.map { getRelative(it) }
}

/**
 * RailのShapeに基づいてつながっているRailを取得
 */
fun Block.getConnectedRail(): Pair<Block?, Block?> {
    if (!isRail()) return Pair(null, null)
    val blocks = when ((blockData as Rail).shape) {
        Rail.Shape.ASCENDING_EAST -> {
            listOf(getRelative(BlockFace.EAST).getRelative(BlockFace.UP), getRelative(BlockFace.EAST.oppositeFace))
        }
        Rail.Shape.ASCENDING_WEST -> {
            listOf(getRelative(BlockFace.WEST).getRelative(BlockFace.UP), getRelative(BlockFace.WEST.oppositeFace))
        }
        Rail.Shape.ASCENDING_NORTH -> {
            listOf(getRelative(BlockFace.NORTH).getRelative(BlockFace.UP), getRelative(BlockFace.NORTH.oppositeFace))
        }
        Rail.Shape.ASCENDING_SOUTH -> {
            listOf(getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP), getRelative(BlockFace.SOUTH.oppositeFace))
        }
        Rail.Shape.NORTH_SOUTH -> {
            getRelatives(BlockFace.NORTH, BlockFace.SOUTH)
        }
        Rail.Shape.EAST_WEST -> {
            getRelatives(BlockFace.EAST, BlockFace.WEST)
        }
        Rail.Shape.SOUTH_EAST -> {
            getRelatives(BlockFace.SOUTH, BlockFace.EAST)
        }
        Rail.Shape.SOUTH_WEST -> {
            getRelatives(BlockFace.SOUTH, BlockFace.WEST)
        }
        Rail.Shape.NORTH_WEST -> {
            getRelatives(BlockFace.NORTH, BlockFace.WEST)
        }
        Rail.Shape.NORTH_EAST -> {
            getRelatives(BlockFace.NORTH, BlockFace.EAST)
        }
    }

    return if (blocks.size == 2) {
        if (blocks[0].isRail()) {
            if (blocks[1].isRail()) {
                Pair(blocks[0], blocks[1])
            } else {

                Pair(blocks[0], null)
            }
        } else {
            if (blocks[1].isRail()) {
                Pair(null, blocks[1])

            }
            Pair(null, null)
        }
    } else {
        Pair(null, null)
    }
}


/**
 * このBlockから見てどの方向にあるか
 */
fun Block.getBlockFace(other:Block): BlockFace? {
    return BlockFace.values().map { Pair(it,getRelative(it)) }.filter { it.second == other }.getOrNull(0)?.first
}

/**
 * たぶん変換
 */
fun BlockFace.toVector():Vector{
    return Vector(this.modX,this.modY,this.modZ)
}