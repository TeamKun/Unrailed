package net.kunmc.lab.unrailed.train

import net.kunmc.lab.unrailed.rail.Rail
import net.kunmc.lab.unrailed.util.Direction
import org.bukkit.Location

/**
 * 列車を一括生成するやーつ
 */
interface TrainBuilder {
    /**
     * @param location 先頭車両の位置
     * @param direction 進行方向
     */
    fun onBuild(location: Location, direction: Direction): Train
}