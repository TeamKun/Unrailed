package net.kunmc.lab.unrailed.listener

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.util.broadCast
import org.bukkit.event.EventHandler

class JumpEvent(unrailed: Unrailed) : ListenerBase(unrailed) {
    @EventHandler
    fun onJump(e: PlayerJumpEvent) {
//        broadCast("Jump!")
    }
}