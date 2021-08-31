package net.kunmc.lab.unrailed.generator.terrain.generator

import net.kunmc.lab.unrailed.util.Direction
import org.bukkit.Location

/**
 * 複数のGeneratorを組み合わせるやーつ
 */
class MultiTerrainGenerator(val generators: List<AbstractTerrainGenerator>) : AbstractTerrainGenerator() {
    override fun onGenerate(
        startLocation: Location,
        width: Int,
        terrainSize: Int,
        direction: Direction,
        level: Int
    ) {
        generators.forEach {
            it.onGenerate(startLocation, width, terrainSize, direction, level)
        }
    }
}