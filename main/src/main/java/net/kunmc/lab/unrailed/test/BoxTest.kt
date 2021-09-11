package net.kunmc.lab.unrailed.test

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.util.Box
import net.kunmc.lab.unrailed.util.Direction
import net.kunmc.lab.unrailed.util.registerEvents
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class BoxTest(unrailed: Unrailed) : TestCase(unrailed) {
    companion object {
        private var ins: BoxTest? = null
        fun getInstance(unrailed: Unrailed): BoxTest {
            if (ins == null) ins = BoxTest(unrailed)
            return ins!!
        }
    }


    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        if (isGoingOn) {
            if (e.action == Action.RIGHT_CLICK_BLOCK) {
                val clickedBlock = e.clickedBlock!!
                val box = Box(clickedBlock.location, 5, 15, 1,Direction.fromBlockFace(e.player.facing)!!)
                e.player.sendMessage("Box:{BaseLocation:${box.base},width:${box.width},weight:${box.weight},height:${box.height},blocksSize:${box.getBlocks().size}}")
            }
        }
    }
}