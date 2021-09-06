package net.kunmc.lab.unrailed.generator

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.generator.terrain.generator.*
import net.kunmc.lab.unrailed.structure.StructureCollection
import net.kunmc.lab.unrailed.util.Direction
import net.kunmc.lab.unrailed.util.RandomDo
import net.kunmc.lab.unrailed.util.copy
import net.kunmc.lab.unrailed.util.scale
import org.bukkit.Location
import org.bukkit.Material

class StandardGenerator(val unrailed: Unrailed) : AbstractGenerator() {
    companion object {
        // 各地形の土台
        var baseBlock = Material.SANDSTONE
    }

    private val woodTerrain = MultiTerrainGenerator(
        listOf(
            BaseTerrainGenerator(baseBlock),
            WoodTerrainGenerator(Material.ACACIA_WOOD)
        )
    )

    private val stoneTerrain = MultiTerrainGenerator(
        listOf(
            BaseTerrainGenerator(baseBlock),
            StoneTerrainGenerator(Material.STONE)
        )
    )

    private val stationTerrain = MultiTerrainGenerator(
        listOf(
            // TODO
            BaseTerrainGenerator(baseBlock),
            StationTerrainGenerator(StructureCollection.getInstance(unrailed).stations.SandStation)
        )
    )

    /**
     *
     */
    override fun onGenerate(s: GenerateSetting, logCallBack: (Double) -> Unit) {
        for (index in 0 until s.terrains) {
            val location = getStartLocation(s, index)

            if (index % (s.betweenStation + 1) == 0) {
                // 駅の生成
                generate(stationTerrain, s, location)
                continue
            }

            // TODO ここでいろいろ生成
            RandomDo(
                {
                    generate(woodTerrain, s, location)
                },
                {
                    generate(stoneTerrain, s, location)
                }
            )


            logCallBack((((index + 1) * 100) / (s.terrains).toDouble()))
        }
    }

    private fun getStartLocation(s: GenerateSetting, count: Int): Location {
        return getStartLocation(s.startLocation, s.direction, count, s.terrainSize)
    }

    /**
     * @return count個目の地形の生成開始地点
     * @param terrainSize 実際の奥行 - 1
     */
    private fun getStartLocation(
        base: Location,
        direction: Direction,
        count: Int,
        terrainSize: Int
    ): Location {
        return base.copy().add(direction.toVector(terrainSize.toDouble()).scale(terrainSize + 1.0).multiply(count))
    }

    private fun generate(generator: MultiTerrainGenerator, s: GenerateSetting) {
        generate(generator, s, s.startLocation)
    }

    /**
     * startLocationから地形を生成
     */
    private fun generate(
        generator: MultiTerrainGenerator,
        s: GenerateSetting,
        startLocation: Location
    ) {
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