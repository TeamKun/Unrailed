package net.kunmc.lab.unrailed.generator

import net.kunmc.lab.unrailed.generator.terrain.generator.BaseTerrainGenerator
import net.kunmc.lab.unrailed.generator.terrain.generator.MultiTerrainGenerator
import net.kunmc.lab.unrailed.generator.terrain.generator.StoneTerrainGenerator
import net.kunmc.lab.unrailed.generator.terrain.generator.WoodTerrainGenerator
import net.kunmc.lab.unrailed.util.Direction
import net.kunmc.lab.unrailed.util.RandomDo
import net.kunmc.lab.unrailed.util.copy
import net.kunmc.lab.unrailed.util.scale
import org.bukkit.Location
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

        for (index in 0 until s.terrains) {
            val location = getStartLocation(s, index)
            // TODO ここでいろいろ生成
            RandomDo(
                {
                    println("woodTerrain")
                    generate(woodTerrain, s, location)
                },
                {
                    println("stoneTerrain")
                    generate(stoneTerrain, s, location)
                }
            )
        }
    }

    private fun getStartLocation(s: GenerateSetting, count: Int): Location {
        return getStartLocation(s.startLocation, s.direction, count, s.terrainSize)
    }

    /**
     * @return count個目の地形の生成開始地点
     * @param terrainSize 実際の奥行 - 1
     */
    private fun getStartLocation(base: Location, direction: Direction, count: Int, terrainSize: Int): Location {
        return base.copy().add(direction.toVector(terrainSize.toDouble()).scale(terrainSize + 1.0).multiply(count))
    }

    private fun generate(generator: MultiTerrainGenerator, s: GenerateSetting) {
        generate(generator, s, s.startLocation)
    }

    /**
     * startLocationから地形を生成
     */
    private fun generate(generator: MultiTerrainGenerator, s: GenerateSetting, startLocation: Location) {
        generator.onGenerate(
            startLocation,
            s.width,
            s.terrainSize,
            s.direction,
            1,
            s.seed
        )
    }
}