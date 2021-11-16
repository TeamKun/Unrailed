package net.kunmc.lab.unrailed

import com.github.bun133.flylib2.gui.ChestGUI
import com.github.bun133.flylib2.utils.EasyItemBuilder
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player

class UnrailedGameSettingGUI(val p: Player, val plugin: Unrailed) {
    private val gui = ChestGUI(p, name = "Unrailed", cols = 4, plugin)

    private fun generateGUI() {
        gui.gen(1, 1, EasyItemBuilder.genItem(Material.FLINT_AND_STEEL, name = "" + ChatColor.BOLD + "Start Game"))
            .also {
                it.register.add {
                    onStart()
                }
            }
    }

    private fun onStart() {
        p.closeInventory()
        val builder = GameInstanceBuilder(plugin)
        // TODO Build Game
        builder.generate().start()
    }

    fun open() {
        generateGUI()
        gui.open(p)
    }
}