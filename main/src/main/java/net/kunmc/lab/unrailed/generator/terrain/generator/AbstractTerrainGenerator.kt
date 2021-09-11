package net.kunmc.lab.unrailed.generator.terrain.generator

import net.kunmc.lab.unrailed.generator.GenerateSetting
import net.kunmc.lab.unrailed.util.Direction
import org.bukkit.Location

/**
 * 地形ごとのGenerator
 * これを連続的に交換しつつ生成する
 */
abstract class AbstractTerrainGenerator {
    /**
     * @param startLocation 地形生成開始地点
     * @param width 生成幅(startLocationが中心に左右それぞれ何マスか)
     * @param terrainSize この地形生成の長さ
     * @param direction 生成方向
     * @param level この地形生成の難易度(1が一番易しい)
     * @note levelが高くなればなるほど、川や壊せない石などを多く生成
     * @param seed シード値
     */
    abstract fun onGenerate(
        startLocation: Location,
        setting: GenerateSetting,
        level: Int,
        width: Int = setting.width,
        terrainSize: Int = setting.terrainSize,
        direction: Direction = setting.direction,
        seed: Long = setting.seed
    )
}