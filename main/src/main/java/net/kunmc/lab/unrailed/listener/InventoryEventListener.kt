package net.kunmc.lab.unrailed.listener

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.util.debug
import net.kunmc.lab.unrailed.util.dropItem
import net.kunmc.lab.unrailed.util.isJoinedGame
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryInteractEvent
import org.bukkit.inventory.PlayerInventory

class InventoryEventListener(unrailed: Unrailed) : ListenerBase(unrailed) {
    companion object {
        /**
         * Index that the player's item will be stored at
         */
        const val inventorySlotIndex = 4
    }

    @EventHandler
    fun onPickUp(e: EntityPickupItemEvent) {
        if (e.entity is Player) {
            if ((e.entity as Player).isJoinedGame()) {
                checkInventory(e)
            }
        }
    }

    @EventHandler
    fun onTransferItems(e: InventoryInteractEvent) {
        if (e.whoClicked is Player) {
            if ((e.whoClicked as Player).isJoinedGame()) {
                checkInventory(e)
            }
        }
    }

    private fun checkInventory(e: EntityPickupItemEvent) {
        val count = (e.entity as Player).inventory.storageContents.filterNotNull().count()
        if (count >= 1) {
            e.isCancelled = true
        }
        checkInventory((e.entity as Player).inventory, e.entity as Player)
    }

    private fun checkInventory(e: InventoryInteractEvent) {
        checkInventory(e.whoClicked.inventory, e.whoClicked as Player)
    }


    /**
     * プレイヤーに対してインベントリチェックを実施
     */
    private fun checkInventory(inventory: PlayerInventory, p: Player) {
        val count = inventory.storageContents.filterNotNull().count()
        val first = inventory.storageContents.filterNotNull().firstOrNull() ?: return
        val firstIndex = inventory.storageContents.indexOfFirst { it != null && it.isSimilar(first) }

        // Drop Other Items
        if (count != 1) {
            inventory.storageContents.filterNotNull().filter { !it.isSimilar(first) }.forEach {
                // TODO 無限増殖バグがあるような気がする
                p.dropItem(it)
            }
        }

        if (firstIndex == inventorySlotIndex) {
            // The item is in correct slot.
        } else {
            // The item is not in correct slot.
            inventory.setItem(firstIndex, null)
            inventory.setItem(inventorySlotIndex, first)
        }
    }
}