package net.kunmc.lab.unrailed.generator

import net.kunmc.lab.unrailed.generator.terrain.generator.BaseTerrainGenerator
import net.kunmc.lab.unrailed.generator.terrain.generator.MultiTerrainGenerator
import net.kunmc.lab.unrailed.generator.terrain.generator.WoodTerrainGenerator
import org.bukkit.Material

class StandardGenerator : AbstractGenerator() {
    val generators = MultiTerrainGenerator(
        listOf(
            BaseTerrainGenerator(Material.SANDSTONE),
            WoodTerrainGenerator(Material.ACACIA_WOOD)
        )
    )

    override fun onGenerate(s: GenerateSetting) {
        generators.onGenerate(
            s.startLocation,
            s.width,
            s.terrainSize,
            s.direction,
            1,
            s.seed
        )
    }
}