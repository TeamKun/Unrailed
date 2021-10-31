package net.kunmc.lab.unrailed.game

import com.github.bun133.flylib2.utils.ComponentUtils
import net.kunmc.lab.unrailed.game.player.GamePlayer
import net.kunmc.lab.unrailed.generator.AbstractGenerator
import net.kunmc.lab.unrailed.generator.GenerateSetting
import net.kunmc.lab.unrailed.generator.StandardGenerator
import net.kunmc.lab.unrailed.rail.Rail
import net.kunmc.lab.unrailed.station.Station
import net.kunmc.lab.unrailed.train.Train
import net.kunmc.lab.unrailed.train.TrainBuilder
import net.kunmc.lab.unrailed.util.*
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

    // Team名16文字制限に引っかかるので変更
    val team: Team =
        game.unrailed.server.scoreboardManager.mainScoreboard.getOrRegisterTeam("Ur-${generateSetting.teamColor}")
            .setColor(generateSetting.teamColor)
    val scoreboard = game.unrailed.server.scoreboardManager.newScoreboard

    // Non Used Value
    // Can be accessed by Player.scoreboard.getObjective("Ur-GUI-Objective")
    val objective =
        run {
            val obj =
                scoreboard.getObjective("Ur-GUI-Objective")
            obj
                ?: scoreboard.registerNewObjective(
                    "Ur-GUI-Objective",
                    "dummy",
                    ComponentUtils.fromText("GameStatus")
                )
        }
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

    val state: LaneState = LaneState(this)
    /////////// Lane Data ////////////

    fun addTeamMember(g: GamePlayer) {
        teamMember.add(g)
        team.addEntry(g.p.name)
        g.p.scoreboard = scoreboard
        g.p.sendMessage("" + ChatColor.GREEN + "${generateSetting.teamColor.displayName}色チームに参加しました!")
        if (g.p.isLeader()) {
            // Notify player that you are the leader of this lane
            g.p.sendMessage("" + ChatColor.GREEN + "${generateSetting.teamColor.displayName}色チームのリーダーになりました!")
        }
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

    /**
     * @return The First Player of this Team
     * in many cases,this value will not be null
     * this value will be null if teamMember is empty.
     */
    fun getLeader() = teamMember.getOrNull(0)

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
            error("Game Not Reset")
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
     * 駅に着いたときの処理
     */
    fun onArrive(station: Station) {
        debug("Lane ${generateSetting.teamColor.displayName}#onArrive@${station}")

        // Update Station Index
        fromStationIndex = stations!!.indexOf(station) // -1にならないと信じてる
        if (fromStationIndex!! == stations!!.lastIndex) {
            // 最後の駅
            fromStationIndex = null
        }
        toStationIndex = null // -1にならないと信じてる

        // Update Lane Keys & GamePlayer Point
        state.keys += game.gameSetting.keysPerStation
        teamMember.forEach { it.state.points += game.gameSetting.pointPerStation }
        game.onArrive(this, station, game.gameSetting.ticksOfUpgrade)

        // Replace The Train (disabled) (not working)
//        train!!.replace(train!!.getFirstLocation()!!)

        // Timer to Hook Departure
        game.unrailed.server.scheduler.runTaskLater(
            game.unrailed,
            Runnable { onDepart(station) },
            game.gameSetting.ticksOfUpgrade
        )

        // Set State to upgrade time
        state.isUpgradeTime = true

        // TODO Upgrade関係
    }

    /**
     * 駅を出発するときの処理
     */
    fun onDepart(station: Station) {
        debug("Lane ${generateSetting.teamColor.displayName}#onDepart@${station}")
        fromStationIndex = stations!!.indexOf(station) // -1にならないと信じてる
        toStationIndex = fromStationIndex!! + 1 // -1にならないと信じてる

        // Set State to not upgrade time
        state.isUpgradeTime = false

        game.onDepart(this, station)
        train!!.deStop()
    }

    /**
     * このレーンがクリアしたときの処理
     */
    fun onClear(clearLocation: Location) {
        fromStationIndex = null
        toStationIndex = null
        if (tickTask != null) tickTask!!.cancel()
        game.onClear(this, clearLocation)
    }

    /**
     * このレーンが脱落したときの処理
     */
    fun onFail(failLocation: Location) {
        if (tickTask != null) tickTask!!.cancel()
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
                    // 先頭車破壊後
                    // 何もしない
                } else {
                    val firstBlock = firstLoc.block
                    if (station.rail.contain(firstBlock) && station.rail.rails.getCenter()!! == firstBlock && train!!.isMoving) {
                        // 列車が駅の中心に進入
                        debug("Stop Result:${train!!.stop()}")
                        if (toStationIndex!! == stations!!.lastIndex) {
                            // 最終駅到着
                            onClear(train!!.getFirstLocation()!!)
                        } else {
                            onArrive(station)
                        }
                    }
                }
            }
        } else if (fromStationIndex != null) {
            // 停車中
        } else {
            // クリア
        }
    }

    fun dispose() {
        teamMember.forEach {
            it.state.perks.forEach { perk ->
                perk.onDeEffect(it.p)
            }
        }
    }
    //////////////// Life Cycle /////////////////
}


