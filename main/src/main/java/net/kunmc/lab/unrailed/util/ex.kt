package net.kunmc.lab.unrailed.util

import org.bukkit.Location

fun Location.copy(): Location {
    return Location(this.world, this.x, this.y, this.z, this.yaw, this.pitch)
}