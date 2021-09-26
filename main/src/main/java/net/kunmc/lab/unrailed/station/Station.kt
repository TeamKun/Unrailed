package net.kunmc.lab.unrailed.station

import net.kunmc.lab.unrailed.rail.Rail
import net.kunmc.lab.unrailed.util.Direction
import org.bukkit.Location
import org.bukkit.block.Block

class Station(val rail: Rail) : AbstractStation() {
    companion object {
        fun generateFrom(block: Block, direction: Direction): Station? {
            val rail = Rail(block, direction = direction)
            if (rail.rails.isEmpty()) return null
            return Station(rail)
        }
    }

    override fun getEnterLocation(): Location {
        return rail.rails[0].location
    }

    override fun getExitLocation(): Location {
        return rail.rails.last().location
    }

    override fun getAllRails(): List<Block> = rail.rails

    /**
     * レールがこの駅につながっているかどうか
     */
    fun isConnected(rail: Rail) = rail.containAny(this.rail)
}