package net.kunmc.lab.unrailed

import com.github.bun133.flylib2.commands.Commander
import com.github.bun133.flylib2.commands.CommanderBuilder
import com.github.bun133.flylib2.commands.TabChain
import com.github.bun133.flylib2.commands.TabObject
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Unrailed : JavaPlugin() {
    var isGoingOn = false
        private set

    fun start(loc: Location) {
    }

    fun stop() {
    }

    companion object {


    }

    override fun onEnable() {
        // Plugin startup logic
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}

fun command(unrailed: Unrailed) =
    Commander(
        unrailed, "Command of Unraild Plugin", "/ur <start|stop>",
        CommanderBuilder<Unrailed>()
            .addFilter(CommanderBuilder.Filters.OP())
            .addTabChain(TabChain(TabObject("start")))
            .setInvoker { plugin, sender, _ ->
                if (sender is Player) {
                    plugin.start(sender.location)
                    sender.sendMessage("開始します")
                    return@setInvoker true
                }
                sender.sendMessage("Playerから開始してください")
                return@setInvoker false
            },
        CommanderBuilder<Unrailed>()
            .addFilter(CommanderBuilder.Filters.OP())
            .addTabChain(TabChain(TabObject("stop")))
            .setInvoker { plugin, sender, _ ->
                plugin.stop()
                sender.sendMessage("終了します")
                return@setInvoker true
            },
        CommanderBuilder<Unrailed>()
            .addFilter(CommanderBuilder.Filters.OP())
            .addTabChain(TabChain(TabObject("test")))
            .setInvoker { plugin, sender, arr ->
                return@setInvoker false
            }
    )