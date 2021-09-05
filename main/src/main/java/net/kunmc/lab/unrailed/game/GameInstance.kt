package net.kunmc.lab.unrailed.game

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.car.EngineCar
import net.kunmc.lab.unrailed.game.player.GamePlayer
import net.kunmc.lab.unrailed.generator.AbstractGenerator
import net.kunmc.lab.unrailed.generator.GenerateSetting
import net.kunmc.lab.unrailed.generator.StandardGenerator
import net.kunmc.lab.unrailed.rail.Rail
import net.kunmc.lab.unrailed.rail.RailBlockException
import net.kunmc.lab.unrailed.rail.RailException
import net.kunmc.lab.unrailed.train.Train
import net.kunmc.lab.unrailed.train.TrainBuilder
import net.kunmc.lab.unrailed.util.Direction
import net.kunmc.lab.unrailed.util.broadCast
import net.kunmc.lab.unrailed.util.copy
import net.kunmc.lab.unrailed.util.getCenter
import org.bukkit.Bukkit
import org.bukkit.Location

/**
 * 1つのゲームを表すクラス
 */
class GameInstance(val unrailed: Unrailed) {
    companion object {
        /**
         * 列車がゲーム開始から何tick後に動くか
         */
        const val MovingTime = 15 * 20L
    }

    val lanes = mutableListOf<LaneInstance>()

    fun addLane(
        g: GenerateSetting,
        members: List<GamePlayer> = listOf(),
        generator: AbstractGenerator = StandardGenerator()
    ): LaneInstance? {
        try {
            val startTerrainCenter =
                g.startLocation.copy().add(g.direction.toVector(g.terrainSize.getCenter().toDouble())).toBlockLocation()
            val rail = Rail(startTerrainCenter.block)
            val lane = LaneInstance(
                this,
                object : TrainBuilder {
                    override fun onBuild(location: Location, direction: Direction): Train {
                        return Train(
                            EngineCar(unrailed, startTerrainCenter.add(.0, 1.0, .0)),
                            rail,
                            unrailed
                        )
                    }
                },
                g,
                generator
            )

            lane.teamMember.addAll(members)
            lanes.add(lane)

            return lane
        } catch (e: RailBlockException) {
            e.print()
            return null
        } catch (e: RailException) {
            e.print()
            return null
        }
    }

    /**
     * このゲームの開始処理
     */
    fun start() {
        broadCast("開始処理中...")
        lanes.forEachIndexed { index, lane ->
            lane.generateAll {
                broadCast("生成中 ${index + 1}/${lanes.size}:${it}")
            }
            lane.start()
        }
        unrailed.server.scheduler.runTaskLater(unrailed, Runnable { startMoving() }, MovingTime)
    }

    /**
     * このゲームのすべての列車を動かし始める処理
     */
    fun startMoving() {
        broadCast("開始!")
        lanes.forEach {
            it.startMoving()
        }
    }

    /**
     * このゲーム内のレーンがクリアしたときの処理
     */
    fun onClear(lane: LaneInstance) {

    }

    /**
     * このゲーム内のレーンが脱落した時の処理
     */
    fun onFail(lane: LaneInstance) {

    }
}