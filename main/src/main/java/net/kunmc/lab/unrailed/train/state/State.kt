package net.kunmc.lab.unrailed.train.state

/**
 * Stateクラス
 * @param T type of whose state is to modify
 * @param R type of state value
 * @param baseValue default value of state
 */
open class State<T, R>(val t: T, var baseValue: () -> R?) {
    private val modifierList = mutableListOf<StateModifier<T, R>>()
    private var forceModifier: StateModifier<T, R>? = null

    /**
     * @param modifier a modifier which to add
     * @param isStrict if the exception need to be thrown when the id of modifier is duplicated
     *
     * @return success or not
     */
    fun addModifier(modifier: StateModifier<T, R>, isStrict: Boolean = false): Boolean {
        if (modifierList.map { it.id }.contains(modifier.id)) {
            // Duplicated ID
            if (isStrict) {
                throw Exception("In State#addModifier,Duplicated ID:${modifier.id}")
            } else {
                net.kunmc.lab.unrailed.util.error("In State#addModifier,Duplicated ID:${modifier.id}")
            }

            return false
        } else {
            modifierList.add(modifier)
            return true
        }
    }

    fun setForceModifier(modifier: StateModifier<T, R>) {
        forceModifier = modifier
    }

    fun calculate() = get()

    /**
     * @return 1つでもmodifierからnullが帰されればnull
     */
    fun get(): R? {
        var value = baseValue()
        @Suppress("FoldInitializerAndIfToElvis")
        if (value == null) return null
        if (forceModifier != null) {
            value = forceModifier!!.onCalculate(t, value)
        } else {
            for (modifier in modifierList) {
                if (value == null) break
                value = modifier.onCalculate(t, value)
            }
        }
        return value
    }
}