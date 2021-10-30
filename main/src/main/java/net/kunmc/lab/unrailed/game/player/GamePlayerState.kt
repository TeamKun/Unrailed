package net.kunmc.lab.unrailed.game.player

import net.kunmc.lab.unrailed.game.player.perk.Perk

class GamePlayerState(val player: GamePlayer) {
    /**
     * プレイヤーが所持しているPoint
     */
    var points = 0

    /**
     * プレイヤーが所持しているPerk
     */
    val perks = mutableListOf<Perk>()

    /**
     * The amount of stacks that the player can store
     */
    var storageStackAmount = 5
}