package net.kunmc.lab.unrailed.rail

import net.kunmc.lab.unrailed.util.*
import org.bukkit.Location
import org.bukkit.block.Block

class Rail(val first: Block) : AbstractRail() {
    val rails = mutableListOf<Block>()

    override fun getAll(): MutableList<Block> = rails.toMutableList()

    override fun add(block: Block): Boolean {
        return add(block, listOf())
    }

    /**
     * @param exceptFor 特定のblockを除いて探索
     */
    fun add(block: Block, exceptFor: List<Block>): Boolean {
        return if (block.isRail()) {
            if (exceptFor.contains(block)) return false
            if (contain(block)) return false
            if (joinOf(block.location)) {
                // このレール群と引数のレールが接続できる
                val edgeIndexed = getEdgeIndexed() // 最適化可能
                if (edgeIndexed == null) {
                    // 最初のレール
                    rails.add(block)
                } else {
                    val connectiveRail = edgeIndexed.toList().filter {
                        it.first.isConnective(block)
                    }
                    if (connectiveRail.isEmpty()) throw Exception("in Rail#add Rail can join,while not connective to any.")

                    when (val maxRailIndex = connectiveRail.maxOfOrNull { it.second }!!) {
                        0 -> {
                            // 最初のレールの前にレールが設置された
                            rails.add(0, block)
                        }
                        rails.lastIndex -> {
                            // 最後のレールの後にレールが設置された
                            rails.add(block)
                        }
                        else -> {
                            val connectiveMaxRail = connectiveRail.maxByOrNull { it.second }

                            throw RailBlockException(
                                this,
                                block,
                                "in Rail#add Rail added to neither first Rail nor final Rail,index:${maxRailIndex},lastIndex:${rails.lastIndex},connectiveMaxRail:${connectiveMaxRail}"
                            )
                        }
                    }
                }


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

    fun getEdge(): Pair<Block, Block>? {
        val indexed = getEdgeIndexed() ?: return null
        return Pair(indexed.first.first, indexed.second.first)
    }

    /**
     * レールの端っこを返す
     */
    fun getEdgeIndexed(): Pair<Pair<Block, Int>, Pair<Block, Int>>? {
        if (rails.isEmpty()) return null
        else {
            if (rails.size == 1) return Pair(Pair(rails[0], 0), Pair(rails[0], 0))
            else {
                // TODO 探索
                val edge = rails.mapIndexed { index, block ->
                    Pair(block, index)
                }.filter {
                    val connectedBoth = it.first.isConnectedBoth(rails)
                    if (connectedBoth == null) return@filter false
                    else return@filter !connectedBoth
                }

                if (edge.size == 2) {
                    return Pair(edge[0], edge[1])
                } else {
                    throw RailException(this, "in getEdgeIndexed,The Edge Size is not 2")
                }
            }
        }
    }

    /**
     * Railsに含まれているものの中で両方に接続しているか
     * @return null -> Block is not Rail
     */
    private fun Block.isConnectedBoth(rails: MutableList<Block>): Boolean? {
        if (!isRail()) return null
        return getConnectedRail().toList().filterNotNull()
            .filter { rails.map { rail -> rail.location }.contains(it.location) }.size == 2
    }

    /**
     * @return locationがこのレール群に接続可能か
     */
    override fun joinOf(loc: Location): Boolean {
        if (rails.isEmpty()) return true
        val edge = getEdge() ?: return true
        return edge.toList().map { ed -> ed.getRailableRelative().map { it.location } }
            .any { list -> list.any { it == loc } }
        // 何が起きているかは聞かないでくれ
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


open class RailException(val rail: Rail, message: String) : Exception(message) {
    open fun print() {
        println("RailException:{Rail:${rail},Message:${message}}")
    }
}

class RailBlockException(rail: Rail, val block: Block, message: String) : RailException(rail, message) {
    override fun print() {
        println("RailBlockException:{Rail:${rail},Block:${block},Message:${message}}")
    }
}