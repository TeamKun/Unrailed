package net.kunmc.lab.unrailed.hud

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.game.GameInstance
import net.kunmc.lab.unrailed.game.LaneInstance
import net.kunmc.lab.unrailed.game.phase.EndPhase
import net.kunmc.lab.unrailed.game.phase.GamePhase
import net.kunmc.lab.unrailed.game.phase.PreparePhase
import net.kunmc.lab.unrailed.station.Station
import org.bukkit.Location
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


    //////////////// EVENTS /////////////////
    // PREPARE PHASE START
    fun onStart(timeToStartMove: Long) {
        prepareHUD.onStart((unrailed.server.currentTick + timeToStartMove).toInt())
    }

    // PREPARE PHASE END
    // GAME PHASE START
    fun onStartMoving() {
        gameHUD.onStartMoving(gameInstance.lanes)
    }

    fun onArrive(timeToDepart: Long, lane: LaneInstance) {
        gameHUD.onArrive((timeToDepart + unrailed.server.currentTick).toInt(), lane)
    }

    fun onDepart(lane: LaneInstance, station: Station) {
        gameHUD.onDepart(lane, station)
    }

    fun onFail(lane: LaneInstance, failLocation: Location) {
        gameHUD.onFail(lane, failLocation)
    }

    fun onClear(lane: LaneInstance, clearLocation: Location) {
        gameHUD.onClear(lane, clearLocation)
    }

    // GAME PHASE END
    // END PHASE START
    fun onAllDone(sortedMap: Map<LaneInstance, Int?>) {
        endHUD.onAllDone(sortedMap)
    }
    // END PHASE END
    //////////////// EVENTS END /////////////////
}