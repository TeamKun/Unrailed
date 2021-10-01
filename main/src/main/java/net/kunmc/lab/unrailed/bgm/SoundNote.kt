package net.kunmc.lab.unrailed.bgm

import net.kunmc.lab.unrailed.util.nullMap
import net.kunmc.lab.unrailed.util.asNotNull
import net.kunmc.lab.unrailed.util.safeValueOf
import org.bukkit.Instrument
import org.bukkit.Note
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player

/**
 * BGMの一音を表すインターフェース
 */
interface SoundNote {
    // BGMの先頭から何tick目から再生されるか
    val shiftedTicks: Int

    /**
     * プレーイヤーに対して再生
     * @param volume 素のボリュームが与えられるのでそれを変更する形でボリューム指定
     */
    fun playAt(player: Player, volume: (Float) -> Float = { it })

    fun toConfig(configSection: ConfigurationSection)

    companion object {
        fun fromConfig(configSection: ConfigurationSection): SoundNote? {
            return when (configSection.getString("type")) {
                "NoteSoundNote" -> NoteSoundNote.fromConfig(configSection)
                "SoundSoundNote" -> SoundSoundNote.fromConfig(configSection)
                else -> null
            }
        }
    }
}

/**
 * 音符ブロックのノーツ
 * @note volumeはこれには機能しません
 */
class NoteSoundNote(override val shiftedTicks: Int, private val note: Note, private val instrument: Instrument) :
    SoundNote {
    override fun playAt(player: Player, volume: (Float) -> Float) {
        player.playNote(player.location, instrument, note)
    }

    override fun toConfig(configSection: ConfigurationSection) {
        configSection["type"] = "NoteSoundNote"
        configSection["shiftedTicks"] = shiftedTicks
        configSection["note"] = noteToString(note)
        configSection["instrument"] = instrument
    }

    companion object {
        fun fromConfig(configSection: ConfigurationSection): NoteSoundNote? {
            val type = configSection.getString("type")
            val shiftedTicks = configSection.getInt("shiftedTicks", -1)
            val note = noteFromString(configSection.getString("note").nullMap { "" })
            val instrument = Instrument.BANJO.safeValueOf(configSection.getString("instrument").nullMap { "" })

            return if (type == "NoteSoundNote" && shiftedTicks != -1 && note != null && instrument != null) {
                NoteSoundNote(shiftedTicks, note, instrument)
            } else null
        }
    }
}

/**
 * Sound用
 */
class SoundSoundNote(
    override val shiftedTicks: Int,
    private val sound: Sound,
    private val volume: Float,
    private val category: SoundCategory = SoundCategory.MUSIC
) :
    SoundNote {
    override fun playAt(player: Player, volume: (Float) -> Float) {
        player.playSound(player.location, sound, category, volume(this.volume), 0.0f)
    }

    override fun toConfig(configSection: ConfigurationSection) {
        configSection["type"] = "NoteSoundNote"
        configSection["shiftedTicks"] = shiftedTicks
        configSection["sound"] = sound
        configSection["volume"] = volume
        configSection["category"] = category
    }

    companion object {
        fun fromConfig(configSection: ConfigurationSection): SoundSoundNote? {
            if (configSection["type"] != "NoteSoundNote") return null
            val shiftedTicks = configSection.getInt("shiftedTicks", -1)
            val sound =
                Sound.AMBIENT_BASALT_DELTAS_ADDITIONS.safeValueOf(configSection.getString("sound").nullMap { "" })
            val volume = configSection.getString("volume").nullMap { "" }.toFloatOrNull()
            val category = SoundCategory.MASTER.safeValueOf(configSection.getString("category").nullMap { "" })

            if (shiftedTicks != -1 && sound != null && volume != null && category != null) {
                return SoundSoundNote(shiftedTicks, sound, volume, category)
            } else {
                return null
            }
        }
    }
}

val notePatten = """Note\{[GABCDEF]#?,[012]}""".toRegex()

fun noteFromString(string: String): Note? {
    if (string.isEmpty()) return null
    if (notePatten.matches(string)) {
        var toneString = string.substring(5..6)
        val isSharped = !toneString.endsWith(',')
        toneString = toneString[0].toString()
        val tone = Note.Tone.A.safeValueOf(toneString)
        val octave = (if (isSharped) {
            string.substring(8)
        } else {
            string.substring(7)
        }).toIntOrNull()
        if (tone != null && octave != null) {
            return Note(octave, tone, isSharped)
        } else {
            return null
        }
    } else {
        return null
    }
}

fun noteToString(note: Note): String {
    return "Note{${note.tone}${if (note.isSharped) "#" else ""},${note.octave}}"
}