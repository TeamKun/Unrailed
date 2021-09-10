package net.kunmc.lab.unrailed.test

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.util.Direction
import net.kunmc.lab.unrailed.util.registerEvents
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class DegreeTest(unrailed: Unrailed) : TestCase(unrailed) {
    init {
        unrailed.registerEvents(this)
    }

    companion object {
        private var ins: DegreeTest? = null
        fun getInstance(unrailed: Unrailed): DegreeTest {
            if (ins != null) return ins!!
            ins = DegreeTest(unrailed)
            return ins!!
        }
    }

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        if (isGoingOn) {
            if (e.action == Action.RIGHT_CLICK_BLOCK) {
                val direction = Direction.fromBlockFace(e.player.facing)!!

                val di = Direction.EAST

                e.player.sendMessage("Degree Between $di and $direction is ${di.getDegree(direction)}")
            }
        }
    }
}