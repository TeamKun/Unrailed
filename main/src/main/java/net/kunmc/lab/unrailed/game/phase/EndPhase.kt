package net.kunmc.lab.unrailed.game.phase

/**
 * ゲーム終了後
 */
class EndPhase : Phase {
    override fun next(): Phase? = null
    override fun phaseName(): String = "End"
}