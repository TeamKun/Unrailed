package net.kunmc.lab.unrailed.train

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.car.EngineCar
import net.kunmc.lab.unrailed.car.StorageCar
import net.kunmc.lab.unrailed.rail.Rail
import net.kunmc.lab.unrailed.util.Direction
import org.bukkit.Location

/**
 * 最初の電車のテンプレ
 */
class DefaultTrainBuilder(val unrailed: Unrailed, val startTerrainCenter: Location, val rail: Rail) : TrainBuilder {
    override fun onBuild(location: Location, direction: Direction): Train {
        return Train(
            EngineCar(unrailed, startTerrainCenter.add(.0, 1.0, .0)),
            rail,
            unrailed
        ).addCar { l, unrailed -> StorageCar(unrailed, l) }
    }
}