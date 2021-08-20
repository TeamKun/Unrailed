package net.kunmc.lab.unrailed.train

class Train : AbstractTrain() {
    val car = mutableListOf<AbstractCar>()
    override fun getLength(): Int = car.size
    override fun getCars(): MutableList<AbstractCar> = car.toMutableList()
    override fun addCar(car: AbstractCar) {
        this.car.add(car)
    }
}