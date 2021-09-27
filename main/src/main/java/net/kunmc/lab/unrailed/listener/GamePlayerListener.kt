package net.kunmc.lab.unrailed.listener

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.game.phase.EndPhase
import net.kunmc.lab.unrailed.game.phase.GamePhase
import net.kunmc.lab.unrailed.util.isJoinedGame
import net.kunmc.lab.unrailed.util.isPhase
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerToggleSprintEvent

/**
 * GamePlayerのEventをListenするクラス
 */
class GamePlayerListener(unrailed: Unrailed) : ListenerBase(unrailed) {
    @EventHandler
    fun onJump(e: PlayerJumpEvent) {
        if (!e.player.isPhase(EndPhase()) && e.player.isJoinedGame()) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onSprint(e: PlayerToggleSprintEvent) {
        if (!e.player.isPhase(EndPhase()) && e.isSprinting && e.player.isJoinedGame()) {
            e.isCancelled = true
            e.player.isSprinting = false
        }
    }
}