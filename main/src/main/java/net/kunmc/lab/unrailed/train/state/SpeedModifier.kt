package net.kunmc.lab.unrailed.train.state

abstract class SpeedModifier(trainState: TrainState) : StateModifier(trainState) {
    /**
     * @param beforeSpeed この効果適用前の列車のスピード
     * @return 効果適用後のスピード
     */
    abstract fun moddedSpeed(beforeSpeed:Double):Double
}