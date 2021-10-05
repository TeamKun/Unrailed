package net.kunmc.lab.unrailed.train.state

/**
 * StateModifierクラス
 * @param T type of whose state is to modify
 * @param R type of state value
 */
abstract class StateModifier<T, R>(val state: State<T, R>, val id: String) {
    /**
     * @param t instance of whose state is to modify
     * @param beforeValue last modified value to modify
     */
    abstract fun onCalculate(t: T, beforeValue: R): R
}