package net.kunmc.lab.unrailed.station

import net.kunmc.lab.unrailed.rail.Rail
import org.bukkit.Location
import org.bukkit.block.Block

/**
 * 駅
 */
abstract class AbstractStation {
    /**
     * @return 駅の入口のlocation
     */
    abstract fun getEnterLocation(): Location

    /**
     * @return 駅の出口のlocation
     */
    abstract fun getExitLocation(): Location

    /**
     * @return 駅構内のレールブロック
     */
    abstract fun getAllRails():List<Block>

    /**
     * @return Railが駅入口につながっているかどうか
     */
    fun isRailConnected(rail: Rail): Boolean {
        return rail.contain(getEnterLocation())
    }
}