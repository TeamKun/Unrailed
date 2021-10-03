package net.kunmc.lab.unrailed.car

import net.kunmc.lab.unrailed.car.upgrade.UpGradeSetting
import net.kunmc.lab.unrailed.util.CarInteractEvent
import org.bukkit.Location
import org.bukkit.entity.minecart.StorageMinecart
import org.bukkit.plugin.java.JavaPlugin

class StorageCar(
    plugin: JavaPlugin,
    spawnLocation: Location,
    Name: String = "貯蔵車",
    setting: UpGradeSetting<Int> = StorageUpgradeSetting()
) :
    UpgradeableSettingCar<Int>(plugin, spawnLocation, Name, setting, carClass = StorageMinecart::class.java) {

    companion object {
        const val baseStorageSize = 5
    }

    var storageSize = baseStorageSize

    override fun onUpgrade(t: Int) {
        storageSize = t
    }

    override fun onInteract(e: CarInteractEvent) {
        // TODO 持ち物を自動整理とかそこらへん(たぶん)
    }
}

class StorageUpgradeSetting : UpGradeSetting<Int>(
    cost = { it },
    tFunction = { StorageCar.baseStorageSize + it * 2 },
    description = { level, size -> listOf("ストレージ容量を増加", "ストレージ容量:${StorageCar.baseStorageSize + level * 2}") }
) {}