package net.kunmc.lab.unrailed.game.player.perk

import net.kunmc.lab.unrailed.game.player.GamePlayer

/**
 * For list up perks
 */
enum class Perks(val f: (GamePlayer) -> Perk) {
    Speed({ net.kunmc.lab.unrailed.game.player.perk.SpeedPerk() });

    companion object {
        inline fun <reified T : Perk> getFromInstance(gamePlayer: GamePlayer): Perks? {
            return values().firstOrNull { it.f(gamePlayer) is T }
        }

        fun <T : Perk> getFromInstance(t: T): Perks {
            return when (t) {
                is SpeedPerk -> Speed
                else -> TODO()
            }
        }
    }
}