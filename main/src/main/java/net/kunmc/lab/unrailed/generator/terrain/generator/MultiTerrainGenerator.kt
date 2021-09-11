package net.kunmc.lab.unrailed.generator.terrain.generator

import net.kunmc.lab.unrailed.generator.GenerateSetting
import net.kunmc.lab.unrailed.util.Direction
import org.bukkit.Location

/**
 * 複数のGeneratorを組み合わせるやーつ
 */
class MultiTerrainGenerator(val generators: List<AbstractTerrainGenerator>) : AbstractTerrainGenerator() {
    override fun onGenerate(
        startLocation: Location,
        setting: GenerateSetting,
        level: Int,
        width: Int,
        terrainSize: Int,
        direction: Direction,
        seed: Long
    ) {
        generators.forEach {
            it.onGenerate(startLocation = startLocation, setting = setting, level = level)
        }
    }
}