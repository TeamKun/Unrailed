package net.kunmc.lab.unrailed.car

import net.kunmc.lab.unrailed.car.upgrade.UpGradeSetting
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin

/**
 * Upgradeできるワゴン
 */
abstract class UpgradeableCar(plugin: JavaPlugin, spawnLocation: Location, Name: String) :
    BaseCar(plugin, spawnLocation, Name) {

    /**
     * 今このワゴンがアップグレードできるかどうか
     */
    abstract fun isUpgradeable(): Boolean

    /**
     * 次のこのワゴンのアップグレードのコスト(単位:ボルト)
     */
    abstract fun nextUpGradeCost(): Int

    /**
     * 次のこのワゴンのアップグレードの説明
     */
    abstract fun nextUpGradeDescription(): List<String>

    /**
     * アップグレード時
     */
    abstract fun onUpgrade()

    /**
     * 初期状態を1で今何段階目までアップグレードしたか
     */
    abstract fun getUpGradeLevel(): Int
}

abstract class UpgradeableSettingCar<T>(
    plugin: JavaPlugin,
    spawnLocation: Location,
    Name: String,
    val setting: UpGradeSetting<T>
) : UpgradeableCar(plugin, spawnLocation, Name) {

    override fun isUpgradeable(): Boolean {
        return setting.cost(getUpGradeLevel() + 1) != -1
    }

    override fun nextUpGradeCost(): Int {
        return setting.cost(getUpGradeLevel() + 1)
    }

    override fun nextUpGradeDescription(): List<String> {
        return setting.description(setting.cost(getUpGradeLevel() + 1), setting.tFunction(getUpGradeLevel() + 1))
    }

    override fun onUpgrade() {
        onUpgrade(setting.tFunction(getUpGradeLevel() + 1))
        nowLevel++
    }

    abstract fun onUpgrade(t: T)

    var nowLevel = 1

    override fun getUpGradeLevel(): Int {
        return nowLevel
    }
}