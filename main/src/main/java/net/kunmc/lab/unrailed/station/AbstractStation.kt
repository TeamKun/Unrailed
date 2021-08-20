package net.kunmc.lab.unrailed.station

import net.kunmc.lab.unrailed.rail.Rail
import org.bukkit.Location

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
     * @return Railが駅入口につながっているかどうか
     */
    fun isRailConnected(rail: Rail): Boolean {
        return rail.contain(getEnterLocation())
    }
}