package net.kunmc.lab.unrailed.listener

import net.kunmc.lab.unrailed.Unrailed
import org.bukkit.event.Listener

/**
 * Listener Base
 *
 * @note no need to register implemented class
 */
open class ListenerBase(val unrailed: Unrailed) : Listener {

    init {
        println("ListenerBase INIT")
        EventAll.getInstance(unrailed).register(this)
    }
}