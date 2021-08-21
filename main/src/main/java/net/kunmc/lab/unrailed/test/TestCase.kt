package net.kunmc.lab.unrailed.test

import net.kunmc.lab.unrailed.Unrailed
import org.bukkit.event.Listener

abstract class TestCase(val unrailed: Unrailed) : Listener {
    var isGoingOn = false
}