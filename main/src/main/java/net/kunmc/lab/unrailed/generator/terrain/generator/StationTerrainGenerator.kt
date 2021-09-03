package net.kunmc.lab.unrailed.generator.terrain.generator

import net.kunmc.lab.unrailed.util.Direction
import org.bukkit.Location

class StationTerrainGenerator() : AbstractTerrainGenerator() {
    override fun onGenerate(
        startLocation: Location,
        width: Int,
        terrainSize: Int,
        direction: Direction,
        level: Int,
        seed: Long
    ) {
        // TODO 駅の生成どうしようかな
    }
}