package net.kunmc.lab.unrailed.game

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.car.EngineCar
import net.kunmc.lab.unrailed.game.phase.EndPhase
import net.kunmc.lab.unrailed.game.phase.GamePhase
import net.kunmc.lab.unrailed.game.phase.Phase
import net.kunmc.lab.unrailed.game.phase.PreparePhase
import net.kunmc.lab.unrailed.game.player.GamePlayer
import net.kunmc.lab.unrailed.generator.AbstractGenerator
import net.kunmc.lab.unrailed.generator.GenerateSetting
import net.kunmc.lab.unrailed.generator.StandardGenerator
import net.kunmc.lab.unrailed.rail.Rail
import net.kunmc.lab.unrailed.rail.RailBlockException
import net.kunmc.lab.unrailed.rail.RailException
import net.kunmc.lab.unrailed.train.DefaultTrainBuilder
import net.kunmc.lab.unrailed.train.Train
import net.kunmc.lab.unrailed.train.TrainBuilder
import net.kunmc.lab.unrailed.util.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

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

    init {
        if (Unrailed.goingOnGame == null) {
            Unrailed.goingOnGame = this
        } else {
            println("GameInstance Already Generated!")
            Unrailed.goingOnGame!!.onAllDone()
            Unrailed.goingOnGame = this
        }
    }

    val lanes = mutableListOf<LaneInstance>()

    fun addLane(
        g: GenerateSetting,
        members: List<Player> = listOf(),
        generator: AbstractGenerator = StandardGenerator(unrailed),
        trainBuilder: TrainBuilder? = null
    ): LaneInstance? {
        try {
            val startTerrainCenter =
                g.startLocation.copy().add(g.direction.toVector(g.terrainSize.getCenter().toDouble())).toBlockLocation()
            val rail = Rail(startTerrainCenter.block)
            val lane = LaneInstance(
                this,
                trainBuilder.nullMap { DefaultTrainBuilder(unrailed, startTerrainCenter, rail) },
                g,
                generator
            )

            lane.addTeamMember(*(members.map { GamePlayer.getOrRegister(it, this) }.toTypedArray()))
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

    fun getLane(color: WoolColor): LaneInstance? {
        return this.lanes.filter { it.generateSetting.teamColor == color }.getOrNull(0)
    }

    fun getLane(p: Player): LaneInstance? {
        return this.lanes.filter { it.getAllTeamMember().map { g -> g.p }.contains(p) }.getOrNull(0)
    }

    /**
     * @see GamePlayer.getFromPlayer
     */
    val players = mutableListOf<GamePlayer>()

    var nowPhase: Phase = PreparePhase()


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
        nowPhase = GamePhase()
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

    /**
     * すべてのレーンが終了した時の処理
     */
    fun onAllDone() {
        // TODO Clean Up all Lane's team
        nowPhase = EndPhase()
    }
}