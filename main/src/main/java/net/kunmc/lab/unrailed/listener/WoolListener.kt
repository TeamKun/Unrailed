package net.kunmc.lab.unrailed.listener

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.game.player.GamePlayer
import net.kunmc.lab.unrailed.util.WoolColor
import net.kunmc.lab.unrailed.util.isGoingOn
import net.kunmc.lab.unrailed.util.isWool
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.util.Vector

/**
 * 最初の地形にある羊毛の上に乗ったらそのチームに参加する奴
 */
class WoolListener(unrailed: Unrailed) : ListenerBase(unrailed) {
    @EventHandler
    fun onMove(e: PlayerMoveEvent) {
        if (unrailed.isGoingOn) {
            // TODO ゲーム開始以降は必要ないのでは
            val block = e.player.location.toBlockLocation().add(Vector(.0, -1.0, .0)).block
            if (block.isWool()) {
                val gamePlayer = GamePlayer.getFromPlayer(e.player)
                val BeforeLane = Unrailed.goingOnGame!!.getLane(e.player)
                val AfterLane = Unrailed.goingOnGame!!.getLane(WoolColor.fromMaterial(block.type)!!)

                if (gamePlayer == null) {
                    // ゲームに未登録
                    if (AfterLane != null) {
                        AfterLane.addTeamMember(GamePlayer(e.player, AfterLane.game))
                    }
                } else {
                    if (BeforeLane != null && AfterLane != null) {
                        if (AfterLane != BeforeLane) {
                            BeforeLane.removeTeamMember(gamePlayer)
                            AfterLane.addTeamMember(gamePlayer)
                        }
                    }
                }
            }
        }
    }
}