package net.kunmc.lab.unrailed.hud

import com.github.bun133.flylib2.utils.ComponentUtils
import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.game.player.GamePlayer

class PreparePhaseHUD : HUD {
    private var departTick: Int = -1

    override fun update(unrailed: Unrailed, p: GamePlayer) {
//        p.p.sendActionBar(ComponentUtils.fromText("PreparePhaseHUD"))
        if (departTick != -1 && departTick - unrailed.server.currentTick >= 0) {
            p.p.sendActionBar(ComponentUtils.fromText("timeToDepart:${departTick - unrailed.server.currentTick}"))
        }
    }

    ////////////// EVENT /////////////
    fun onStart(departTick: Int) {
        this.departTick = departTick
    }
}