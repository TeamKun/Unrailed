package net.kunmc.lab.unrailed.test

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.structure.StructureCollection
import net.kunmc.lab.unrailed.util.Direction
import net.kunmc.lab.unrailed.util.Timer
import net.kunmc.lab.unrailed.util.registerEvents
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class RotateStructureTest(unrailed: Unrailed) : TestCase(unrailed) {
    init {
        unrailed.registerEvents(this)
    }

    companion object {
        private var ins: RotateStructureTest? = null
        fun getInstance(unrailed: Unrailed): RotateStructureTest {
            if (ins != null) return ins!!
            ins = RotateStructureTest(unrailed)
            return ins!!
        }
    }

    val copiedStructure = StructureCollection.getInstance(unrailed).stations.SandStation.copy()
    var timer = Timer(unrailed)

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        if (isGoingOn) {
            if (e.action == Action.RIGHT_CLICK_BLOCK) {
                if (timer.isUp()) {
                    val clickedBlock = e.clickedBlock!!
                    val direction = Direction.fromBlockFace(e.player.facing)!!
                    val degree = copiedStructure.direction!!.getDegree(direction).toDouble()
                    val rotatedStationStructure =
                        copiedStructure.copy().rotateAroundY(Math.toRadians(degree), clickedBlock.world)
                    e.player.sendMessage("Rotated for $degree degree.")
                    rotatedStationStructure.copyTo(clickedBlock.location)
                    timer.time = 20 // 1秒クールダウン
                } else {
                    e.player.sendMessage("CoolDown!")
                }
            }
        }
    }
}