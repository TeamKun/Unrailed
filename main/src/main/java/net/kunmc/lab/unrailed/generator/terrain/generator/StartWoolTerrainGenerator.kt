package net.kunmc.lab.unrailed.generator.terrain.generator

import net.kunmc.lab.unrailed.generator.GenerateSetting
import net.kunmc.lab.unrailed.util.Direction
import net.kunmc.lab.unrailed.util.copy
import net.kunmc.lab.unrailed.util.getCenter
import org.bukkit.Location
import kotlin.math.min

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
        val terrainCenter =
            startLocation.copy()
                .add(direction.toVector(terrainSize.getCenter().toDouble()))
                .toBlockLocation()

        val moveSize = min(width, 3)

        val woolVector = terrainCenter.add(direction.rotateLeft(1).toVector(moveSize.toDouble()))

        woolVector.toBlockLocation().block.type = setting.teamColor.woolMaterial
    }
}