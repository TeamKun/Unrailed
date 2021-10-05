package net.kunmc.lab.unrailed.train.state

import net.kunmc.lab.unrailed.train.AbstractTrain

open class SpeedModifier(state: SpeedState, id: String, var f: (AbstractTrain, Double) -> Double) :
    StateModifier<AbstractTrain, Double>(state, id) {
    override fun onCalculate(t: AbstractTrain, beforeValue: Double): Double = f(t, beforeValue)
}

/**
 * 完全停車させるやつ
 */
class SpeedStopper(state: SpeedState, id: String) : SpeedModifier(state, id, { _, _ -> .0 })