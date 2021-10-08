package net.kunmc.lab.unrailed.game.player.perk

import net.kunmc.lab.unrailed.game.player.GamePlayer
import org.bukkit.entity.Player

/**
 * @see net.kunmc.lab.unrailed.game.player.GamePlayer に適用できるパーク(何にしよう...)
 */
interface Perk {
    /**
     * @return GamePlayerがこのPerkを取得するのに何Point必要か
     */
    fun cost(): Int

    /**
     * @return name of Perk
     */
    fun name(): String

    /**
     * @return description of Perk
     */
    fun description(gamePlayer: GamePlayer): List<String>

    /**
     * gamePlayerに効果を適用
     */
    fun onEffect(gamePlayer: GamePlayer)

    /**
     * playerから効果をはく奪
     */
    fun onDeEffect(player: Player)
}