package net.kunmc.lab.unrailed.test

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.game.GameInstance
import net.kunmc.lab.unrailed.game.GameSetting
import net.kunmc.lab.unrailed.game.LaneInstance
import net.kunmc.lab.unrailed.game.player.GamePlayer
import net.kunmc.lab.unrailed.generator.GenerateSetting
import net.kunmc.lab.unrailed.generator.StandardGenerator
import net.kunmc.lab.unrailed.util.*
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import kotlin.error
import kotlin.random.Random

class GameInstanceTest(unrailed: Unrailed) : TestCase(unrailed) {
    companion object {
        private var ins: GameInstanceTest? = null
        fun getInstance(unrailed: Unrailed): GameInstanceTest {
            if (ins != null) return ins!!
            ins = GameInstanceTest(unrailed)
            return ins!!
        }
    }

    var timer = Timer(unrailed)

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        if (isGoingOn) {
            if (e.action == Action.RIGHT_CLICK_BLOCK && timer.isUp()) {
                val clickedBlock = e.clickedBlock!!

                val gameInstance = if (Unrailed.goingOnGame != null) {
                    Unrailed.goingOnGame!!
                } else {
                    GameInstance(unrailed, GameSetting())
                }

                val generateSetting = GenerateSetting(
                    clickedBlock.location,
                    Direction.fromBlockFace(e.player.facing)!!,
                    5,
                    false,
                    Material.BEDROCK,
                    9,
                    Random.nextLong(Long.MIN_VALUE, Long.MAX_VALUE),
                    3,
                    3,
                    // TODO 色がかぶる
                    WoolColor.namedTextColor().random()
                )

                val laneInstance = gameInstance.addLane(generateSetting, listOf(e.player))

                unrailed.isGoingOn = true
                if (laneInstance == null) {
                    error("Fail to Generate LaneInstance")
                } else {
                    gameInstance.start()
                }

                timer.time = 20
            }
        }
    }
}