package net.kunmc.lab.unrailed.listener

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.util.distinct
import net.kunmc.lab.unrailed.util.isJoinedGame
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryInteractEvent
import org.bukkit.inventory.PlayerInventory

class InventoryEventListener(unrailed: Unrailed) : ListenerBase(unrailed) {
    @EventHandler
    fun onPickUp(e: EntityPickupItemEvent) {
        if (e.entity is Player) {
            if ((e.entity as Player).isJoinedGame()) {
                checkInventory((e.entity as Player).inventory)
            }
        }
    }

    @EventHandler
    fun onTransferItems(e: InventoryInteractEvent) {
        if (e.whoClicked is Player) {
            if ((e.whoClicked as Player).isJoinedGame()) {
                checkInventory(e.whoClicked.inventory)
            }
        }
    }

    /**
     * プレイヤーに対してインベントリチェックを実施
     *
     * インベントリバー左から
     * ピッケル、斧、木か石かレール(1種類のみ許容)
     */
    private fun checkInventory(inventory: PlayerInventory) {
        println("checkInventory")
        inventory.distinct()
    }
}