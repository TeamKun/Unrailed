package net.kunmc.lab.unrailed

import com.github.bun133.flylib2.commands.Commander
import com.github.bun133.flylib2.commands.CommanderBuilder
import com.github.bun133.flylib2.commands.TabChain
import com.github.bun133.flylib2.commands.TabObject
import net.kunmc.lab.unrailed.game.GameInstance
import net.kunmc.lab.unrailed.listener.EventAll
import net.kunmc.lab.unrailed.test.*
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Unrailed : JavaPlugin() {
    companion object {
        /**
         * The game going on
         */
        var goingOnGame: GameInstance? = null
    }

    var isGoingOn = false

    override fun onEnable() {
        // Plugin startup logic
        saveDefaultConfig()
        command(this).register("ur")
        upgradeCommand(this).register("ug")
        EventAll.getInstance(this)
        EventAll.init(this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}

fun command(unrailed: Unrailed) =
    // TODO Rewrite All
    Commander(
        unrailed, "Command of Unrailed Plugin", "/ur <start|stop>",
        CommanderBuilder<Unrailed>()
            .addFilter(CommanderBuilder.Filters.OP())
            .addTabChain(TabChain(TabObject("start")))
            .setInvoker { plugin, sender, _ ->
                if (sender is Player) {
                    UnrailedGameSettingGUI(sender, plugin).open()
                    return@setInvoker true
                }
                sender.sendMessage("Playerから開始してください")
                return@setInvoker false
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
                        "GameInstanceTest",
                        "RotateStructureTest",
                        "DegreeTest"
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
                    "RotateStructureTest" -> {
                        RotateStructureTest.getInstance(plugin)
                    }
                    "DegreeTest" -> {
                        DegreeTest.getInstance(plugin)
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