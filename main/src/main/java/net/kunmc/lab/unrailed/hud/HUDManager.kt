package net.kunmc.lab.unrailed.hud

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.game.GameInstance
import net.kunmc.lab.unrailed.game.phase.EndPhase
import net.kunmc.lab.unrailed.game.phase.GamePhase
import net.kunmc.lab.unrailed.game.phase.PreparePhase

/**
 * Manager of All HUD
 */
class HUDManager(val unrailed: Unrailed, val gameInstance: GameInstance) {
    /**
     * @note this function should be called when the game is starting
     */
    fun init() {
        unrailed.server.scheduler.runTaskTimer(unrailed, Runnable { tick() }, 1, 1)
    }

    private val prepareHUD = PreparePhaseHUD()
    private val gameHUD = GamePhaseHUD()
    private val endHUD = EndPhaseHUD()

    fun tick() {
        val hud: HUD = when (gameInstance.nowPhase) {
            is PreparePhase -> {
                prepareHUD
            }
            is GamePhase -> {
                gameHUD
            }
            is EndPhase -> {
                endHUD
            }
            else -> {
                TODO("In HudManager,Phase:${gameInstance.nowPhase} is not registered")
            }
        }

        gameInstance.players.forEach {
            hud.update(unrailed, it)
        }
    }
}