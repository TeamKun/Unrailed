package net.kunmc.lab.unrailed.car

import net.kunmc.lab.unrailed.util.CarInteractEvent
import org.bukkit.entity.Minecart
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerInteractEntityEvent

/**
 * ワゴン
 * @see AbstractTrain
 */
abstract class AbstractCar {
    /**
     * @return このワゴンの表示名
     */
    abstract fun name():String

    /**
     * @return Minecart
     */
    abstract fun getMinecart():Minecart

    /**
     * このトロッコに向かってInteractした時の動作
     * 冷却車など...
     * @note event will be VehicleEnterEvent(Right Click),VehicleDestroyEvent(Left Click),InventoryOpenEvent(Right Click).so you'll have to check that
     * @note also,all event is Cancelable(needed to be casted).
     */
    abstract fun onInteract(e: CarInteractEvent)
}