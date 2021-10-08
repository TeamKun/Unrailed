package net.kunmc.lab.unrailed.game.player.perk

import net.kunmc.lab.unrailed.game.player.GamePlayer

/**
 * For list up perks
 */
enum class Perks(val f: (GamePlayer) -> Perk) {
    Speed({net.kunmc.lab.unrailed.game.player.perk.SpeedPerk()})
}