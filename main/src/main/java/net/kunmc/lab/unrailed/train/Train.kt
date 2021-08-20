package net.kunmc.lab.unrailed.train

import net.kunmc.lab.unrailed.car.AbstractCar
import net.kunmc.lab.unrailed.train.state.TrainState

class Train(firstCar: AbstractCar) : AbstractTrain() {
    val car = mutableListOf<AbstractCar>()
    override fun getLength(): Int = car.size
    override fun getCars(): MutableList<AbstractCar> = car.toMutableList()
    override fun addCar(car: AbstractCar) {
        this.car.add(car)
    }

    private val State = TrainState(this)
    override fun state(): TrainState = State

    init {
        addCar(firstCar)
    }
}