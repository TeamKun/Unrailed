package net.kunmc.lab.unrailed.train

import net.kunmc.lab.unrailed.car.AbstractCar
import net.kunmc.lab.unrailed.train.state.TrainState

/**
 * ワゴン全体
 * @see AbstractCar
 */
abstract class AbstractTrain {
    /**
     * @return 先頭車両含む全体の両数
     */
    abstract fun getLength(): Int

    /**
     * @return 先頭車両含む全車両取得
     */
    abstract fun getCars(): MutableList<AbstractCar>

    /**
     * Add Car
     */
    abstract fun addCar(car: AbstractCar)

    /**
     * @return 車両ごとのLocation
     */
    fun getLocations() = getCars().map { it.getMinecart().location }

    /**
     * @return 先頭車両のLocation
     */
    fun getFirstLocation() = getCars().getOrNull(0)?.getMinecart()?.location

    /**
     * @return state of this train
     */
    abstract fun state(): TrainState

    inline fun <reified T : AbstractCar> getComponents(clazz: Class<T>): List<T> {
        return getCars().filterIsInstance<T>()
    }
}