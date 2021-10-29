package net.kunmc.lab.unrailed.listener

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.generator.StandardGenerator
import net.kunmc.lab.unrailed.util.*
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent

class BlockBreakListener(unrailed: Unrailed) : ListenerBase(unrailed) {
    @EventHandler
    fun onBreak(e: BlockBreakEvent) {
        if (e.player.isJoinedGame()) {
            val gamePlayer = e.player.getGamePlayer()!!

            if (e.block.isRail()) {
                val lane = Unrailed.goingOnGame!!.getLane(e.player)!!
                val train = lane.train
                if (train == null) {
                    // 列車生成前
                    e.isCancelled = true
                } else {
                    val rail = train.rail
                    val edges = rail.getEdge()
                    if (edges != null && (edges.first == e.block || edges.second == e.block)) {
                        if (rail.remove(e.block)) {
                            // Remove出来た
                        } else {
                            // Remove出来なかった
                            e.isCancelled = true
                        }
                    } else {
                        e.isCancelled = true
                    }
                }
            } else {
                if (e.block.type.isStone() || e.block.type.isWood()) {
                    // 石とか木とか掘ってる
                    gamePlayer.state.points += gamePlayer.game.gameSetting.pointPerBreak
                } else {
                    // 地面とか掘ってる
                    e.isCancelled = true
                }
            }
        }
    }
}