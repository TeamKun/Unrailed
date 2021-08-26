package net.kunmc.lab.unrailed.test

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.util.getConnectedRail
import net.kunmc.lab.unrailed.util.isRail
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class RailConnectingTest(unrailed: Unrailed) : TestCase(unrailed) {
    init {
        unrailed.server.pluginManager.registerEvents(this,unrailed)
    }

    companion object{
        private var ins:RailConnectingTest? = null
        fun getInstance(unrailed: Unrailed):RailConnectingTest{
            if(ins == null) ins = RailConnectingTest(unrailed)
            return ins!!
        }
    }

    @EventHandler
    fun onRightClick(e:PlayerInteractEvent){
        if(isGoingOn){
            if(e.action == Action.RIGHT_CLICK_BLOCK){
                val block = e.clickedBlock!!
                if(block.isRail()){
                    val connecting = block.getConnectedRail()
                    connecting.first?.let { it.type = Material.STONE }
                    connecting.second?.let { it.type = Material.STONE }
                }
            }
        }
    }
}