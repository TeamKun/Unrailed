package net.kunmc.lab.unrailed.test

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.car.EngineCar
import net.kunmc.lab.unrailed.rail.Rail
import net.kunmc.lab.unrailed.train.Train
import net.kunmc.lab.unrailed.util.copy
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

/**
 * ワゴンをくっつけて動くかテスト
 */
class TrainCombineTest(val unrailed: Unrailed) : Listener {
    companion object {
        private var ins: TrainCombineTest? = null
        fun getInstance(unrailed: Unrailed): TrainCombineTest {
            if (ins == null) {
                val i = TrainCombineTest(unrailed)
                ins = i
            }
            return ins!!
        }
    }

    var isGoingOn = false


    init {
        unrailed.server.pluginManager.registerEvents(this, unrailed)
    }

    var train: Train? = null

    @EventHandler
    fun onPlaceBlock(e: BlockPlaceEvent) {
        if (isGoingOn) {
            e.isCancelled = true
            e.player.sendMessage("開始!!!")
            for (x in e.block.location.copy().blockX..e.block.location.copy().blockX + 100) {
                val loc = e.block.location.copy()
                loc.x = x.toDouble()
                loc.copy().add(0.0, -1.0, 0.0).block.type = Material.STONE
                loc.block.type = Material.RAIL
            }

            val rail = Rail(e.block.location.copy().block)

            train = Train(EngineCar(unrailed, rail.first.location.copy().toCenterLocation()), rail, unrailed)

            train!!.isMoving = true
            unrailed.isGoingOn = true

            println("Rail Length:${rail.rails.size}")
        }
    }
}