package net.kunmc.lab.unrailed.test

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.util.Box
import net.kunmc.lab.unrailed.util.Direction
import net.kunmc.lab.unrailed.util.fill
import net.kunmc.lab.unrailed.util.registerEvents
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class BoxFillTest(unrailed: Unrailed) : TestCase(unrailed) {
    companion object {
        private var ins: BoxFillTest? = null
        fun getInstance(unrailed: Unrailed): BoxFillTest {
            if (ins == null) ins = BoxFillTest(unrailed)
            return ins!!
        }
    }

    var selecting: Location? = null

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        if (isGoingOn) {
            if (e.action == Action.RIGHT_CLICK_BLOCK) {
                val clickedBlock = e.clickedBlock!!
                val box = Box(clickedBlock.location, 5, 15, 10, Direction.fromBlockFace(e.player.facing)!!)
                box.fill(Material.STONE)
                e.player.sendMessage("Box:{BaseLocation:${box.base},width:${box.width},weight:${box.weight},height:${box.height},blocksSize:${box.getBlocks().size}}")
            }
        }
    }
}