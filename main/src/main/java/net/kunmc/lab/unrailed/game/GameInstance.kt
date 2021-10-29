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
import net.kunmc.lab.unrailed.hud.HUDManager
import net.kunmc.lab.unrailed.rail.Rail
import net.kunmc.lab.unrailed.rail.RailBlockException
import net.kunmc.lab.unrailed.rail.RailException
import net.kunmc.lab.unrailed.station.Station
import net.kunmc.lab.unrailed.train.DefaultTrainBuilder
import net.kunmc.lab.unrailed.train.Train
import net.kunmc.lab.unrailed.train.TrainBuilder
import net.kunmc.lab.unrailed.util.*
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * 1つのゲームを表すクラス
 */
class GameInstance(val unrailed: Unrailed, val gameSetting: GameSetting) {
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

    /**
     * レーンのゲーム結果を保存
     * Int?
     * ゲーム中 -> null
     * ゲーム終了 -> 到達距離
     */
    var laneResult = mutableMapOf<LaneInstance, Int?>()

    fun addLane(
        g: GenerateSetting,
        members: List<Player> = listOf(),
        generator: AbstractGenerator = StandardGenerator(unrailed),
        trainBuilder: TrainBuilder? = null
    ): LaneInstance? {
        try {
            val startTerrainCenter =
                g.startLocation.copy().add(g.direction.toVector(g.terrainSize.getCenter().toDouble())).toBlockLocation()
            val rail = Rail(startTerrainCenter.copy().add(.0, 1.0, .0).block)
            val lane = LaneInstance(
                this,
                trainBuilder.nullMap { DefaultTrainBuilder(unrailed, startTerrainCenter, rail) },
                g,
                generator,
                rail
            )

            val gamePlayers = members.map { GamePlayer.getOrRegister(it, this) }.toTypedArray()
            gamePlayers.forEach {
                // ほかのレーンから削除してない
                it.getLane()?.removeTeamMember(it)
            }

            lane.addTeamMember(*gamePlayers)
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

    fun getLane(train: Train): LaneInstance? {
        return this.lanes.filter { it.train == train }.getOrNull(0)
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
            lane.getAllTeamMember().forEach {
                // Make sure all player's inventory is clear
                // so everyone will not have external tools or items
                it.p.inventory.clear()
            }
            debug("Lane Station Size:${lane.stations!!.size}")
        }
        // Status Reset
        laneResult = mutableMapOf()
        lanes.forEach { laneResult[it] = null }

        // Init HUDs
        hudManager.init()

        // Pass to #startMoving
        unrailed.server.scheduler.runTaskLater(unrailed, Runnable { startMoving() }, gameSetting.ticksOfStarting)
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
     * このゲーム内のレーンが駅に着いた時の処理
     */
    fun onArrive(lane: LaneInstance, station: Station) {
        log("GameInstance ${lane.generateSetting.teamColor.displayName}#onArrive@${station}")
    }

    /**
     * このゲーム内のレーンが駅を出発する時の処理
     */
    fun onDepart(lane: LaneInstance, station: Station) {
        log("GameInstance ${lane.generateSetting.teamColor.displayName}#onDepart@${station}")
    }

    /**
     * このゲーム内のレーンがクリアしたときの処理
     */
    fun onClear(lane: LaneInstance, clearLocation: Location) {
        val blocks = clearLocation.toVector().distance(lane.generateSetting.startLocation.toVector()).toInt()
        broadCaseMessage("${lane.generateSetting.teamColor.chatColor}${lane.generateSetting.teamColor.displayName}色${ChatColor.RESET}チーム クリア!")
        broadCaseMessage("記録:${blocks}ブロック")
        laneResult[lane] = blocks
        checkAllResult()
    }

    /**
     * このゲーム内のレーンが脱落した時の処理
     */
    fun onFail(lane: LaneInstance, failLocation: Location) {
        val blocks = failLocation.toVector().distance(lane.generateSetting.startLocation.toVector()).toInt()
        broadCaseMessage("${lane.generateSetting.teamColor.chatColor}${lane.generateSetting.teamColor.displayName}色${ChatColor.RESET}チーム 脱落!")
        broadCaseMessage("記録:${blocks}ブロック")
        laneResult[lane] = blocks
        checkAllResult()
    }

    /**
     * check whether all lane is done.
     */
    private fun checkAllResult() {
        if (laneResult.all { it.value != null }) {
            onAllDone()
        }
    }

    /**
     * すべてのレーンが終了した時の処理
     */
    fun onAllDone() {
        nowPhase = EndPhase()
        broadCaseMessage("すべてのチームが終了しました")
        broadCaseMessage("-------- 結果発表 --------")
        val sortedMap = laneResult.toList().sortedBy { it.second.nullMap { -1 } }.toMap()
        sortedMap.toList().forEachIndexed { index, pair ->
            broadCaseMessage("${index + 1}位 ${pair.first.generateSetting.teamColor.chatColor}${pair.first.generateSetting.teamColor.displayName}色${ChatColor.RESET}チーム 記録:${pair.second.nullMap { -1 }}ブロック")
        }
    }


    /**
     * Dispose this GameInstance
     */
    fun dispose() {
        // TODO Call this function when regenerate gameInstance
        hudManager.dispose()
    }


    //////////////// HUD ///////////////
    val hudManager = HUDManager(unrailed, this)
}