package net.kunmc.lab.unrailed.game.phase

/**
 * ゲーム開始前の待機時間
 */
class PreparePhase : Phase {
    override fun next(): Phase = GamePhase()
    override fun phaseName(): String = "Prepare"
}