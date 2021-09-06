package net.kunmc.lab.unrailed.train

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.car.AbstractCar
import net.kunmc.lab.unrailed.car.EngineCar
import net.kunmc.lab.unrailed.rail.Rail
import net.kunmc.lab.unrailed.train.state.TrainState
import net.kunmc.lab.unrailed.util.copy
import net.kunmc.lab.unrailed.util.isOnRail
import net.kunmc.lab.unrailed.util.scale
import net.kunmc.lab.unrailed.util.toVector
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.vehicle.VehicleCollisionEvent
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent
import org.bukkit.util.Vector
import java.lang.IllegalArgumentException

class Train(firstCar: EngineCar, val rail: Rail, private val plugin: Unrailed) : AbstractTrain(), Listener {
    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    val car = mutableListOf<AbstractCar>()
    override fun getLength(): Int = car.size
    override fun getCars(): MutableList<AbstractCar> = car.toMutableList()
    override fun addCar(car: AbstractCar) {
        this.car.add(car)
    }

    private val trainState = TrainState(this)
    override fun state(): TrainState = trainState

    var isMoving = false

    override fun getRunningRail(): Rail = rail

    init {
        addCar(firstCar)
        plugin.server.scheduler.runTaskTimer(plugin, Runnable { onUpdate() }, 1, 1)
    }

    private fun onUpdate() {
        if (!plugin.isGoingOn) return
        //TODO 先頭車両に後続車両をびっちりくっつける
        if (!isMoving) return

        car.removeAll {
            val minecart = it.getMinecart()
            if (!minecart.isOnRail()) {
                // 地面に落ちているワゴンを削除
                // TODO 演出
                println("地面に落ちているワゴンを削除")
                minecart.remove()
            }
            return@removeAll !minecart.isOnRail()
        }

        move()
    }

    /**
     * 列車をうごかす
     */
    fun move() {
        if (!isMoving) {
            return
        }

        val speed = state().getSpeed()
        if (speed == null) {
            // TODO 先頭車爆発後処理
//            println("[ERROR] Train is Running,Speed State is Null")
            state().isDropped = true
            return
        }

        applyVectors { abstractCar, vector ->
            val now = abstractCar.getMinecart().location.block
            val next = rail.getAfter(now)
            if (next == null) {
                // 車両の先のレールがない
                state().isDropped = true
                // 速度維持
                return@applyVectors vector.scale(speed)
            } else {
                val face = now.getFace(next)
                if (face == null) {
                    // 次のレールブロックが近くにない
                    println("次のレールブロックが近くにない")
                    state().toDrop = true
                    // 速度維持
                    return@applyVectors vector.scale(speed)
                } else {
                    state().isDropped = false
                    state().toDrop = false

                    val v = face.toVector()
                    // 次のブロックの方向に基づいて速度維持
                    return@applyVectors v.scale(speed)
                }
            }
        }
    }

    fun applyVectors(vector: Vector) {
        applyVectors { _, _ -> vector }
    }

    fun applyVectors(f: (AbstractCar, Vector) -> Vector) {
        getCars().forEach {
            try {
                val fr = f(it, it.getMinecart().velocity).copy()
                it.getMinecart().velocity = fr
            } catch (e: IllegalArgumentException) {
                // 握りつぶす
            }
        }
    }


    /**
     * 今あるトロッコ全部を消滅
     */
    fun removeAll() {
        this.getCars().forEach {
            it.getMinecart().remove()
        }
        this.car.clear()
    }


    ////////////////// Listener ////////////////////
    @EventHandler
    fun onVehicleCollide(e: VehicleEntityCollisionEvent) {
        if (car.map { it.getMinecart() }.contains(e.vehicle)) {
            e.isCollisionCancelled = true
            e.isCancelled = true
        }
    }
}
