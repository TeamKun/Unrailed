package net.kunmc.lab.unrailed

import com.github.bun133.flylib2.commands.Commander
import com.github.bun133.flylib2.commands.CommanderBuilder
import com.github.bun133.flylib2.commands.TabChain
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
                        } else {
                            // Non Leader用のメニュー
                            debug("Not Leader")
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