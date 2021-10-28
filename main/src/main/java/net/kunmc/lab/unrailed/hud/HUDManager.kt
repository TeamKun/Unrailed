package net.kunmc.lab.unrailed.hud

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.game.GameInstance
import net.kunmc.lab.unrailed.game.phase.EndPhase
import net.kunmc.lab.unrailed.game.phase.GamePhase
import net.kunmc.lab.unrailed.game.phase.PreparePhase
import org.bukkit.scheduler.BukkitTask

/**
 * Manager of All HUD
 */
class HUDManager(val unrailed: Unrailed, val gameInstance: GameInstance) {
    private var task: BukkitTask? = null

    /**
     * @note this function should be called when the game is starting
     */
    fun init() {
        task = unrailed.server.scheduler.runTaskTimer(unrailed, Runnable { tick() }, 1, 1)
    }

    /**
     * @note this function should be called when the game is regenerating
     */
    fun dispose() {
        if (task != null) {
            task!!.cancel()
        }
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