package net.kunmc.lab.unrailed.car

import net.kunmc.lab.unrailed.car.upgrade.UpGradeSetting
import org.bukkit.Location
import org.bukkit.entity.Minecart
import org.bukkit.plugin.java.JavaPlugin
import kotlin.math.max

class EngineCar(
    plugin: JavaPlugin,
    spawnLocation: Location,
    Name: String = "機関車",
    val engineSetting: EngineCarSetting = EngineCarSetting()
) :
    UpgradeableSettingCar<Double>(
        plugin, spawnLocation,
        Name,
        engineSetting,
        carClass = Minecart::class.java
    ) {
    companion object {
        /**
         * EngineCarの初期スピード
         */
        const val BaseSpeed = 0.150

        /**
         * 最低スピード
         */
        const val minSpeed = 0.05
    }

    var speed: Double = BaseSpeed

    override fun onUpgrade(t: Double) {
        speed = engineSetting.tFunction(getUpGradeLevel() + 1)
    }
}

class EngineCarSetting :
    UpGradeSetting<Double>(
        cost = { it * 2 },
        tFunction = { max(EngineCar.BaseSpeed - it * 0.05, EngineCar.minSpeed) },
        description = { _, speed -> listOf("進行速度が遅くなる", "エンジンが壊れていたかも。", "速度が${speed}になる") }
    )