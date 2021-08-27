package net.kunmc.lab.unrailed.test

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.rail.Rail
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class EdgeTest(unrailed: Unrailed) : TestCase(unrailed) {
    init {
        unrailed.server.pluginManager.registerEvents(this, unrailed)
    }

    companion object{
        private var ins:EdgeTest? = null
        fun getInstance(unrailed: Unrailed):EdgeTest{
            if(ins == null) ins = EdgeTest(unrailed)
            return ins!!
        }
    }

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        if (isGoingOn) {
            if (e.action == Action.RIGHT_CLICK_BLOCK) {
                val block = e.clickedBlock!!
                val rail = Rail(block)
                val edge = rail.getEdge()
                if (edge == null) {
                    e.player.sendMessage("Edge Not Found")
                }else{
                    e.player.sendMessage("Edge1:${edge.first},Edge2:${edge.second}")
                }
            }
        }
    }
}