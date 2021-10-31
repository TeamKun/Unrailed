package net.kunmc.lab.unrailed.hud

import com.github.bun133.flylib2.utils.ComponentUtils
import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.game.player.GamePlayer

class EndPhaseHUD : HUD {
    override fun update(unrailed: Unrailed, p: GamePlayer) {
        // TODO
        p.p.sendActionBar(ComponentUtils.fromText("EndPhaseHUD") )
    }

    ///////////// EVENT /////////////
    fun onAllDone() {
    }
}
