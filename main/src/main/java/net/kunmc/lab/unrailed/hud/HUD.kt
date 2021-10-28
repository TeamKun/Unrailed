package net.kunmc.lab.unrailed.hud

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.game.player.GamePlayer

/**
 * HUD class
 * provides Actionbar Content,ScoreBoard and Title every tick.
 */
interface HUD {
    fun update(unrailed: Unrailed, p: GamePlayer)
}