package net.kunmc.lab.unrailed.hud

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.game.LaneInstance
import net.kunmc.lab.unrailed.game.player.GamePlayer

class EndPhaseHUD : HUD {
    override fun update(unrailed: Unrailed, p: GamePlayer) {
    }

    ///////////// EVENT /////////////
    fun onAllDone(sortedMap: Map<LaneInstance, Int?>) {
    }
}
