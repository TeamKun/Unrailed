package net.kunmc.lab.unrailed.generator

import net.kunmc.lab.unrailed.generator.terrain.generator.BaseTerrainGenerator
import net.kunmc.lab.unrailed.generator.terrain.generator.MultiTerrainGenerator
import net.kunmc.lab.unrailed.generator.terrain.generator.StoneTerrainGenerator
import net.kunmc.lab.unrailed.generator.terrain.generator.WoodTerrainGenerator
import net.kunmc.lab.unrailed.util.RandomDo
import org.bukkit.Material

class StandardGenerator : AbstractGenerator() {
    private val woodTerrain = MultiTerrainGenerator(
        listOf(
            BaseTerrainGenerator(Material.SANDSTONE),
            WoodTerrainGenerator(Material.ACACIA_WOOD)
        )
    )

    private val stoneTerrain = MultiTerrainGenerator(
        listOf(
            BaseTerrainGenerator(Material.SANDSTONE),
            StoneTerrainGenerator(Material.STONE)
        )
    )

    override fun onGenerate(s: GenerateSetting) {
        // TODO ここでいろいろ生成
        RandomDo(
            {
                println("woodTerrain")
                generate(woodTerrain, s)
            },
            {
                println("stoneTerrain")
                generate(stoneTerrain, s)
            }
        )
    }

    private fun generate(generator: MultiTerrainGenerator, s: GenerateSetting) {
        generator.onGenerate(
            s.startLocation,
            s.width,
            s.terrainSize,
            s.direction,
            1,
            s.seed
        )
    }
}