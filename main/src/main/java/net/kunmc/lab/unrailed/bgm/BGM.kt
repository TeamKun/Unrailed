package net.kunmc.lab.unrailed.bgm

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

/**
 * BGMを表現するクラス
 */
class BGM(val soundNotes: List<SoundNote>) {
    fun play(plugin: JavaPlugin, volume: (Float) -> Float = { it }) = BGMPlayer(this, volume)

    companion object {
        /**
         * コンフィグからBGMを読み込みます
         * @param isStrict 1つでも解釈できないものがあった場合にnullを返します
         *
         */
        fun fromConfig(config: ConfigurationSection, isStrict: Boolean): BGM? {
            val notes = mutableListOf<SoundNote>()
            for (key in config.getKeys(false)) {
                val section = config.getConfigurationSection(key)
                if (section != null) {
                    val note = SoundNote.fromConfig(section)
                    if (note != null) {
                        notes.add(note)
                    } else if (isStrict) {
                        return null
                    }
                } else {
                    if (isStrict) return null
                }
            }
            return BGM(notes)
        }
    }
}

class BGMPlayer(val bgm: BGM, var volume: (Float) -> Float = { it }) {
    var playingPosition = -1

    fun playNext(players: () -> List<Player>) {
        playNext(players())
    }

    /**
     * 再生するときに1tickに1回呼ぶべきもの
     */
    fun playNext(players: List<Player>) {
        playingPosition++
        bgm.soundNotes
            .filter { playingPosition == it.shiftedTicks }
            .forEach {
                for (player in players) {
                    it.playAt(player, volume)
                }
            }
    }

    private var task: BukkitTask? = null

    fun startPlaying(plugin: JavaPlugin, players: List<Player>) {
        task = plugin.server.scheduler.runTaskTimer(plugin, Runnable { playNext(players) }, 1, 1)
    }

    fun startPlaying(plugin: JavaPlugin, players: () -> List<Player>) {
        task = plugin.server.scheduler.runTaskTimer(plugin, Runnable { playNext(players) }, 1, 1)
    }

    fun stopPlaying() {
        task?.cancel()
    }
}