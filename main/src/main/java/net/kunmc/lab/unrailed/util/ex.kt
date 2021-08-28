package net.kunmc.lab.unrailed.util

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Rail
import org.bukkit.entity.Minecart
import org.bukkit.util.Vector

val Rails = listOf(Material.RAIL, Material.ACTIVATOR_RAIL, Material.POWERED_RAIL, Material.DETECTOR_RAIL)
val RailFace = listOf(BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST)

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
    return Vector(this.x, this.y, this.z)
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
    val l = RailFace
    return l.map { getRelative(it) } + l.map { getRelative(it).getRelative(BlockFace.UP) } + l.map {
        getRelative(it).getRelative(
            BlockFace.DOWN
        )
    }
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
 * 指定方向(水平・垂直方向のみ)に存在するレールを1ブロック上、同高度、1ブロック下から探してくる
 */
fun Block.getRailRelative(face: BlockFace): Block? {
    return if (RailFace.contains(face)) {
        val blocks = listOf(
            getRelative(face),
            getRelative(face).getRelative(BlockFace.UP),
            getRelative(face).getRelative(BlockFace.DOWN)
        )
        blocks.filter { it.isRail() }.getOrNull(0)
    } else {
        null
    }
}

/**
 * RailのShapeに基づいてつながっているであろうRailを取得
 *
 * @see getConnectedRail
 */
fun Block.getConnectiveRail(): Pair<Block?, Block?> {
    if (!isRail()) return Pair(null, null)
    val faces = when ((blockData as Rail).shape) {
        Rail.Shape.ASCENDING_EAST -> {
            listOf(BlockFace.EAST, BlockFace.WEST)
        }
        Rail.Shape.ASCENDING_WEST -> {
            listOf(BlockFace.WEST, BlockFace.EAST)
        }
        Rail.Shape.ASCENDING_NORTH -> {
            listOf(BlockFace.NORTH, BlockFace.SOUTH)
        }
        Rail.Shape.ASCENDING_SOUTH -> {
            listOf(BlockFace.SOUTH, BlockFace.NORTH)
        }
        Rail.Shape.NORTH_SOUTH -> {
            listOf(BlockFace.NORTH, BlockFace.SOUTH)
        }
        Rail.Shape.EAST_WEST -> {
            listOf(BlockFace.EAST, BlockFace.WEST)
        }
        Rail.Shape.SOUTH_EAST -> {
            listOf(BlockFace.SOUTH, BlockFace.EAST)
        }
        Rail.Shape.SOUTH_WEST -> {
            listOf(BlockFace.SOUTH, BlockFace.WEST)
        }
        Rail.Shape.NORTH_WEST -> {
            listOf(BlockFace.NORTH, BlockFace.WEST)
        }
        Rail.Shape.NORTH_EAST -> {
            listOf(BlockFace.NORTH, BlockFace.EAST)
        }
    }

    val blocks = faces.map { getRailRelative(it) }

    return Pair(
        blocks[0].nullOr {
            if (it.isRail()) it else null
        },
        blocks[1].nullOr {
            if (it.isRail()) it else null
        }
    )

//    return if (blocks[0] != null && blocks[0]!!.isRail()) {
//        if (blocks[1] != null && blocks[1]!!.isRail()) {
//            Pair(blocks[0], blocks[1])
//        } else {
//            Pair(blocks[0], null)
//        }
//    } else {
//        if (blocks[1] != null && blocks[1]!!.isRail()) {
//            Pair(null, blocks[1])
//        }
//        Pair(null, null)
//    }
}

/**
 * RailのShapeに基づいてつながっているRailを取得
 */
fun Block.getConnectedRail(): Pair<Block?, Block?> {
    var connective = getConnectiveRail()

    if (connective.first != null) {
        // 相手側からもつながっているかチェック
        val b = connective.first!!.getConnectiveRail().contain(this)
        if (!b) {
            connective = connective.first(null)
        }
    }

    if (connective.second != null) {
        // 相手側からもつながっているかチェック
        val b = connective.second!!.getConnectiveRail().contain(this)
        if (!b) {
            connective = connective.second(null)
        }
    }

    return connective
}


/**
 * このBlockから見てどの方向にあるか
 */
fun Block.getBlockFace(other: Block): BlockFace? {
    return BlockFace.values().map { Pair(it, getRelative(it)) }.filter { it.second == other }.getOrNull(0)?.first
}

/**
 * たぶん変換
 */
fun BlockFace.toVector(): Vector {
    return Vector(this.modX, this.modY, this.modZ)
}

/**
 * レールが両端接続済みか
 */
fun Block.isConnectedBoth(): Boolean {
    return if (!isRail()) false
    else {
        getConnectedRail().isNotNull()
    }
}

/**
 * @return Pairどちらかがnullかどうか
 */
fun <T, K> Pair<T?, K?>.isNull(): Boolean {
    return this.first == null || this.second == null
}

fun <T, K> Pair<T?, K?>.isNotNull(): Boolean {
    return !isNull()
}

fun <T> Pair<T?, T?>.contain(t: T): Boolean {
    var b = false
    if (first != null) {
        b = b || first!! == t
    }
    if (second != null) {
        b = b || second!! == t
    }
    return b
}

fun <T, K> Pair<T?, K?>.first(t: T): Pair<T, K?> {
    return Pair(t, this.second)
}

fun <T, K> Pair<T?, K?>.second(k: K): Pair<T?, K> {
    return Pair(this.first, k)
}

/**
 * @return レールがto(レール)に接続可能か
 */
fun Block.isConnective(to: Block): Boolean {
    if (!isRail() || !to.isRail()) return false
    return getRailableRails().map { it.location }.contains(to.location)
}

fun <T, R> T?.nullOr(f: (T) -> R): R? {
    return if (this != null) {
        f(this)
    } else {
        null
    }
}

fun <T, K> MutableMap<T, K>.setForeach(f: (T) -> K) {
    for (key in this.keys) {
        this[key] = f(key)
    }
}