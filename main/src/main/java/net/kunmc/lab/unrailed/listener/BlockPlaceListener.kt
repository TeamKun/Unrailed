package net.kunmc.lab.unrailed.listener

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.util.*
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockPlaceEvent

/**
 * @see net.kunmc.lab.unrailed.game.player.GamePlayer がレールを設置したときに認識する用
 *
 * GamePlayerのブロック設置も抑止
 */
class BlockPlaceListener(unrailed: Unrailed) : ListenerBase(unrailed) {
    @EventHandler
    fun onPlace(e: BlockPlaceEvent) {
        if (e.player.isJoinedGame()) {
            if (e.block.isRail()) {
                // レール設置
                val lane = Unrailed.goingOnGame!!.getLane(e.player)!!

                if (lane.generateSetting.startLocation.blockY + 1 != e.blockPlaced.location.blockY) {
                    // レールの高さが違う
                    e.isCancelled = true
                    return
                }

                val train = lane.train
                if (train != null) {
                    val rail = train.rail
                    val result = rail.add(e.block)
                    if (!result) {
                        // 接続不可
                        e.isCancelled = true
                    } else {
                        debug("Rail Size is now:${rail.rails.size}")
                    }
                } else {
                    // 列車生成前
                    e.isCancelled = true
                }
            } else {
                e.isCancelled = true
            }
        }
    }
}