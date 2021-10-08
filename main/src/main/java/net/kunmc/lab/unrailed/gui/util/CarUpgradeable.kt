package net.kunmc.lab.unrailed.gui.util

import com.github.bun133.flylib2.utils.EasyItemBuilder
import net.kunmc.lab.unrailed.car.EngineCar
import net.kunmc.lab.unrailed.car.StorageCar
import net.kunmc.lab.unrailed.car.UpgradeableCar
import net.kunmc.lab.unrailed.game.player.GamePlayer
import net.kunmc.lab.unrailed.util.asNotNull
import net.kunmc.lab.unrailed.util.nullMap
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * Upgradeable for Cars
 */
class CarUpgradeable(val upgradeableCar: UpgradeableCar) : Upgradable {
    override fun itemStack(gamePlayer: GamePlayer): ItemStack {
        // 表示するMaterial
        val stackMaterial = when (upgradeableCar) {
            is EngineCar -> {
                Material.FURNACE
            }
            is StorageCar -> {
                Material.CHEST
            }
            else -> TODO()
        }

        return EasyItemBuilder.genItem(
            stackMaterial,
            1,
            "アップグレード:${upgradeableCar.name()}",
            upgradeableCar.nextUpGradeDescription().toMutableList()
                .also {
                    it.add("")
                    it.add("価格:${upgradeableCar.nextUpGradeCost()}キー")
                }
        )
    }

    override fun isEnough(gamePlayer: GamePlayer): Boolean {
        return gamePlayer.getLane().asNotNull { it.state.keys >= upgradeableCar.nextUpGradeCost() }.nullMap { false }
    }

    override fun onBuy(gamePlayer: GamePlayer) {
        gamePlayer.getLane().asNotNull { it.state.keys -= upgradeableCar.nextUpGradeCost() }
        upgradeableCar.onUpgrade()
    }
}