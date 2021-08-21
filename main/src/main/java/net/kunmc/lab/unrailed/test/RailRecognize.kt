package net.kunmc.lab.unrailed.test

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.rail.Rail
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

/**
 * レール認識テスト
 * Passed 08/21 11:24
 */
class RailRecognize(unrailed: Unrailed) : TestCase(unrailed) {
    companion object {
        private var ins: RailRecognize? = null
        fun getInstance(unrailed: Unrailed): RailRecognize {
            if (ins == null) ins = RailRecognize(unrailed)
            return ins!!
        }
    }


    init {
        unrailed.server.pluginManager.registerEvents(this, unrailed)
    }

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        if (isGoingOn) {
            if (e.action == Action.RIGHT_CLICK_BLOCK) {
                val rail = Rail(e.clickedBlock!!)
                e.player.sendMessage("Rails Length:${rail.rails.size}")
            }
        }
    }

}