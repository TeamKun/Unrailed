package net.kunmc.lab.unrailed.train.state

import net.kunmc.lab.unrailed.car.EngineCar
import net.kunmc.lab.unrailed.train.AbstractTrain

class SpeedState(val trainState: TrainState) : State<AbstractTrain, Double>(trainState.train, {
    trainState.train.getComponents(
        EngineCar::class.java
    ).getOrNull(0)?.speed
})