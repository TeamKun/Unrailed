package net.kunmc.lab.unrailed.util

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.vehicle.VehicleDestroyEvent
import org.bukkit.event.vehicle.VehicleEnterEvent

/**
 * For AbstractCar#onInteract
 * @see net.kunmc.lab.unrailed.car.AbstractCar
 */
class CarInteractEvent private constructor(val e: Event) {

    companion object {
        fun generate(e: Event): CarInteractEvent {
            return if (e is VehicleEnterEvent || e is InventoryOpenEvent || e is VehicleDestroyEvent) CarInteractEvent(e)
            else throw Exception(
                "In init CarInteractEvent,Event type is not expected"
            )
        }
    }

    fun isRightClick() = e is VehicleEnterEvent || e is InventoryOpenEvent
    fun isLeftClick() = e is VehicleDestroyEvent

    fun getPlayer(): Player? {
        return when (e) {
            is VehicleEnterEvent -> {
                if (e.entered is Player) e.entered as Player
                else null
            }
            is InventoryOpenEvent -> e.player as Player
            is VehicleDestroyEvent -> {
                if (e.attacker != null && e.attacker is Player) return e.attacker as Player
                else null
            }
            else -> {
                error("in CarInteractEvent,Event is unexpected type:$e")
                null
            }
        }
    }
}