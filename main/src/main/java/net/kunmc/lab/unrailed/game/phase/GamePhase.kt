package net.kunmc.lab.unrailed.game.phase

/**
 * ゲーム中
 */
class GamePhase:Phase {
    override fun next(): Phase = EndPhase()
    override fun phaseName(): String = "Game"
}