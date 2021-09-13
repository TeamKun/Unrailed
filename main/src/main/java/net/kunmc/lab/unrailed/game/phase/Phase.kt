package net.kunmc.lab.unrailed.game.phase

/**
 * ゲーム進行のフェーズ
 */
interface Phase {
    fun next(): Phase?
    fun phaseName(): String
}