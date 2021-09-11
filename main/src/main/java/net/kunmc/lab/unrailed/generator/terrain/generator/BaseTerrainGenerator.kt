package net.kunmc.lab.unrailed.generator.terrain.generator

import net.kunmc.lab.unrailed.generator.GenerateSetting
import net.kunmc.lab.unrailed.util.Box
import net.kunmc.lab.unrailed.util.Direction
import net.kunmc.lab.unrailed.util.copy
import net.kunmc.lab.unrailed.util.fill
import org.bukkit.Location
import org.bukkit.Material

/**
 * すべての地形の下の部分を指定ブロックで埋める
 */
class BaseTerrainGenerator(val block: Material) : AbstractTerrainGenerator() {
    override fun onGenerate(
        startLocation: Location,
        setting: GenerateSetting,
        level: Int,
        width: Int,
        terrainSize: Int,
        direction: Direction,
        seed: Long
    ) {
        val bottom = Box(startLocation, width, terrainSize, -startLocation.blockY + 1, direction)
        bottom.fill(block)
        val upper = Box(
            startLocation.copy().add(.0, 1.0, .0),
            width,
            terrainSize,
            256 - startLocation.copy().add(.0, 1.0, .0).blockY,
            direction
        )
        upper.fill(Material.AIR)
    }
}