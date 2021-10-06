package net.kunmc.lab.unrailed.game.player

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.game.GameInstance
import net.kunmc.lab.unrailed.game.LaneInstance
import net.kunmc.lab.unrailed.game.player.perk.Perk
import net.kunmc.lab.unrailed.listener.ListenerBase
import net.kunmc.lab.unrailed.util.registerEvents
import org.bukkit.entity.Player
import org.bukkit.event.Listener

/**
 * @see net.kunmc.lab.unrailed.game.GameInstance ごとに毎回生成されるゲームインスタンス用プレイヤー
 */
class GamePlayer(val p: Player, val game: GameInstance) : ListenerBase(game.unrailed) {
    companion object {
        fun getFromPlayer(p: Player): GamePlayer? {
            return Unrailed.goingOnGame?.players?.filter { it.p == p }?.getOrNull(0)
        }

        fun getOrRegister(p: Player, g: GameInstance): GamePlayer {
            val gg = getFromPlayer(p)
            if (gg != null) {
                return gg
            } else {
                return GamePlayer(p, g)
            }
        }
    }

    init {
        game.players.add(this)
    }

    val state = GamePlayerState(this)

    fun getLane(): LaneInstance? {
        return Unrailed.goingOnGame?.getLane(this.p)
    }
}