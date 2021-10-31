package net.kunmc.lab.unrailed.hud

import com.github.bun133.flylib2.utils.ComponentUtils
import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.game.LaneInstance
import net.kunmc.lab.unrailed.game.player.GamePlayer
import net.kunmc.lab.unrailed.station.Station
import net.kunmc.lab.unrailed.util.debug
import net.kunmc.lab.unrailed.util.getOrRegisterObjective
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective

class GamePhaseHUD : HUD {
    companion object {
        const val failMagicNum = -1
        const val clearMagicNum = -2
    }

    /**
     * Stores Data about State of Train
     *
     *  when value:
     *  (not Registered) -> This Train hasn't started moving yet
     *  0 -> This train is Moving
     *  (Int Value) -> This value means departTime.
     *  -1(failMagicNum) -> This Lane is failed
     *  -2(clearMagicNum) -> This Lane is Cleared
     */
    private val departTick = mutableMapOf<LaneInstance, Int>()

    override fun update(unrailed: Unrailed, p: GamePlayer) {
        // TODO たぶん2人以上でやると大変なことになる
//        p.p.sendActionBar(ComponentUtils.fromText("GamePhaseHUD"))
        val lane = p.game.getLane(p.p) ?: return
        val objective = p.p.scoreboard.getOrRegisterObjective(
            "Ur-GUI-Objective",
            "dummy",
            ComponentUtils.fromText("GAME PHASE: MAIN ")
        )
        update(objective, p, lane, unrailed)
    }

    private fun update(objective: Objective, p: GamePlayer, lane: LaneInstance, unrailed: Unrailed) {
        objective.displaySlot = DisplaySlot.SIDEBAR
        val points = objective.getScore("Points:")
        points.score = p.state.points
        val keys = objective.getScore("Keys:")
        keys.score = lane.state.keys

        val i = this.departTick.getOrDefault(lane, -3)
        when (i) {
            -3 -> {
                // Not Found in DepartTick Map
                // This Train hasn't started moving yet
                // This element is called while Prepare Phase,so this element can be ignored
                error("GamePhaseHUD:WARN Called while Prepare Phase")
            }

            failMagicNum -> {
                // This Lane is failed
                p.p.sendActionBar(ComponentUtils.fromText("STATE:${ChatColor.RED}FAILED"))
            }

            clearMagicNum -> {
                // This Lane is Cleared
                p.p.sendActionBar(ComponentUtils.fromText("STATE:${ChatColor.GREEN}CLEARED"))
            }

            0 -> {
                // This train is Moving
                p.p.sendActionBar(ComponentUtils.fromText("STATE:${ChatColor.BLUE}MOVING"))
            }

            else -> {
                // This value means departTime.
                val timeToDepartScore = objective.getScore("timeToDepart")
                if (i != -1 && i - unrailed.server.currentTick >= 0) {
                    timeToDepartScore.score = i - unrailed.server.currentTick
                }
            }
        }
    }


    /////////// EVENT /////////////
    fun onStartMoving(lanes: MutableList<LaneInstance>) {
        lanes.forEach {
            this.departTick[it] = 0
        }
    }

    fun onArrive(departTick: Int, lane: LaneInstance) {
        this.departTick[lane] = departTick
    }

    fun onDepart(lane: LaneInstance, station: Station) {
        this.departTick[lane] = 0
    }

    fun onFail(lane: LaneInstance, failLocation: Location) {
        this.departTick[lane] = failMagicNum
    }

    fun onClear(lane: LaneInstance, clearLocation: Location) {
        this.departTick[lane] = clearMagicNum
    }
}
