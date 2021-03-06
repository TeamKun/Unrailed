package net.kunmc.lab.unrailed.rail

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block



/**
 * 1つのレールがつながったやつ(語彙力)
 */
abstract class AbstractRail {
    /**
     * @return すべてのレールブロック
     */
    abstract fun getAll(): MutableList<Block>

    /**
     * @return locationがレール群に含まれるか
     */
    fun isOn(location: Location): Boolean {
        return getAll().any { it.location == location }
    }

    fun add(loc: Location): Boolean {
        return add(loc.block)
    }

    abstract fun add(block: Block): Boolean

    /**
     * @return 位置的にレールが既存のレール群と接続可能か
     */
    abstract fun joinOf(loc: Location): Boolean
}