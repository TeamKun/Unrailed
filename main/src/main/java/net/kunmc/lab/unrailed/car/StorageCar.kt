package net.kunmc.lab.unrailed.car

import net.kunmc.lab.unrailed.car.upgrade.UpGradeSetting
import net.kunmc.lab.unrailed.util.*
import org.bukkit.Location
import org.bukkit.entity.minecart.StorageMinecart
import org.bukkit.plugin.java.JavaPlugin
import kotlin.math.min

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

    /**
     * This value means that this storage stores <value> items each.
     */
    private var storageSize = baseStorageSize

    /**
     * The amount of woods this storageCar holding
     */
    var woodCount = 0

    /**
     * The amount of stones this storageCar holding
     */
    var stoneCount = 0

    override fun onUpgrade(t: Int) {
        storageSize = t
    }

    override fun onInteract(e: CarInteractEvent) {
        // TODO 持ち物を自動整理とかそこらへん(たぶん)
        val p = e.getPlayer() ?: return

        val wC =
            p.inventory.storageContents.filterNotNull().filter { it.type.isWood() }.count { it.amount.toLong() }.toInt()
        val sC = p.inventory.storageContents.filterNotNull().filter { it.type.isStone() }.count { it.amount.toLong() }
            .toInt()

        val wTC = min(storageSize - woodCount, wC)
        val sTC = min(storageSize - stoneCount, sC)

        if (p.inventory.remove({ it.type.isWood() }, wTC)) {
            // Able to Remove all Amount
            woodCount += wTC
        } else {
            if (wTC != 0) {
                // This Branch Will not be reached
                error("in StorageCar#onInteract not expected reaching happened: wTC:${wTC} wC:${wC}")
            }
        }

        if (p.inventory.remove({ it.type.isStone() }, sTC)) {
            // Able to Remove all Amount
            stoneCount += sTC
        } else {
            if (sTC != 0) {
                // This Branch Will not be reached
                error("in StorageCar#onInteract not expected reaching happened: sTC:${sTC} sC:${sC}")
            }
        }
        debug("StorageCar#onInteract woodCount:${woodCount} stoneCount:${stoneCount}")
    }
}

class StorageUpgradeSetting : UpGradeSetting<Int>(
    cost = { it },
    tFunction = { StorageCar.baseStorageSize + it * increaseItemSizePerLevel },
    description = { level, size ->
        listOf(
            "ストレージ容量を増加",
            "ストレージ容量:${StorageCar.baseStorageSize + level * increaseItemSizePerLevel}"
        )
    }
) {
    companion object {
        const val increaseItemSizePerLevel = 4
    }
}