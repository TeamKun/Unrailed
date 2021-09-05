package net.kunmc.lab.unrailed.structure

import net.kunmc.lab.unrailed.util.Box
import net.kunmc.lab.unrailed.util.getConfigurationSectionOrDefault
import net.kunmc.lab.unrailed.util.reset
import org.bukkit.Location
import org.bukkit.Server
import org.bukkit.configuration.ConfigurationSection

class BlockSet(private var blocks: List<BlockData>) {
    constructor(from: Location, to: Location) : this(Box(from, to))
    constructor(box: Box) : this(processBlocks(box))
    constructor(config: ConfigurationSection, server: Server) : this(fromConfig(config, server))

    companion object {
        fun processBlocks(box: Box): List<BlockData> {
            return box.getBlocks().map { BlockData(it, box.base) }
        }

        fun fromConfig(yaml: ConfigurationSection, server: Server): List<BlockData> {
            val list = mutableListOf<BlockData>()
            for (x in yaml.getKeys(false)) {
                val xSection = yaml.getConfigurationSection(x)!!
                for (y in xSection.getKeys(false)) {
                    val ySection = xSection.getConfigurationSection(y)!!
                    for (z in ySection.getKeys(false)) {
                        val zSection = ySection.getConfigurationSection(z)!!
                        val block = loadBlock(zSection, server)
                        if (block != null) list.add(block)
                    }
                }
            }
            return list
        }

        private fun loadBlock(yaml: ConfigurationSection, server: Server): BlockData? {
            try {
                val vector = yaml.getVector("pos")!!
                val materialName = yaml.getString("material")!!
                val dataString = yaml.getString("data")!!
                return BlockData(materialName, dataString, server, vector)
            } catch (e: NullPointerException) {
                // 握りつぶします
            } catch (e: IllegalArgumentException) {
                // MaterialがEnumに存在しない(握りつぶします)
            }
            return null
        }
    }

    fun copyTo(loc: Location) {
        blocks.forEach {
            it.copyTo(loc)
        }
    }

    fun rotateAroundX(angle: Double) {
        blocks.forEach { it.pos.rotateAroundX(angle) }
    }

    fun rotateAroundY(angle: Double) {
        blocks.forEach { it.pos.rotateAroundY(angle) }
    }

    fun rotateAroundZ(angle: Double) {
        blocks.forEach { it.pos.rotateAroundZ(angle) }
    }

    fun blocks() = blocks.toMutableList()

    /**
     * @param isClear ConfigurationSectionをクリアするかどうか
     */
    fun toConfig(yaml: ConfigurationSection, isClear: Boolean = true) {
        yaml.reset()
        blocks.forEach {
            val x = yaml.getConfigurationSectionOrDefault("${it.pos.blockX}")
            val y = x.getConfigurationSectionOrDefault("${it.pos.blockY}")
            val z = y.getConfigurationSectionOrDefault("${it.pos.blockZ}")
            it.toConfig(z)
        }
    }
}