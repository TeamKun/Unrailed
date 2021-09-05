package net.kunmc.lab.unrailed

import com.github.bun133.flylib2.commands.Commander
import com.github.bun133.flylib2.commands.CommanderBuilder
import com.github.bun133.flylib2.commands.TabChain
import com.github.bun133.flylib2.commands.TabObject
import net.kunmc.lab.unrailed.test.*
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
        saveDefaultConfig()
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
            .addTabChain(
                TabChain(
                    TabObject("test"),
                    TabObject(
                        "combine",
                        "RailRecognize",
                        "Train",
                        "MinecartDelete",
                        "RailShape",
                        "RailConnecting",
                        "EdgeTest",
                        "BoxTest",
                        "BoxFillTest",
                        "GeneratorTest",
                        "WriteOutStructureTest",
                        "StructureLoadTest",
                        "GameInstanceTest"
                    )
                )
            )
            .setInvoker { plugin, sender, arr ->
                val testCase = when (arr[1]) {
                    "combine" -> {
                        TrainCombineTest.getInstance(plugin)
                    }
                    "RailRecognize" -> {
                        RailRecognize.getInstance(plugin)
                    }
                    "Train" -> {
                        TrainTest.getInstance(plugin)
                    }
                    "MinecartDelete" -> {
                        MinecartDelete.getInstance(plugin)
                    }
                    "RailShape" -> {
                        RailShapeTest.getInstance(plugin)
                    }
                    "RailConnecting" -> {
                        RailConnectingTest.getInstance(plugin)
                    }
                    "EdgeTest" -> {
                        EdgeTest.getInstance(plugin)
                    }
                    "BoxTest" -> {
                        BoxTest.getInstance(plugin)
                    }
                    "BoxFillTest" -> {
                        BoxFillTest.getInstance(plugin)
                    }
                    "GeneratorTest" -> {
                        GeneratorTest.getInstance(plugin)
                    }
                    "WriteOutStructureTest" -> {
                        WriteOutStructureTest.getInstance(plugin)
                    }
                    "StructureLoadTest" -> {
                        StructureLoadTest.getInstance(plugin)
                    }
                    "GameInstanceTest" -> {
                        GameInstanceTest.getInstance(plugin)
                    }
                    else -> null
                }

                if (testCase == null) {
                    return@setInvoker false
                } else {
                    testCase.isGoingOn = !testCase.isGoingOn
                }
                return@setInvoker true
            }
    )