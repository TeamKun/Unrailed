package net.kunmc.lab.unrailed.game.player

import net.kunmc.lab.unrailed.game.GameInstance
import net.kunmc.lab.unrailed.game.player.perk.Perk
import net.kunmc.lab.unrailed.util.registerEvents
import org.bukkit.entity.Player
import org.bukkit.event.Listener

/**
 * @see net.kunmc.lab.unrailed.game.GameInstance ごとに毎回生成されるゲームインスタンス用プレイヤー
 */
class GamePlayer(val p: Player, val game: GameInstance) : Listener {
    init {
        game.unrailed.registerEvents(this)
    }

    val perks = mutableListOf<Perk>()

    /**
     * Playerが木を切ったり石を掘ったりレールを敷いたりしたらもらえるポイント
     * Perk解放に使える
     */
    var points = 0

    // TODO Prevent Events (Run,Jump,Inventory etc.)
}