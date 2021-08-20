package net.kunmc.lab.unrailed.train.state

import net.kunmc.lab.unrailed.car.EngineCar
import net.kunmc.lab.unrailed.train.Train

class TrainState(val train: Train) {
    fun addModifier(modifier: StateModifier) {
        when (modifier) {
            is SpeedModifier -> {
                speedModifier.add(modifier)
            }
        }
    }


    /**
     * 現在の列車の速度
     */
    fun getSpeed(): Double? {
        val d = train.getComponents(EngineCar::class.java).getOrNull(0)?.speed ?: return null
        var result = d
        speedModifier.forEach {
            result = it.moddedSpeed(result)
        }
        return result
    }

    val speedModifier = mutableListOf<SpeedModifier>()
}