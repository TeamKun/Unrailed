package net.kunmc.lab.unrailed.util

import com.github.bun133.flylib2.utils.ComponentUtils
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit

var isBroadCast = true
fun broadCastMessage(str: String) {
    broadCast(str)
}

fun broadCaseMessage(vararg str: String) {
    if (isBroadCast) {
        str.forEach {
            broadCast(it)
        }
    }
}

fun broadCast(component: Component) {
    if (isBroadCast) Bukkit.broadcast(component)
}

fun broadCast(vararg component: Component) {
    if (isBroadCast) {
        component.forEach {
            Bukkit.broadcast(it)
        }
    }
}

fun broadCast(message: String) {
    broadCast(ComponentUtils.fromText(message))
}


var log = false
fun log(message: String) {
    if (log) println(message)
}

fun log(vararg message: String) {
    if (log) {
        message.forEach {
            println(it)
        }
    }
}

var debug = true
fun debug(message: String) {
    if (debug) println(message)
}

fun debug(vararg message: String) {
    if (debug) {
        message.forEach {
            println(it)
        }
    }
}

var info = true
fun info(message: String) {
    if (info) println(message)
}

fun info(vararg message: String) {
    if (info) {
        message.forEach {
            println(it)
        }
    }
}

var error = true
fun error(message: String) {
    if (error) {
        broadCast(message)
        println(message)
    }
}

fun error(vararg message: String) {
    if (error) {
        message.forEach {
            broadCast(it)
            println(it)
        }
    }
}