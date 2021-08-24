package net.kunmc.lab.unrailed.rail

import net.kunmc.lab.unrailed.util.getConnectedRail
import net.kunmc.lab.unrailed.util.getRailableRelative
import net.kunmc.lab.unrailed.util.isRail
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
                connected.first?.let { add(it) }
                connected.second?.let { add(it) }
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
        if (rails.isEmpty()) return true
        return rails.last().getRailableRelative()
            .map { it.location }.any { it == loc }
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

