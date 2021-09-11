package net.kunmc.lab.unrailed.listener

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.util.isGoingOn
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerToggleSprintEvent

/**
 * GamePlayerのEventをListenするクラス
 */
class GamePlayerListener(unrailed: Unrailed) : ListenerBase(unrailed) {
    @EventHandler
    fun onJump(e: PlayerJumpEvent) {
        if (e.player.isGoingOn()) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onSprint(e: PlayerToggleSprintEvent) {
        if (e.player.isGoingOn() && e.isSprinting) {
            e.isCancelled = true
        }
    }
}