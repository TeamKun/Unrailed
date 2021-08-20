package net.kunmc.lab.unrailed.rail

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block


val Rails = listOf(Material.RAIL,Material.ACTIVATOR_RAIL,Material.POWERED_RAIL,Material.DETECTOR_RAIL)
/**
 * 1つのレールがつながったやつ(語彙力)
 */
abstract class AbstractRail {
    /**
     * @return すべてのレールブロック
     */
    abstract fun getAll():MutableList<Block>

    /**
     * @return locationがレール郡に含まれるか
     */
    fun isOn(location: Location):Boolean{
        return getAll().any{ it.location == location }
    }

    fun add(loc: Location): Boolean {
        return add(loc.block)
    }

    abstract fun add(block:Block): Boolean

    /**
     * @return 位置的にレールが既存のレール郡と接続可能か
     */
    abstract fun joinOf(loc:Location):Boolean
}