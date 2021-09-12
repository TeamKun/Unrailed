package net.kunmc.lab.unrailed.generator.terrain.generator

import net.kunmc.lab.unrailed.generator.GenerateSetting
import net.kunmc.lab.unrailed.util.Direction
import org.bukkit.Location

/**
 * 最初の地形の羊毛置くやつ
 */
class StartWoolTerrainGenerator : AbstractTerrainGenerator() {
    override fun onGenerate(
        startLocation: Location,
        setting: GenerateSetting,
        level: Int,
        width: Int,
        terrainSize: Int,
        direction: Direction,
        seed: Long
    ) {
        setting.getStartWoolLocation().block.type = setting.teamColor.woolMaterial
    }
}