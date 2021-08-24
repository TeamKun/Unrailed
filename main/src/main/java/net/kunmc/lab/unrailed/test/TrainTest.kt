package net.kunmc.lab.unrailed.test

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.car.EngineCar
import net.kunmc.lab.unrailed.rail.Rail
import net.kunmc.lab.unrailed.train.Train
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class TrainTest(unrailed: Unrailed) : TestCase(unrailed) {
    companion object{
        private var ins:TrainTest? = null
        fun getInstance(unrailed: Unrailed): TrainTest {
            if(ins == null) ins = TrainTest(unrailed)
            return ins!!
        }
    }

    init {
        unrailed.server.pluginManager.registerEvents(this,unrailed)
    }

    @EventHandler
    fun onRightClick(e:PlayerInteractEvent){
        if(isGoingOn){
            if(e.action == Action.RIGHT_CLICK_BLOCK){
                val rail = Rail(e.clickedBlock!!)
                e.player.sendMessage("Rails Length:${rail.rails.size}")

                val train = Train(EngineCar(unrailed,rail.first.location.toCenterLocation()),rail,unrailed)

                train.isMoving = true
                unrailed.isGoingOn = true
            }
        }
    }
}