package net.kunmc.lab.unrailed.test

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.structure.BlockSet
import net.kunmc.lab.unrailed.util.Box
import net.kunmc.lab.unrailed.util.getConfigurationSectionOrDefault
import net.kunmc.lab.unrailed.util.registerEvents
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent

class WriteOutStructureTest(unrailed: Unrailed) : TestCase(unrailed) {
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
    fun onBreakBlock(e: BlockBreakEvent) {
        if (isGoingOn) {
            val clickedBlock = e.block
            if (startLocation == null) {
                startLocation = clickedBlock.location
            } else {
                if (startLocation!! == clickedBlock.location) return
                val box = Box(startLocation!!, clickedBlock.location)
                val blockSet = BlockSet(box)
                blockSet.toConfig(unrailed.config.getConfigurationSectionOrDefault("WriteOutStructureTest"))

                unrailed.saveConfig()

                println("Saved to Config#onBreakBlock")
                isGoingOn = false
            }

            e.isCancelled = true
        }
    }

    @EventHandler
    fun onPlaceBlock(e: BlockPlaceEvent) {
        if (isGoingOn) {
            val clickedBlock = e.block
            if (startLocation == null) {
                startLocation = clickedBlock.location
            } else {
                if (startLocation!! == clickedBlock.location) return
                val box = Box(startLocation!!, clickedBlock.location)
                val blockSet = BlockSet(box)

                blockSet.toConfig(unrailed.config.getConfigurationSectionOrDefault("WriteOutStructureTest"))

                unrailed.saveConfig()

                println("Saved to Config#onPlaceBlock")

                e.isCancelled = true
                isGoingOn = false
            }
        }
    }
}