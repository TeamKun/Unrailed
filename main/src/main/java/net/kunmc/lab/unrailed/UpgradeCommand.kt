package net.kunmc.lab.unrailed

import com.github.bun133.flylib2.commands.Commander
import com.github.bun133.flylib2.commands.CommanderBuilder
import com.github.bun133.flylib2.commands.TabChain
import net.kunmc.lab.unrailed.car.UpgradeableCar
import net.kunmc.lab.unrailed.game.player.GamePlayer
import net.kunmc.lab.unrailed.game.player.perk.Perks
import net.kunmc.lab.unrailed.gui.UpgradeGUI
import net.kunmc.lab.unrailed.gui.util.CarUpgradeable
import net.kunmc.lab.unrailed.gui.util.PerkUpgradeable
import net.kunmc.lab.unrailed.gui.util.Upgradable
import net.kunmc.lab.unrailed.util.asNotNull
import net.kunmc.lab.unrailed.util.debug
import net.kunmc.lab.unrailed.util.getGamePlayer
import net.kunmc.lab.unrailed.util.isLeader
import org.bukkit.ChatColor
import org.bukkit.entity.Player

val upgradeCommand: (Unrailed) -> Commander<Unrailed> = { unrailed ->
    Commander(
        unrailed, "Upgrade Command for Game", "/<command>",
        CommanderBuilder<Unrailed>()
            .addTabChain(TabChain())
            .setInvoker { unrailed, commandSender, strings ->
                // TODO Upgrade Command処理
                if (commandSender is Player) {
                    val gamePlayer = commandSender.getGamePlayer()
                    if (gamePlayer != null) {
                        val isLeader = commandSender.isLeader()
                        if (isLeader) {
                            // Leader用のメニュー
                            debug("Leader")
                            val gui = UpgradeGUI(gamePlayer, collectUpgradeable(true, gamePlayer), unrailed)
                            gui.open()
                        } else {
                            // Non Leader用のメニュー
                            debug("Not Leader")
                            val gui = UpgradeGUI(gamePlayer, collectUpgradeable(false, gamePlayer), unrailed)
                            gui.open()
                        }
                        return@setInvoker true
                    } else {
                        commandSender.sendMessage(
                            "" + ChatColor.RED + "You can't execute this command now"
                        )
                        return@setInvoker true
                    }
                } else {
                    commandSender.sendMessage("This command can be executed by player")
                    return@setInvoker true
                }
            }
    )
}

private fun collectUpgradeable(isLeader: Boolean, gamePlayer: GamePlayer): MutableList<Upgradable> {
    val list = mutableListOf<Upgradable>()
    if (isLeader) {
        val carUpgradeable =
            gamePlayer.getLane().asNotNull {
                it.train.asNotNull { t ->
                    t.car.filterIsInstance<UpgradeableCar>().map { c -> CarUpgradeable(c) }
                }
            }
        if (carUpgradeable != null) {
            list.addAll(carUpgradeable)
        }
    }
    val perkUpgradeable = Perks.values().map { PerkUpgradeable(it.f(gamePlayer)) }
    list.addAll(perkUpgradeable)

    return list
}