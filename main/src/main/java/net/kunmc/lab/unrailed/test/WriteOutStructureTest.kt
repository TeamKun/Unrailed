package net.kunmc.lab.unrailed.test

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.structure.BlockSet
import net.kunmc.lab.unrailed.util.Box
import net.kunmc.lab.unrailed.util.getConfigurationSectionOrDefault
import net.kunmc.lab.unrailed.util.registerEvents
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class WriteOutStructureTest(unrailed: Unrailed) : TestCase(unrailed) {
    init {
        unrailed.registerEvents(this)
    }

    companion object {
        private var ins: WriteOutStructureTest? = null
        fun getInstance(unrailed: Unrailed): WriteOutStructureTest {
            if (ins != null) return ins!!
            ins = WriteOutStructureTest(unrailed)
            return ins!!
        }
    }

    var startLocation: Location? = null

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        if (isGoingOn) {
            if (e.action == Action.RIGHT_CLICK_BLOCK) {
                val clickedBlock = e.clickedBlock!!
                if (startLocation == null) {
                    startLocation = clickedBlock.location
                } else {
                    if (startLocation!! == clickedBlock.location) return
                    val box = Box(startLocation!!, clickedBlock.location)
                    val blockSet = BlockSet(box)
                    blockSet.toConfig(unrailed.config.getConfigurationSectionOrDefault("WriteOutStructureTest"))

                    unrailed.saveConfig()

                    println("Saved to Config")

                    isGoingOn = false
                }
            }
        }
    }
}