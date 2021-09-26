package net.kunmc.lab.unrailed.generator

import net.kunmc.lab.unrailed.generator.terrain.generator.AbstractTerrainGenerator
import net.kunmc.lab.unrailed.util.Direction
import net.kunmc.lab.unrailed.util.WoolColor
import net.kunmc.lab.unrailed.util.copy
import net.kunmc.lab.unrailed.util.getCenter
import org.bukkit.Location
import org.bukkit.Material
import org.jetbrains.annotations.NotNull
import kotlin.math.min

/**
 * ステージ生成クラス
 */
abstract class AbstractGenerator {
    abstract fun onGenerate(s: GenerateSetting, logCallBack: (Double) -> Unit)
}

/**
 * ステージ生成設定
 * @param startLocation ゲーム開始地点
 * @param direction 生成方向
 * @param width 生成幅(startLocationが中心に左右それぞれ何マスか)
 * @param isNeedWall width外左右それぞれ1ブロックに壁を作るかどうか
 * @param wallBlock 壁の材質
 * @param terrainSize 地形生成時に次の地形に何ブロックで移行するか
 * @param seed シード値
 * @param betweenStation 駅間の地形数(両端の駅の地形は含まない)
 * @param stations ゴールまでのすべての駅数(ゴールの駅を含む)
 * @param level 難易度(1~)
 * @property terrains 生成すべき地形の個数
 */
data class GenerateSetting(
    val startLocation: Location,
    val direction: Direction,
    val width: Int,
    val isNeedWall: Boolean,
    val wallBlock: Material = Material.BEDROCK,
    val terrainSize: Int,
    val seed: Long,
    val betweenStation: Int,
    val stations: Int,
    val teamColor: WoolColor,
    val level: Int = 1
) {
    val terrains = (betweenStation + 1) * (stations - 1) + 1

    fun getStartWoolLocation(): Location {
        val terrainCenter =
            startLocation.copy()
                .add(direction.toVector(terrainSize.getCenter().toDouble()))
                .toBlockLocation()

        val moveSize = min(width, 3)

        return terrainCenter.add(direction.rotateLeft(1).toVector(moveSize.toDouble())).toBlockLocation()
    }
}