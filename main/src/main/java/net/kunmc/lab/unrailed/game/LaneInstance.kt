package net.kunmc.lab.unrailed.game

import net.kunmc.lab.unrailed.game.player.GamePlayer
import net.kunmc.lab.unrailed.generator.AbstractGenerator
import net.kunmc.lab.unrailed.generator.GenerateSetting
import net.kunmc.lab.unrailed.generator.StandardGenerator
import net.kunmc.lab.unrailed.rail.Rail
import net.kunmc.lab.unrailed.station.Station
import net.kunmc.lab.unrailed.train.Train
import net.kunmc.lab.unrailed.train.TrainBuilder
import net.kunmc.lab.unrailed.util.broadCaseMessage
import net.kunmc.lab.unrailed.util.debug
import net.kunmc.lab.unrailed.util.getOrRegisterTeam
import net.kunmc.lab.unrailed.util.setColor
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import org.bukkit.scoreboard.Team

/**
 * @see GameInstance 内における、一つのレーン(チーム)
 * このレーン増やして同時に対戦できるようにする
 */
class LaneInstance(
    val game: GameInstance,
    val trainBuilder: TrainBuilder,
    var generateSetting: GenerateSetting,
    val generator: AbstractGenerator,
    val rail: Rail
) {
    /////////// Lane Data ////////////
    private val teamMember = mutableListOf<GamePlayer>()
    val team: Team =
        game.unrailed.server.scoreboardManager.mainScoreboard.getOrRegisterTeam("Unrailed-${generateSetting.teamColor}")
            .setColor(generateSetting.teamColor)
    var train: Train? = null
    var tickTask: BukkitTask? = null
    var stations: MutableList<Station>? = null

    /**
     *  移動中 -> 両方 not null
     *  停車中 -> fromStationはnot null toStationはnull
     *  クリア -> 両方 null
     *  失敗 -> 直前の内容を保持
     */
    var fromStationIndex: Int? = null // 出発した駅のIndex
    var toStationIndex: Int? = null // 到着予定の駅のIndex
    /////////// Lane Data ////////////

    fun addTeamMember(g: GamePlayer) {
        teamMember.add(g)
        team.addEntry(g.p.name)
        g.p.sendMessage("" + ChatColor.GREEN + "${generateSetting.teamColor.displayName}色チームに参加しました!")
    }

    fun addTeamMember(vararg g: GamePlayer) {
        g.forEach { addTeamMember(it) }
    }

    fun removeTeamMember(g: GamePlayer): Boolean {
        if (teamMember.contains(g)) {
            teamMember.remove(g)
            team.removeEntry(g.p.name)
            return true
        } else return false
    }

    fun removeTeamMember(p: Player): Boolean {
        val g = getAllTeamMember().filter { it.p == p }.getOrNull(0)
        if (g == null) return false
        else return removeTeamMember(g)
    }

    fun getAllTeamMember() = teamMember.toMutableList()

    //////////////// Life Cycle /////////////////


    /**
     * @memo 絶対重い
     * @param logCallBack Double:進行度
     */
    fun generateAll(logCallBack: (Double) -> Unit = {}) {
        generator.onGenerate(generateSetting, logCallBack)
        rail.recognizeFrom(direction = generateSetting.direction)    // 地形生成後再探索実施 (駅のレールが認識されていない状態で開始してた)
        stations =
            ((generator as StandardGenerator).getAllStationsBlock(generateSetting)
                .mapNotNull { Station.generateFrom(it, generateSetting.direction) }
                .toMutableList())
    }

    /**
     * このレーンに置ける開始処理
     * @see GameInstance.start
     */
    fun start() {
        if (train != null) {
            net.kunmc.lab.unrailed.util.error("Game Not Reset")
            train!!.removeAll()
        }
        train = trainBuilder.onBuild(generateSetting.startLocation, generateSetting.direction)

        // Tick Task
        if (tickTask != null) tickTask!!.cancel()
        tickTask = game.unrailed.server.scheduler.runTaskTimer(game.unrailed, Runnable { tick() }, 1, 1)
        // 最初のIndexセット
        fromStationIndex = 0
        toStationIndex = 1
    }

    /**
     * このレーンの列車を動かし始める処理
     * @note stationIndex処理忘れずに
     * @see GameInstance.startMoving
     */
    fun startMoving() {
        train!!.isMoving = true
        train!!.applyVectors(generateSetting.direction.toVector(0.2))
    }

    /**
     * 駅に突いたときの処理
     */
    fun onArrive(station: Station) {
        fromStationIndex = stations!!.indexOf(station) // -1にならないと信じてる
        if (fromStationIndex!! == stations!!.lastIndex) {
            // 最後の駅
            fromStationIndex = null
        }
        toStationIndex = null // -1にならないと信じてる
        game.onArrive(this, station)
    }

    /**
     * 駅を出発するときの処理
     */
    fun onDepart(station: Station) {
        fromStationIndex = stations!!.indexOf(station) // -1にならないと信じてる
        toStationIndex = fromStationIndex!! + 1 // -1にならないと信じてる
        game.onDepart(this, station)
    }

    /**
     * このレーンがクリアしたときの処理
     */
    fun onClear(clearLocation: Location) {
        train!!.isMoving = false
        fromStationIndex = null
        toStationIndex = null
        game.onClear(this, clearLocation)
    }

    /**
     * このレーンが脱落したときの処理
     */
    fun onFail(failLocation: Location) {
        game.onFail(this, failLocation)
    }

    fun tick() {
        // TODO State Update
        if (toStationIndex != null) {
            // 移動中
            val station = stations!![toStationIndex!!]
            if (station.isConnected(rail)) {
                val firstLoc = train!!.getFirstLocation()
                if (firstLoc == null) {
                    // TODO 先頭車破壊後
                } else {
                    val firstBlock = firstLoc.block
                    if (station.rail.contain(firstBlock)) {
                        // 列車が駅に進入
                        // TODO アニメーション
                    } else {
                        // 列車が駅に進入する前
                        // (加速?)
                        // TODO アニメーション
                    }
                }
            }
        } else if (fromStationIndex != null) {
            // 停車中
        } else {
            // クリア
        }
    }
    //////////////// Life Cycle /////////////////
}


