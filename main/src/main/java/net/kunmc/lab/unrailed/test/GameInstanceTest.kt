package net.kunmc.lab.unrailed.test

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.game.GameInstance
import net.kunmc.lab.unrailed.game.LaneInstance
import net.kunmc.lab.unrailed.game.player.GamePlayer
import net.kunmc.lab.unrailed.generator.GenerateSetting
import net.kunmc.lab.unrailed.generator.StandardGenerator
import net.kunmc.lab.unrailed.util.Direction
import net.kunmc.lab.unrailed.util.registerEvents
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import kotlin.random.Random

class GameInstanceTest(unrailed: Unrailed) : TestCase(unrailed) {
    init {
        unrailed.registerEvents(this)
    }

    companion object {
        private var ins: GameInstanceTest? = null
        fun getInstance(unrailed: Unrailed): GameInstanceTest {
            if (ins != null) return ins!!
            ins = GameInstanceTest(unrailed)
            return ins!!
        }
    }

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        if (isGoingOn) {
            if (e.action == Action.RIGHT_CLICK_BLOCK) {
                val clickedBlock = e.clickedBlock!!
                val generateSetting = GenerateSetting(
                    clickedBlock.location,
                    Direction.fromBlockFace(e.player.facing)!!,
                    5,
                    false,
                    Material.BEDROCK,
                    9,
                    Random.nextLong(Long.MIN_VALUE, Long.MAX_VALUE),
                    10,
                    3
                )

                val gameInstance = GameInstance(unrailed)
                val laneInstance =
                    gameInstance.addLane(generateSetting, listOf(e.player).map { GamePlayer(it, gameInstance) })
                if (laneInstance == null) {
                    error("Fail to Generate LaneInstance")
                } else {
                    gameInstance.start()
                }
            }
        }
    }
}