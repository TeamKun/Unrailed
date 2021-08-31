package net.kunmc.lab.unrailed.util

import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.util.Vector
import org.jetbrains.annotations.NotNull
import kotlin.math.max
import kotlin.math.min

/**
 * World上の範囲指定用
 * @param width X軸方向
 * @param weight Z軸方向
 * @param height Y軸方向
 */
class Box(val base: Location, val width: Int, val weight: Int, val height: Int) {
    fun getBlocks(): MutableList<Block> {
        val list = mutableListOf<Block>()
        for (x in base.blockX..base.blockX + width) {
            for (y in base.blockY..base.blockY + height) {
                for (z in base.blockZ..base.blockZ + weight) {
                    val loc = Location(base.world, x.toDouble(), y.toDouble(), z.toDouble())
                    list.add(loc.block)
                }
            }
        }
        return list
    }

    constructor(from: Location, to: Location) : this(
        Location(
            from.world, min(from.blockX, to.blockX).toDouble(),
            min(from.blockY, to.blockY).toDouble(), min(from.blockZ, to.blockZ).toDouble()
        ),
        max(from.blockX, to.blockX) - min(from.blockX, to.blockX),
        max(from.blockZ, to.blockZ) - min(from.blockZ, to.blockZ),
        max(from.blockY, to.blockY) - min(from.blockY, to.blockY)
    )

    /**
     * @param width startLocation含まず左右何ブロックか
     * @param terrainSize **実際の奥行 - 1**(こらそこ直せとか言わない)
     * @param height **実際の高さ - 1**(こらそこ直せとか言わない)
     */
    constructor(
        startLocation: Location,
        width: Int,
        terrainSize: Int,
        height: Int,
        direction: Direction
    ) : this(
        startLocation.copy().add(direction.rotateLeft(1).toVector(width.toDouble())),
        startLocation.copy().add(
            direction.rotateRight(1).toVector(width.toDouble())
                .add(direction.toVector(terrainSize.toDouble()))
                .add(Vector(0, height, 0))
        )
    )
}