package net.kunmc.lab.unrailed.gui.util

import com.github.bun133.flylib2.utils.EasyItemBuilder
import net.kunmc.lab.unrailed.game.player.GamePlayer
import net.kunmc.lab.unrailed.game.player.perk.Perk
import net.kunmc.lab.unrailed.game.player.perk.SpeedPerk
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class PerkUpgradeable(val perk: Perk) : Upgradable {
    override fun itemStack(gamePlayer: GamePlayer): ItemStack {
        val stackMaterial = when (perk) {
            is SpeedPerk -> Material.CHAINMAIL_BOOTS
            else -> TODO()
        }

        return EasyItemBuilder.genItem(
            stackMaterial,
            1,
            "Perk:${perk.name()}",
            perk.description(gamePlayer).toMutableList().also {
                it.add("")
                it.add("価格:${perk.cost()}ポイント")
            })
    }

    override fun isEnough(gamePlayer: GamePlayer): Boolean = gamePlayer.state.points >= perk.cost()

    override fun onBuy(gamePlayer: GamePlayer) {
        gamePlayer.state.perks.add(perk)
        gamePlayer.state.points -= perk.cost()
        perk.onEffect(gamePlayer)
    }
}