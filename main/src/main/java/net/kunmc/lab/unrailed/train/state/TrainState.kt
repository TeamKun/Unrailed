package net.kunmc.lab.unrailed.train.state

import net.kunmc.lab.unrailed.train.AbstractTrain
import net.kunmc.lab.unrailed.train.Train

class TrainState(val train: Train) {
    fun addModifier(modifier: StateModifier<AbstractTrain, *>) {
        when (modifier) {
            is SpeedModifier -> {
                speed.addModifier(modifier)
            }
        }
    }

    fun setForceModifier(modifier: StateModifier<AbstractTrain, *>) {
        when (modifier) {
            is SpeedModifier -> {
                speed.setForceModifier(modifier)
            }
        }
    }

    /**
     * 列車の速度のState
     */
    val speed = SpeedState(this)

    /**
     * @return 列車が線路から落ちているかどうか = ゲームオーバーかどうか
     */
    var isDropped = false

    /**
     * @return 列車が線路から落ちそうかどうか = ゲームオーバーになりかけているかどうか
     */
    var toDrop = false
}