package net.kunmc.lab.unrailed.generator.terrain.generator

import net.kunmc.lab.unrailed.util.Direction
import org.bukkit.Location
import org.bukkit.Material

/**
 * すべての地形の下の部分を指定ブロックで埋める
 */
class BaseTerrainGenerator(val block: Material) : AbstractTerrainGenerator() {
    override fun onGenerate(
        startLocation: Location,
        width: Int,
        terrainSize: Int,
        direction: Direction,
        level: Int
    ) {
    }
}