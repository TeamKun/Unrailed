package net.kunmc.lab.unrailed.test

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.util.isRail
import org.bukkit.block.data.Rail
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class RailShapeTest(unrailed: Unrailed) : TestCase(unrailed) {
    companion object{
        private var ins:RailShapeTest? = null
        fun getInstance(unrailed: Unrailed):RailShapeTest{
            if(ins == null) ins = RailShapeTest(unrailed)
            return ins!!
        }
    }

    @EventHandler
    fun onRightClick(e:PlayerInteractEvent){
        if(isGoingOn){
            if(e.action == Action.RIGHT_CLICK_BLOCK){
                val block = e.clickedBlock!!
                if(block.isRail()){
                    e.player.sendMessage("Shape:${(block.blockData as Rail).shape}")
                }
            }
        }
    }
}