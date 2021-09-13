package net.kunmc.lab.unrailed.util

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.game.phase.GamePhase
import net.kunmc.lab.unrailed.game.phase.Phase
import net.kunmc.lab.unrailed.game.player.GamePlayer
import net.kunmc.lab.unrailed.train.Train
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Rail
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Minecart
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team
import org.bukkit.util.Vector
import kotlin.random.Random

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

fun Material.isRail(): Boolean {
    return Rails.contains(this)
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

fun <T> T?.nullMap(f: () -> T): T {
    if (this == null) {
        return f()
    } else return this
}

fun <T, K> MutableMap<T, K>.setForeach(f: (T) -> K) {
    for (key in this.keys) {
        this[key] = f(key)
    }
}

fun Box.fill(material: Material) {
    getBlocks().forEach {
        it.type = material
    }
}

fun JavaPlugin.registerEvents(listener: Listener) {
    this.server.pluginManager.registerEvents(listener, this)
}

fun RandomDo(vararg functions: () -> Unit) {
    functions[Random.nextInt(0, functions.size)]()
}

fun ConfigurationSection.getOrDefault(path: String, default: Any = ""): Any {
    val o = this[path]
    if (o != null) return o
    else {
        this[path] = default
    }
    return this[path]!!
}

inline fun <reified T> ConfigurationSection.getOrDefaultT(path: String, default: T): T {
    val o = this[path]
    return if (o != null) {
        if (o !is T) {
            this[path] = default
            this[path]!! as T
        } else {
            o
        }
    } else {
        this[path] = default
        this[path] as T
    }
}

fun ConfigurationSection.getConfigurationSectionOrDefault(path: String): ConfigurationSection {
    val section = getConfigurationSection(path)
    return section ?: createSection(path)
}

fun ConfigurationSection.reset() {
    for (key in getKeys(true)) {
        // reset Config
        this[key] = null
    }
}

fun Int.getCenter(): Int {
    return if (this % 2 == 0) {
        this / 2
    } else {
        (this + 1) / 2
    }
}

/**
 * 細かい数字を切り捨てる
 */
fun Vector.toBlockPos(): Vector {
    return Vector(blockX, blockY, blockZ)
}

/**
 * BlockFaceを回したものを返す
 * @param times 90 * 何回分回すか
 */
fun BlockFace.rotateAroundY(times: Int): BlockFace? {
    return when (this) {
        BlockFace.UP, BlockFace.DOWN -> this
        BlockFace.NORTH,
        BlockFace.EAST,
        BlockFace.SOUTH,
        BlockFace.WEST -> {
            val direction = Direction.fromBlockFace(this)!!
            direction.rotateLeft(times).toBlockFace()
        }
        else -> null
    }
}

fun Rail.Shape.rotateRight(times: Int): Rail.Shape {
    val t = times % 4
    var shape = this

    for (i in 0 until t) {
        // Rotate
        shape = shape.rotateRightOnce()
    }

    return shape
}

fun Rail.Shape.rotateRightOnce(): Rail.Shape {
    return when (this) {
        Rail.Shape.NORTH_SOUTH -> Rail.Shape.EAST_WEST
        Rail.Shape.EAST_WEST -> Rail.Shape.NORTH_SOUTH
        Rail.Shape.ASCENDING_EAST -> Rail.Shape.ASCENDING_SOUTH
        Rail.Shape.ASCENDING_WEST -> Rail.Shape.ASCENDING_NORTH
        Rail.Shape.ASCENDING_NORTH -> Rail.Shape.ASCENDING_EAST
        Rail.Shape.ASCENDING_SOUTH -> Rail.Shape.ASCENDING_WEST
        Rail.Shape.SOUTH_EAST -> Rail.Shape.SOUTH_WEST
        Rail.Shape.SOUTH_WEST -> Rail.Shape.NORTH_WEST
        Rail.Shape.NORTH_WEST -> Rail.Shape.NORTH_EAST
        Rail.Shape.NORTH_EAST -> Rail.Shape.SOUTH_EAST
    }
}

/**
 * 指定したプレイヤーが現在進行中のゲームに参加しているかどうか
 */
@Deprecated(message = "Use Player#.isPhase(Phase)")
fun Player.isGoingOn(): Boolean {
    val phase = getPhase() ?: return false
    return phase is GamePhase
}

/**
 * 指定したプレイヤーが指定したPhaseかどうか
 */
inline fun <reified T : Phase> Player.isPhase(t: T): Boolean {
    val phase = getPhase() ?: return false
    return phase is T
}

fun Player.getPhase(): Phase? {
    return Unrailed.goingOnGame?.getLane(this)?.game?.nowPhase
}

inline fun <reified T : Enum<T>> T.random(): T {
    return enumValues<T>().random()
}

/**
 * @return null when filtered EnumValues are empty
 */
inline fun <reified T : Enum<T>> T.random(exceptFor: List<T>): T? {
    return try {
        enumValues<T>().filter { !exceptFor.contains(it) }.random()
    } catch (e: NoSuchElementException) {
        null
    }
}

fun Scoreboard.getOrRegisterTeam(name: String): Team {
    val team = getTeam(name)
    if (team != null) {
        return team
    } else {
        return registerNewTeam(name)
    }
}

fun Team.setColor(color: WoolColor): Team {
    val textColor = color.namedTextColor
    if (textColor != null) {
        this.color(textColor)
    }
    return this
}

fun Team.setColorForce(color: WoolColor): Team {
    this.color(color.toNamedTextColorForce())
    return this
}

fun Block.isWool(): Boolean {
    return type.isWool()
}

fun Material.isWool(): Boolean {
    return WoolColor.values().map { it.woolMaterial }.contains(this)
}

fun Player.isJoinedGame(): Boolean {
    return Unrailed.goingOnGame?.players?.map { it.p }?.contains(this).nullMap { false }
}