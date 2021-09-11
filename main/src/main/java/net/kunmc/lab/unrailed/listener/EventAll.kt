package net.kunmc.lab.unrailed.listener

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.util.registerEvents

/**
 * Listenerをすべて登録するとこ
 */
class EventAll(val unrailed: Unrailed) {
    companion object {
        private var ins: EventAll? = null
        fun getInstance(unrailed: Unrailed): EventAll {
            if (ins != null) return ins!!
            ins = EventAll(unrailed)
            return ins!!
        }

        fun init(unrailed: Unrailed) {
            JumpEvent(unrailed)
        }
    }

    private val listeners = mutableListOf<ListenerBase>()

    fun register(l: ListenerBase) {
        if (!listeners.contains(l)) {
            unrailed.registerEvents(l)
            listeners.add(l)
        }
    }
}