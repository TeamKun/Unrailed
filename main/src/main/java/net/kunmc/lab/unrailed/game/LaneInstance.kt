package net.kunmc.lab.unrailed.game

import net.kunmc.lab.unrailed.game.player.GamePlayer
import net.kunmc.lab.unrailed.generator.AbstractGenerator
import net.kunmc.lab.unrailed.generator.GenerateSetting
import net.kunmc.lab.unrailed.train.Train
import net.kunmc.lab.unrailed.train.TrainBuilder
import net.kunmc.lab.unrailed.util.WoolColor
import org.bukkit.Color
import org.bukkit.scheduler.BukkitTask


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
    val teamMember = mutableListOf<GamePlayer>()
    var train: Train? = null
    var tickTask: BukkitTask? = null

    /**
     * @memo 絶対重い
     * @param logCallBack Double:進行度
     */
    fun generateAll(logCallBack: (Double) -> Unit = {}) {
        generator.onGenerate(generateSetting, logCallBack)
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
        // TODO 最初にちょこっと押してあげないとだめ
    }

    /**
     * このレーンがクリアしたときの処理
     */
    fun onClear() {
        train!!.isMoving = false
        game.onClear(this)
    }

    /**
     * このレーンが脱落したときの処理
     */
    fun onFail() {
        game.onFail(this)
    }

    fun tick() {
        // TODO State Update
    }
}