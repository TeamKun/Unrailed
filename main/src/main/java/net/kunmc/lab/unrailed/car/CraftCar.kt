package net.kunmc.lab.unrailed.car

import net.kunmc.lab.unrailed.car.upgrade.UpGradeSetting
import net.kunmc.lab.unrailed.game.player.GamePlayer
import net.kunmc.lab.unrailed.listener.InventoryEventListener
import net.kunmc.lab.unrailed.train.Train
import net.kunmc.lab.unrailed.util.*
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Minecart
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.material.MaterialData
import org.bukkit.plugin.java.JavaPlugin
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

class CraftCar(plugin: JavaPlugin, spawnLocation: Location, Name: String = "作業車", val train: Train) :
    UpgradeableSettingCar<Int>(
        plugin,
        spawnLocation,
        Name,
        CraftUpgradeSetting(),
        carClass = Minecart::class.java,
        carInit = {
            it.setDisplayBlock(MaterialData(Material.CRAFTING_TABLE))
        }
    ) {

    companion object {
        // 1つのレールを制作するのにかかるTick数
        const val baseCraftTime: Int = 20 * 3

        /**
         * 計算のときに使う値
         * must be < 1
         * 1に近づければ近づけるほどクラフト時間が長くなる
         */
        const val decreaseRateCraftTime = 0.9

        /**
         * calculate craft time
         */
        internal fun calculateCraftTime(toUpgrade: Int): Int =
            ((baseCraftTime - 1) * (decreaseRateCraftTime.pow(toUpgrade.toDouble())) + 1).roundToInt()

        /**
         * calculate Upgrade Cost
         */
        internal fun calculateCost(toUpgrade: Int): Int = (2 * sqrt(toUpgrade.toDouble())).roundToInt()
    }

    init {
        plugin.server.scheduler.runTaskTimer(plugin, Runnable { tick() }, 1, 1)
    }

    /**
     * The amount of rails this craftCar holding
     */
    var railCounts = 0

    private var craftTime: Int = baseCraftTime
    override fun onUpgrade(t: Int) {
        craftTime = t
    }

    override fun onInteract(e: CarInteractEvent) {
        val p = e.getPlayer() ?: return

        if (p.inventory.storageContents.filterNotNull().size == 1 && p.inventory.storageContents.filterNotNull()
                .first().isMergeable(ItemStack(Material.RAIL), GamePlayer.storageSize)
        ) {
            // Can be merged
            val stack = p.inventory.storageContents.filterNotNull().first()
            stack.amount = stack.amount + 1
        } else if (p.inventory.storageContents.filterNotNull().isEmpty()) {
            p.inventory.setItem(InventoryEventListener.inventorySlotIndex, ItemStack(Material.RAIL))
        }
    }

    /**
     * Ticks while crafting
     * This value will be 0 when the rail is created
     */
    private var craftTicksCount = 0

    /**
     * To do something about crafting.........
     */
    private fun tick() {
        if (isEnoughItems()) {
            craftTicksCount++
            if (craftTicksCount >= craftTime) {
                // Complete Crafting
                remove()
                railCounts++
                craftTicksCount = 0
                debug("Complete Crafting")
            }
        } else {
            craftTicksCount = 0
        }
    }

    /**
     * トロッコに今レールを作るのに十分なアイテムがあるかどうか
     * @note return if the chest has one wood and stone each at least
     */
    private fun isEnoughItems(): Boolean {
        val storageCar = train.getComponents(StorageCar::class.java)
        if (storageCar.size == 1) {
            return storageCar[0].woodCount >= 1 && storageCar[0].stoneCount >= 1
        } else {
            error("in CraftCar#isEnoughItems storageCar is Duplicated")
        }
        return false
    }

    private fun remove(): Boolean {
        val storageCar = train.getComponents(StorageCar::class.java)
        if (storageCar.size == 1) {
            storageCar[0].woodCount--
            storageCar[0].stoneCount--
            return true
        } else {
            error("in CraftCar#remove storageCar is Duplicated")
        }
        return false
    }
}

class CraftUpgradeSetting : UpGradeSetting<Int>(
    description = { i, f -> listOf("クラフトスピード加速", "クラフト速度:${f}") },
    cost = { CraftCar.calculateCost(it) },
    tFunction = { CraftCar.calculateCraftTime(it) }
)