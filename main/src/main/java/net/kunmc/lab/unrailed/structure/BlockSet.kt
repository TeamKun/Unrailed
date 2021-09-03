package net.kunmc.lab.unrailed.structure

import net.kunmc.lab.unrailed.util.Box
import net.kunmc.lab.unrailed.util.getConfigurationSectionOrDefault
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.util.Vector

class BlockSet(private var blocks: List<Pair<BlockData, Vector>>) {
    constructor(from: Location, to: Location) : this(Box(from, to))
    constructor(box: Box) : this(processBlocks(box))

    companion object {
        fun processBlocks(box: Box): List<Pair<BlockData, Vector>> {
            return box.getBlocks()
                .map { Pair(BlockData(it, box.base), it.location.toVector().add(box.base.toVector().multiply(-1))) }
        }
    }

    fun copyTo(loc: Location) {
        blocks.forEach {
            val l = loc.toVector().add(it.second).toLocation(loc.world)
            it.first.copyTo(l)
        }
    }

    fun rotateAroundX(angle: Double) {
        blocks = blocks.map { Pair(it.first, it.second.rotateAroundX(angle)) }
    }

    fun rotateAroundY(angle: Double) {
        blocks = blocks.map { Pair(it.first, it.second.rotateAroundY(angle)) }
    }

    fun rotateAroundZ(angle: Double) {
        blocks = blocks.map { Pair(it.first, it.second.rotateAroundZ(angle)) }
    }

    fun blocks() = blocks.toMutableList()

    fun toConfig(yaml: ConfigurationSection) {
        blocks.forEach {
            val x = yaml.getConfigurationSectionOrDefault("${it.second.blockX}")
            val y = x.getConfigurationSectionOrDefault("${it.second.blockY}")
            val z = y.getConfigurationSectionOrDefault("${it.second.blockZ}")
            it.first.toConfig(z)
        }
    }
}