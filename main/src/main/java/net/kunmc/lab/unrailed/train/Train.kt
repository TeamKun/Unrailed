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
import org.bukkit.util.Vector
import java.lang.IllegalArgumentException

class Train(val firstCar: EngineCar, val rail: Rail, private val plugin: Unrailed) : AbstractTrain() {
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

        getCars().map { it.getMinecart() }.filter { !it.isOnRail() }.forEach {
            // 地面に落ちているワゴンを削除
            // TODO 演出
            println("地面に落ちているワゴンを削除")
            println("it:$it")
            it.damage = Double.MAX_VALUE
        }

        val speed = state().getSpeed()
        if (speed == null) {
            // TODO 先頭車爆発後処理
            println("[ERROR] Train is Running,Speed State is Null")
            return
        }

        move()
    }

    /**
     * 列車をうごかす
     */
    fun move() {
        if (!isMoving) {
            println("boost is called,this train is not moving")
            return
        }

        val speed = state().getSpeed()
        if (speed == null) {
            // TODO 先頭車爆発後処理
            println("[ERROR] Train is Running,Speed State is Null")
            return
        }

        applyVectors { abstractCar, vector ->
            val now = abstractCar.getMinecart().location.block
            val next = rail.getAfter(now)
            if (next == null) {
                // 車両の先のレールがない
                // TODO どうしよ
                return@applyVectors vector.scale(speed)
            } else {
                val face = now.getFace(next)
                if (face == null) {
                    // 次のレールブロックが近くにない
                    println("次のレールブロックが近くにない")
                    return@applyVectors Vector().zero()
                } else {
                    val v = face.toVector()
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
}
