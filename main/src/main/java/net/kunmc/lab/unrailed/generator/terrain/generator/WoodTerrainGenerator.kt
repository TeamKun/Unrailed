package net.kunmc.lab.unrailed.generator.terrain.generator

import net.kunmc.lab.unrailed.generator.GenerateSetting
import net.kunmc.lab.unrailed.util.Box
import net.kunmc.lab.unrailed.util.Direction
import net.kunmc.lab.unrailed.util.copy
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.util.noise.PerlinNoiseGenerator

class WoodTerrainGenerator(val wood: Material) : AbstractTerrainGenerator() {
    override fun onGenerate(
        startLocation: Location,
        setting: GenerateSetting,
        level: Int,
        width: Int,
        terrainSize: Int,
        direction: Direction,
        seed: Long
    ) {
        val noise = PerlinNoiseGenerator(seed)

        // 木をその場所に生成するかの閾値
        // memo:noiseは-1~1まで
        val threshold = 0.3 - level * 0.1
        val box = Box(
            startLocation.copy().add(.0, 1.0, .0),
            width,
            terrainSize,
            1,
            direction
        )

        box.getBlocks().forEach {
            val n =
                noise.noise(it.location.blockX.toDouble(), it.location.blockY.toDouble(), it.location.blockZ.toDouble())
            if (n >= threshold) {
                // ノイズが閾値を超えたため生成
                it.type = wood
            }
        }
    }
}