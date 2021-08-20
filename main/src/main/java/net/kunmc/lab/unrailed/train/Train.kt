package net.kunmc.lab.unrailed.train

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.car.AbstractCar
import net.kunmc.lab.unrailed.car.EngineCar
import net.kunmc.lab.unrailed.rail.Rail
import net.kunmc.lab.unrailed.rail.isRail
import net.kunmc.lab.unrailed.train.state.TrainState
import org.bukkit.entity.Minecart
import org.bukkit.util.Vector

class Train(firstCar: EngineCar, val rail: Rail, private val plugin: Unrailed) : AbstractTrain() {
    val car = mutableListOf<AbstractCar>()
    override fun getLength(): Int = car.size
    override fun getCars(): MutableList<AbstractCar> = car.toMutableList()
    override fun addCar(car: AbstractCar) {
        this.car.add(car)
    }

    private val State = TrainState(this)
    override fun state(): TrainState = State

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

        getCars().map { it.getMinecart() }.filter { !it.isOnRail() }.forEach {
            // 地面に落ちているワゴンを削除
            // TODO 演出

        }

        val speed = state().getSpeed()
        if (speed == null) {
            // TODO 先頭車爆発後処理
            println("[ERROR] Train is Running,Speed State is Null")
            return
        }

        getCars().map { it.getMinecart() }.forEach {
            // 速度を同じに調整
            it.velocity.scale(speed)
        }
    }
}


/**
 * lengthがscaleになるように調整
 */
fun Vector.scale(scale: Double) {
    multiply(length() / scale)
}

/**
 * Minecartがレールに乗っているかどうか
 */
fun Minecart.isOnRail(): Boolean {
    return location.block.isRail()
}