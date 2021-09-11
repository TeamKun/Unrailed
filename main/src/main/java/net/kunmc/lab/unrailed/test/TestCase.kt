package net.kunmc.lab.unrailed.test

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.listener.ListenerBase
import org.bukkit.event.Listener

abstract class TestCase(unrailed: Unrailed) : ListenerBase(unrailed) {
    var isGoingOn = false
}