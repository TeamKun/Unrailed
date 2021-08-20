package net.kunmc.lab.unrailed.station

import net.kunmc.lab.unrailed.rail.Rail
import org.bukkit.Location

class Station(val rail: Rail) : AbstractStation() {
    override fun getEnterLocation(): Location {
        return rail.rails[0].location
    }

    override fun getExitLocation(): Location {
        return rail.rails.last().location
    }
}