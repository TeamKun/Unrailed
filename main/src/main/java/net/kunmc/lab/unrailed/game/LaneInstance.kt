package net.kunmc.lab.unrailed.game

import net.kunmc.lab.unrailed.game.player.GamePlayer
import net.kunmc.lab.unrailed.generator.GenerateSetting
import net.kunmc.lab.unrailed.train.Train

/**
 * @see GameInstance 内における、一つのレーン(チーム)
 * このレーン増やして同時に対戦できるようにする
 */
class LaneInstance(val game: GameInstance, val train: Train, var generateSetting: GenerateSetting) {
    val teamMember = mutableListOf<GamePlayer>()
}