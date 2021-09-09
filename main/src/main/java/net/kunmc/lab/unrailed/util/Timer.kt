package net.kunmc.lab.unrailed.util

import org.bukkit.plugin.java.JavaPlugin

class Timer(javaPlugin: JavaPlugin) {
    init {
        javaPlugin.server.scheduler.runTaskTimer(
            javaPlugin,
            Runnable {
                if (time > 0) time--
            },
            1, 1
        )
    }

    var time = 0

    fun isUp() = time <= 0
}