package net.kunmc.lab.unrailed.listener

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.car.AbstractCar
import net.kunmc.lab.unrailed.car.EngineCar
import net.kunmc.lab.unrailed.car.StorageCar
import net.kunmc.lab.unrailed.game.phase.EndPhase
import net.kunmc.lab.unrailed.train.Train
import net.kunmc.lab.unrailed.util.CarInteractEvent
import net.kunmc.lab.unrailed.util.asNotNull
import net.kunmc.lab.unrailed.util.debug
import net.kunmc.lab.unrailed.util.isRail
import org.bukkit.entity.Minecart
import org.bukkit.entity.minecart.StorageMinecart
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
        // どうやら俺らが思うEntityに当たっても発火しない
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
        // どうやら俺らが思うEntityに当たって発火する
        if (e.vehicle is Minecart && !e.block.isRail()) {
            debug("onCollideWithBlock Type:${e.block.type}")
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
        if (Unrailed.goingOnGame.asNotNull { it.nowPhase is EndPhase } == true) {
            return
        }
        if (e.vehicle is Minecart) {
            val car = getCar(e.vehicle as Minecart)
            if (car != null) {
                e.isCancelled = true
                // Handle Event
                car.onInteract(CarInteractEvent.generate(e))
            }
        }
    }

    @EventHandler
    fun onEnter(e: VehicleEnterEvent) {
        if (e.vehicle is Minecart) {
            val car = getCar(e.vehicle as Minecart)
            if (car != null) {
                e.isCancelled = true
                // Handle Event
                car.onInteract(CarInteractEvent.generate(e))
            }
        }
    }

    @EventHandler
    fun onOpenInventory(e: InventoryOpenEvent) {
        val all = getAllCar { it is StorageCar }
        if (all != null) {
            // Handle
            all.firstOrNull { (it.getMinecart() as StorageMinecart).inventory == e.inventory }
                ?.onInteract(CarInteractEvent.generate(e))
                ?.also { e.isCancelled = true }
        }
    }

    private fun getTrain(minecart: Minecart): Train? {
        return Unrailed.goingOnGame.asNotNull {
            it.lanes
                .mapNotNull { l -> l.train }
                .filter { t -> t.getCars().map { tt -> tt.getMinecart() }.contains(minecart) }
                .getOrNull(0)
        }
    }

    private fun getCar(minecart: Minecart): AbstractCar? {
        return Unrailed.goingOnGame.asNotNull { gameInstance ->
            gameInstance.lanes
                .mapNotNull { l -> l.train }
                .map { t -> t.getCars() }.flatten()
                .firstOrNull { it.getMinecart() == minecart }
        }
    }

    private fun getAllCar(filter: (AbstractCar) -> Boolean = { true }): List<AbstractCar>? {
        return Unrailed.goingOnGame.asNotNull {
            return@asNotNull it.lanes.mapNotNull { l -> l.train }.map { l -> l.car }.flatten().filter(filter)
        }
    }
}