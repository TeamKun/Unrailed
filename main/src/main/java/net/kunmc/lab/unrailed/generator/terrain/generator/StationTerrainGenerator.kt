package net.kunmc.lab.unrailed.generator.terrain.generator

import net.kunmc.lab.unrailed.structure.BlockSet
import net.kunmc.lab.unrailed.util.Box
import net.kunmc.lab.unrailed.util.Direction
import net.kunmc.lab.unrailed.util.copy
import net.kunmc.lab.unrailed.util.getCenter
import org.bukkit.Location
import org.bukkit.Material

class StationTerrainGenerator(val stationStructure: BlockSet) : AbstractTerrainGenerator() {
    override fun onGenerate(
        startLocation: Location,
        width: Int,
        terrainSize: Int,
        direction: Direction,
        level: Int,
        seed: Long
    ) {
        // TODO 駅の生成どうしようかな

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
                .add(direction.rotateLeft(1).toVector(1.0))
                .toBlockLocation()

        val rotatedStationStructure = stationStructure.rotateAroundY(Math.toRadians(90.0))

        val stationSize = stationStructure.toBox(stationCenter)
    }
}