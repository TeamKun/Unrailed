package net.kunmc.lab.unrailed.listener

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.generator.StandardGenerator
import net.kunmc.lab.unrailed.util.contain
import net.kunmc.lab.unrailed.util.info
import net.kunmc.lab.unrailed.util.isJoinedGame
import net.kunmc.lab.unrailed.util.isRail
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent

class BlockBreakListener(unrailed: Unrailed) : ListenerBase(unrailed) {
    @EventHandler
    fun onBreak(e: BlockBreakEvent) {
        if (e.player.isJoinedGame()) {
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
                if (e.block.type == StandardGenerator.baseBlock) {
                    // 地面掘ってるだけ、許したれ。
                    // ↑誰が許すかアホ！
                    e.isCancelled = true
                } else {
                    // 石とか掘ってるんやろ。たぶん。
                }
            }
        }
    }
}