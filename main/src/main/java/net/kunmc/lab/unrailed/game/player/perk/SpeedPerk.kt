package net.kunmc.lab.unrailed.game.player.perk

import net.kunmc.lab.unrailed.game.player.GamePlayer
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class SpeedPerk : Perk {
    override fun cost(): Int = 500
    override fun name(): String = "Speed"

    override fun description(gamePlayer: GamePlayer): List<String> = listOf("スピードアップ!")

    override fun onEffect(gamePlayer: GamePlayer) {
        if (!gamePlayer.p.hasPotionEffect(PotionEffectType.SPEED)) {
            gamePlayer.p.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 0))
        }
    }

    override fun onDeEffect(player: Player) {
        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.removePotionEffect(PotionEffectType.SPEED)
        }
    }
}