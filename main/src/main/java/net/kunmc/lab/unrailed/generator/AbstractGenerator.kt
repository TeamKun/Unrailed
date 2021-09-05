package net.kunmc.lab.unrailed.generator

import net.kunmc.lab.unrailed.generator.terrain.generator.AbstractTerrainGenerator
import net.kunmc.lab.unrailed.util.Direction
import org.bukkit.Location
import org.bukkit.Material

/**
 * ステージ生成クラス
 */
abstract class AbstractGenerator {
    abstract fun onGenerate(s: GenerateSetting,logCallBack: (Double) -> Unit)
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
 * @param terrains 生成すべき地形の個数
 */
data class GenerateSetting(
    val startLocation: Location,
    val direction: Direction,
    val width: Int,
    val isNeedWall: Boolean,
    val wallBlock: Material = Material.BEDROCK,
    val terrainSize: Int,
    val seed: Long,
    val terrains:Int
)