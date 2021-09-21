package net.kunmc.lab.unrailed.generator.terrain.generator

import net.kunmc.lab.unrailed.generator.GenerateSetting
import net.kunmc.lab.unrailed.structure.BlockSet
import net.kunmc.lab.unrailed.util.Box
import net.kunmc.lab.unrailed.util.Direction
import net.kunmc.lab.unrailed.util.copy
import net.kunmc.lab.unrailed.util.getCenter
import org.bukkit.Location
import org.bukkit.Material

class StationTerrainGenerator(private val stationStructure: BlockSet) : AbstractTerrainGenerator() {
    override fun onGenerate(
        startLocation: Location,
        setting: GenerateSetting,
        level: Int,
        width: Int,
        terrainSize: Int,
        direction: Direction,
        seed: Long
    ) {
        // 土台の上
        val box = Box(
            startLocation.copy().add(.0, 1.0, .0),
            width,
            terrainSize,
            1,
            direction
        )

        val railBox = Box(startLocation.copy().add(.0, 1.0, .0), 0, terrainSize, 0, direction)

        railBox.getBlocks().forEach {
            it.type = Material.RAIL
        }

        val stationCenter =
            startLocation.copy()
                .add(.0, 1.0, .0)
                .add(direction.toVector(terrainSize.getCenter().toDouble()))
//                .add(direction.rotateLeft(1).toVector(1.0))
                .toBlockLocation()


        // いい感じに回転できるはず!!!!!
        val rotatedStationStructure =
            stationStructure.copy()
                .rotateAroundY(
                    Math.toRadians(stationStructure.direction!!.getDegree(direction).toDouble()),
                    startLocation.world
                )

        val stationSize = rotatedStationStructure.toBox(stationCenter)

        val stationStart =
            stationCenter.toVector()
                .add(
                    direction.opposite().toVector(
                        stationSize.getLengthFor(direction.toAxis()).getCenter().toDouble()
                    )
                )  // 横方向調整
//                .add(direction.rotateLeft(1).toVector(stationSize.width.toDouble()))

        rotatedStationStructure.copyTo(stationStart.toLocation(startLocation.world))
    }
}