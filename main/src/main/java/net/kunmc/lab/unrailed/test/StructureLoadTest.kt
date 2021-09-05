package net.kunmc.lab.unrailed.test

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.structure.StructureCollection
import net.kunmc.lab.unrailed.util.registerEvents
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class StructureLoadTest(unrailed: Unrailed) : TestCase(unrailed) {
    companion object {
        private var ins: StructureLoadTest? = null
        fun getInstance(unrailed: Unrailed): StructureLoadTest {
            if (ins != null) return ins!!
            ins = StructureLoadTest(unrailed)
            return ins!!
        }
    }

    init {
        unrailed.registerEvents(this)
    }

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        if (isGoingOn) {
            if (e.action == Action.RIGHT_CLICK_BLOCK) {
                val structure = StructureCollection.getInstance(unrailed).stations.SandStation
                structure.copyTo(e.clickedBlock!!.location)
            }
        }
    }
}