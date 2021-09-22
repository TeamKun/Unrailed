package net.kunmc.lab.unrailed.game

import net.kunmc.lab.unrailed.game.player.GamePlayer
import net.kunmc.lab.unrailed.generator.AbstractGenerator
import net.kunmc.lab.unrailed.generator.GenerateSetting
import net.kunmc.lab.unrailed.station.Station
import net.kunmc.lab.unrailed.train.Train
import net.kunmc.lab.unrailed.train.TrainBuilder
import net.kunmc.lab.unrailed.util.WoolColor
import net.kunmc.lab.unrailed.util.getOrRegisterTeam
import net.kunmc.lab.unrailed.util.setColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.ChatColor
import org.bukkit.Color
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
) {
    /////////// Lane Data ////////////
    private val teamMember = mutableListOf<GamePlayer>()
    val team: Team =
        game.unrailed.server.scoreboardManager.mainScoreboard.getOrRegisterTeam("Unrailed-${generateSetting.teamColor}")
            .setColor(generateSetting.teamColor)
    var train: Train? = null
    var tickTask: BukkitTask? = null
    var stations:MutableList<Station>? = null
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

    /**
     * @memo 絶対重い
     * @param logCallBack Double:進行度
     */
    fun generateAll(logCallBack: (Double) -> Unit = {}) {
        generator.onGenerate(generateSetting, logCallBack)
        //TODO Collect Stations to stations(mutable list)
    }

    /**
     * このレーンに置ける開始処理
     * @see GameInstance.start
     */
    fun start() {
        if (train != null) {
            println("Game Not Reset")
            train!!.removeAll()
        }
        train = trainBuilder.onBuild(generateSetting.startLocation, generateSetting.direction)

        // Tick Task
        if (tickTask != null) tickTask!!.cancel()
        tickTask = game.unrailed.server.scheduler.runTaskTimer(game.unrailed, Runnable { tick() }, 1, 1)
    }

    /**
     * このレーンの列車を動かし始める処理
     * @see GameInstance.startMoving
     */
    fun startMoving() {
        train!!.isMoving = true
        train!!.applyVectors(generateSetting.direction.toVector(0.2))
    }

    /**
     * このレーンがクリアしたときの処理
     */
    fun onClear(clearLocation: Location) {
        train!!.isMoving = false
        game.onClear(this,clearLocation)
    }

    /**
     * このレーンが脱落したときの処理
     */
    fun onFail(failLocation: Location) {
        game.onFail(this,failLocation)
    }

    fun tick() {
        // TODO State Update
        // TODO 駅への接続確認
    }
}


