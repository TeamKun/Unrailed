package net.kunmc.lab.unrailed.test

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.car.EngineCar
import net.kunmc.lab.unrailed.car.StorageCar
import net.kunmc.lab.unrailed.rail.Rail
import net.kunmc.lab.unrailed.train.Train
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import java.lang.IndexOutOfBoundsException

class TrainTest(unrailed: Unrailed) : TestCase(unrailed) {
    companion object {
        private var ins: TrainTest? = null
        fun getInstance(unrailed: Unrailed): TrainTest {
            if (ins == null) ins = TrainTest(unrailed)
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

                val TrainLength = 10     // 10両編成をスポーン

                try {
                    val train = Train(
                        EngineCar(unrailed, rail.rails[TrainLength - 1].location.toCenterLocation()),
                        rail,
                        unrailed
                    )

                    for (i in 0..TrainLength - 2) {
                        val storageCar = StorageCar(unrailed, rail.rails[i].location.toCenterLocation())
                        train.addCar(storageCar)
                    }

                    train.isMoving = true
                    unrailed.isGoingOn = true
                    isGoingOn = false
                } catch (e: IndexOutOfBoundsException) {
                    //握りつぶします。はい。何か悪いですか？
                }
            }
        }
    }
}