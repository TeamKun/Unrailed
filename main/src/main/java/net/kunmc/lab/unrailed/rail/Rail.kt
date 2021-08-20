package net.kunmc.lab.unrailed.rail

import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Rail

class Rail(val first: Block) : AbstractRail() {
    val rails = mutableListOf<Block>()

    override fun getAll(): MutableList<Block> = rails.toMutableList()

    override fun add(block: Block): Boolean {
        return if (block.isRail()) {
            if (contain(block)) return false
            if (joinOf(block.location)) {
                // このレール群と引数のレールが接続できる
                rails.add(block)
                val connected = block.getConnectedRail()
                if (connected != null) {
                    add(connected.first)
                    add(connected.second)
                }
                true
            } else {
                // できない
                false
            }
        } else {
            // そもそもレールじゃない
            false
        }
    }

    override fun joinOf(loc: Location): Boolean {
        if(rails.isEmpty()) return true
        return rails.last().getRelatives(BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.SOUTH)
            .map { it.location }.contains(loc)
    }

    /**
     * @param rail 実際のレールブロック
     * @return index このレール群に存在
     */
    fun getIndex(rail: Block): Int? {
        if (!rail.isRail()) return null
        val index = rails.indexOf(rail)
        if (index == -1) return null
        return index
    }

    /**
     * @param rail 実際のレールブロック
     * @return 引数のレールのひとつ前のレール
     */
    fun getBefore(rail: Block): Block? {
        val index = getIndex(rail) ?: return null
        val beforeIndex = index - 1
        if (beforeIndex < 0 || beforeIndex > rails.lastIndex) return null
        return rails[beforeIndex]
    }

    /**
     * @param rail 実際のレールブロック
     * @return 引数のレールのひとつ先のレール
     */
    fun getAfter(rail: Block): Block? {
        val index = getIndex(rail) ?: return null
        val afterIndex = index + 1
        if (afterIndex < 0 || afterIndex > rails.lastIndex) return null
        return rails[afterIndex]
    }

    fun contain(rail: Block): Boolean {
        if (!rail.isRail()) return false
        return rails.contains(rail)
    }

    fun contain(loc: Location): Boolean {
        return contain(loc.block)
    }

    init {
        add(first)
    }
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
    return listOf(BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST).map { getRelative(it) }
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
fun Block.getConnectedRail(): Pair<Block, Block>? {
    if (!isRail()) return null
    val blocks = when ((blockData as Rail).shape) {
        Rail.Shape.ASCENDING_EAST -> {
            getRelatives(BlockFace.EAST, BlockFace.EAST.oppositeFace)
        }
        Rail.Shape.NORTH_SOUTH -> {
            getRelatives(BlockFace.NORTH, BlockFace.SOUTH)
        }
        Rail.Shape.EAST_WEST -> {
            getRelatives(BlockFace.EAST, BlockFace.WEST)
        }
        Rail.Shape.ASCENDING_WEST -> {
            getRelatives(BlockFace.WEST, BlockFace.WEST.oppositeFace)
        }
        Rail.Shape.ASCENDING_NORTH -> {
            getRelatives(BlockFace.NORTH, BlockFace.NORTH.oppositeFace)
        }
        Rail.Shape.ASCENDING_SOUTH -> {
            getRelatives(BlockFace.SOUTH, BlockFace.SOUTH.oppositeFace)
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
        if (blocks[0].isRail() && blocks[1].isRail()) {
            Pair(blocks[0], blocks[1])
        }
        null
    } else {
        null
    }
}