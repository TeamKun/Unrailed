package net.kunmc.lab.unrailed.hud

import com.github.bun133.flylib2.utils.ComponentUtils
import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.game.LaneInstance
import net.kunmc.lab.unrailed.game.player.GamePlayer
import net.kunmc.lab.unrailed.util.asNotNull
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective

class GamePhaseHUD : HUD {
    override fun update(unrailed: Unrailed, p: GamePlayer) {
        // TODO
        p.p.sendActionBar(ComponentUtils.fromText("GamePhaseHUD"))
        val lane = p.game.getLane(p.p) ?: return
        val objective1 = p.p.scoreboard.getObjective("Ur-GUI-Objective")
        val objective = if (objective1 != null) objective1 else {
            net.kunmc.lab.unrailed.util.error("in GamePhaseHUD#update objective1 is null. This is a bug.")
            return
        }

        update(objective, p, lane)
    }

    private fun update(objective: Objective, p: GamePlayer, lane: LaneInstance) {
        objective.displaySlot = DisplaySlot.SIDEBAR
        val points = objective.getScore("Points:")
        points.score = p.state.points
        val keys = objective.getScore("Keys:")
        keys.score = lane.state.keys
    }
}
