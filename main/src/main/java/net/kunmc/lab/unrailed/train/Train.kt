package net.kunmc.lab.unrailed.train

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.car.AbstractCar
import net.kunmc.lab.unrailed.car.EngineCar
import net.kunmc.lab.unrailed.game.LaneInstance
import net.kunmc.lab.unrailed.rail.Rail
import net.kunmc.lab.unrailed.train.state.TrainState
import net.kunmc.lab.unrailed.util.*
import org.bukkit.Location
import org.bukkit.entity.Minecart
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.vehicle.VehicleCollisionEvent
import org.bukkit.event.vehicle.VehicleDestroyEvent
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

    fun addCar(f: (Location, Unrailed) -> AbstractCar): Train {
        val lastCar = car.last().getMinecart()
        println("Rail Size:${rail.rails.size}")
        val lastCarRailIndex = rail.getIndex(lastCar.location.block)
        val addLocation = when {
            lastCarRailIndex == null -> {
                println("in AddCar,lastCarRailIndex is null")
                // うまくindex取得できなかった場合
                lastCar.location
            }
            lastCarRailIndex != 0 -> {
                // 中間のレールに乗ってる
                println("中間のレールに乗ってる")
                rail.rails[lastCarRailIndex - 1].location
            }
            else -> {
                // 最初のレールに乗ってる
                println("最初のレールに乗ってる")
                rail.rails[0].location
            }
        }
        addCar(f(addLocation, plugin))
        return this
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
                if (it is EngineCar) {
                    // 先頭車のみ判定に必要
                    getLane()?.onFail(minecart.location)
                }
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
                    // TODO レールの先にブロックがあったら?
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

    @EventHandler
    fun onBreakVehicle(e: VehicleDestroyEvent) {
        if (e.vehicle is Minecart) {
            if (car.map { it.getMinecart() }.contains(e.vehicle)) {
                e.isCancelled = true
            }
        }
    }


    ////////////////// Game ///////////////
    fun getLane(): LaneInstance? {
        return Unrailed.goingOnGame?.getLane(this)
    }
}
