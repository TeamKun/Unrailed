package net.kunmc.lab.unrailed.listener

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.car.EngineCar
import net.kunmc.lab.unrailed.game.GameInstance
import net.kunmc.lab.unrailed.train.Train
import net.kunmc.lab.unrailed.util.asNotNull
import net.kunmc.lab.unrailed.util.debug
import net.kunmc.lab.unrailed.util.isRail
import net.kunmc.lab.unrailed.util.log
import org.bukkit.entity.Minecart
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent
import org.bukkit.event.vehicle.VehicleDestroyEvent
import org.bukkit.event.vehicle.VehicleEnterEvent
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent

/**
 * すべてのトロッコに関して色々する
 */
class MinecartListener(unrailed: Unrailed) : ListenerBase(unrailed) {
    @EventHandler
    fun onCollideWithEntity(e: VehicleEntityCollisionEvent) {
        if (e.vehicle is Minecart) {
            val train = getTrain(e.vehicle as Minecart)
            if (train != null) {
                e.isCollisionCancelled = true
                e.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onCollideWithBlock(e: VehicleBlockCollisionEvent) {
        if (e.vehicle is Minecart && !e.block.isRail()) {
            debug("Type:${e.block.type}")
            val train = getTrain(e.vehicle as Minecart)
            if (train != null) {
                train.state().isDropped = true
                e.vehicle.world.createExplosion(e.vehicle.location, 3.0f, false, false)

                if (train.car.any { it.getMinecart() == e.vehicle && it is EngineCar }) {
                    // 先頭車のみ判定に必要
                    train.getLane()?.onFail(e.vehicle.location)
                }

                e.vehicle.remove()
            }
        }
    }

    @EventHandler
    fun onBreak(e: VehicleDestroyEvent) {
        if (e.vehicle is Minecart) {
            val train = getTrain(e.vehicle as Minecart)
            if (train != null) {
                e.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onEnter(e: VehicleEnterEvent) {
        if (e.vehicle is Minecart) {
            val train = getTrain(e.vehicle as Minecart)
            if (train != null) {
                e.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onOpenInventory(e: InventoryOpenEvent) {
        // TODO Listen and Handle
    }

    private fun getTrain(minecart: Minecart): Train? {
        return Unrailed.goingOnGame.asNotNull {
            it.lanes
                .mapNotNull { l -> l.train }
                .filter { t -> t.getCars().map { tt -> tt.getMinecart() }.contains(minecart) }
                .getOrNull(0)
        }
    }
}