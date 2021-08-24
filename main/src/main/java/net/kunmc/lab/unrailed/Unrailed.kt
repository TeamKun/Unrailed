package net.kunmc.lab.unrailed

import com.github.bun133.flylib2.commands.Commander
import com.github.bun133.flylib2.commands.CommanderBuilder
import com.github.bun133.flylib2.commands.TabChain
import com.github.bun133.flylib2.commands.TabObject
import net.kunmc.lab.unrailed.test.RailRecognize
import net.kunmc.lab.unrailed.test.TrainCombineTest
import net.kunmc.lab.unrailed.test.TrainTest
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Unrailed : JavaPlugin() {
    var isGoingOn = false

    fun start(loc: Location) {
    }

    fun stop() {
    }

    companion object {
    }

    override fun onEnable() {
        // Plugin startup logic
        command(this).register("ur")
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
            .addTabChain(TabChain(TabObject("test"), TabObject("combine","RailRecognize","Train")))
            .setInvoker { plugin, sender, arr ->
                when (arr[1]) {
                    "combine" -> {
                        TrainCombineTest.getInstance(plugin).isGoingOn = !TrainCombineTest.getInstance(plugin).isGoingOn
                    }
                    "RailRecognize" -> {
                        RailRecognize.getInstance(plugin).isGoingOn = !RailRecognize.getInstance(plugin).isGoingOn
                    }
                    "Train" -> {
                        TrainTest.getInstance(plugin).isGoingOn = !TrainTest.getInstance(plugin).isGoingOn
                    }
                }
                return@setInvoker true
            }
    )