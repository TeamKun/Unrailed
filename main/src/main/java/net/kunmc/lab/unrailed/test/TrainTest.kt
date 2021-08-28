package net.kunmc.lab.unrailed.test

import com.github.bun133.flylib2.utils.ComponentUtils
import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.car.EngineCar
import net.kunmc.lab.unrailed.car.StorageCar
import net.kunmc.lab.unrailed.rail.Rail
import net.kunmc.lab.unrailed.train.Train
import net.kunmc.lab.unrailed.util.setForeach
import org.bukkit.Bukkit
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
        unrailed.server.scheduler.runTaskTimer(unrailed, Runnable { tick() }, 1, 1)
    }

    var trains = mutableMapOf<Train, Int>()

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
                    trains[train] = 0

                    for (i in 0..TrainLength - 2) {
                        val storageCar = StorageCar(unrailed, rail.rails[i].location.toCenterLocation())
                        train.addCar(storageCar)
                    }

                    train.isMoving = true
                    unrailed.isGoingOn = true
                    isGoingOn = true
                } catch (e: IndexOutOfBoundsException) {
                    //握りつぶします。はい。何か悪いですか？
                }
            }
        }
    }

    fun tick() {
        if (isGoingOn) {
            trains.setForeach {
                val firstLocation = it.getFirstLocation()
                if (firstLocation == null) {
                    Bukkit.broadcastMessage("Train Broken")
                    return@setForeach 0
                }
                val progress = it.rail.getIndex(firstLocation.block)
                if (progress == null) {
                    Bukkit.broadcastMessage("Train out of bounds")
                    return@setForeach 0
                } else {
                    if (progress != trains[it]) {
                        Bukkit.broadcastMessage("Train Progress:${progress}/${it.rail.rails.size}")
                    }
                    return@setForeach progress
                }
            }
        }
    }
}