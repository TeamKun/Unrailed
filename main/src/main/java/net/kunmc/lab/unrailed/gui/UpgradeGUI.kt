package net.kunmc.lab.unrailed.gui

import com.github.bun133.flylib2.gui.ChestGUI
import com.github.bun133.flylib2.utils.ComponentUtils
import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.game.LaneInstance
import net.kunmc.lab.unrailed.game.player.GamePlayer
import net.kunmc.lab.unrailed.game.player.perk.Perks
import net.kunmc.lab.unrailed.gui.util.CarUpgradeable
import net.kunmc.lab.unrailed.gui.util.PerkUpgradeable
import net.kunmc.lab.unrailed.gui.util.Upgradable
import net.kunmc.lab.unrailed.util.containSameType

/**
 * アップグレードのGUI
 */
class UpgradeGUI(
    val player: GamePlayer,
    private var upgrades: List<Upgradable>,
    plugin: Unrailed
) {
    private val gui = ChestGUI(player.p, "", 4, plugin)

    init {
        update(player)
    }

    fun open() = gui.open(player.p)

    // Not Expected
    fun close() = player.p.closeInventory()

    fun update(player: GamePlayer) {
        gui.clear()
        upgrades
            .filter {
                if (it is PerkUpgradeable) {
                    return@filter !player.state.perks.map { p -> Perks.getFromInstance(p) }
                        .contains(Perks.getFromInstance(it.perk))
                } else {
                    return@filter true
                }
            }
            .forEachIndexed { index, upgradable ->
                val entry = gui.gen(upgradable.itemStack(player))
                entry.register.add { onBuy(upgradable, player) }
                gui.set((index + 1) % 10, (index + 1) / 10 + 1, entry)
            }
    }

    private fun getName(upgradable: Upgradable): String {
        return when (upgradable) {
            is CarUpgradeable -> "アップグレード:${upgradable.upgradeableCar.name()}"
            is PerkUpgradeable -> "Perk:${upgradable.perk.name()}"
            else -> TODO()
        }
    }

    fun onBuy(upgradable: Upgradable, gamePlayer: GamePlayer) {
        if (upgradable.isEnough(gamePlayer)) {
            upgradable.onBuy(gamePlayer)
            gamePlayer.p.sendMessage("${getName(upgradable)}を購入しました")
        } else {
            gamePlayer.p.sendMessage("${getName(upgradable)}を購入できませんでした")
        }
        update(gamePlayer)
    }
}