package net.kunmc.lab.unrailed.train

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.car.AbstractCar
import net.kunmc.lab.unrailed.car.EngineCar
import net.kunmc.lab.unrailed.rail.Rail
import net.kunmc.lab.unrailed.train.state.TrainState

class Train(firstCar: EngineCar, val rail: Rail, private val plugin: Unrailed) : AbstractTrain() {
    val car = mutableListOf<AbstractCar>()
    override fun getLength(): Int = car.size
    override fun getCars(): MutableList<AbstractCar> = car.toMutableList()
    override fun addCar(car: AbstractCar) {
        this.car.add(car)
    }

    private val State = TrainState(this)
    override fun state(): TrainState = State

    override fun getRunningRail(): Rail = rail

    init {
        addCar(firstCar)
        plugin.server.scheduler.runTaskTimer(plugin, Runnable { onUpdate() }, 1, 1)
    }

    private fun onUpdate() {
        if (!plugin.isGoingOn) return
        //TODO 先頭車両に後続車両をびっちりくっつける
        TODO("先頭車両に後続車両をびっちりくっつける")
    }
}