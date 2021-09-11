package net.kunmc.lab.unrailed.structure

import net.kunmc.lab.unrailed.util.*
import org.bukkit.Location
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.block.data.Directional
import org.bukkit.block.data.Rail
import org.bukkit.configuration.ConfigurationSection

class BlockSet(private var blocks: List<BlockData>, val direction: Direction? = null) {
    constructor(from: Location, to: Location) : this(Box(from, to))
    constructor(box: Box) : this(processBlocks(box))
    constructor(config: ConfigurationSection, server: Server) : this(fromConfig(config, server))
    constructor(pair: Pair<List<BlockData>, Direction?>) : this(pair.first, pair.second)

    companion object {
        fun processBlocks(box: Box): List<BlockData> {
            return box.getBlocks().map { BlockData(it, box.base) }
        }

        fun fromConfig(yaml: ConfigurationSection, server: Server): Pair<List<BlockData>, Direction?> {
            val direction = yaml.getString("direction").nullOr { Direction.valueOf(it) }

            val list = mutableListOf<BlockData>()
            for (x in yaml.getKeys(false)) {
                val xSection = yaml.getConfigurationSection(x) ?: continue
                for (y in xSection.getKeys(false)) {
                    val ySection = xSection.getConfigurationSection(y) ?: continue
                    for (z in ySection.getKeys(false)) {
                        val zSection = ySection.getConfigurationSection(z) ?: continue
                        val block = loadBlock(zSection, server)
                        if (block != null) list.add(block)
                    }
                }
            }
            return Pair(list, direction)
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

    /**
     * 位置・BlockFaceを回転
     */
    fun rotateAroundX(angle: Double): BlockSet {
        if (angle == 0.0) return this
        blocks.forEach { it.pos.rotateAroundX(angle) }
        normalize()
        return this
    }

    fun rotateAroundY(angle: Double, world: World): BlockSet {
        if (angle == 0.0) return this
        blocks.forEach { it.pos.rotateAroundY(angle) }
        rotateFaceAroundY(angle, world)
        normalize()
        return this
    }

    /**
     * BlockFaceを回転
     */
    fun rotateFaceAroundY(angle: Double, world: World): BlockSet {
        // 何回転するか
        val times = (angle / Math.toRadians(90.0)).toInt()
        this.blocks.forEach {
            if (it.data is Directional) {
                val data = it.data
                val exceptTo = data.facing.rotateAroundY(times)!!
                if (data.faces.contains(exceptTo)) {
                    // その方向に回転可能
                    data.facing = exceptTo
                }
//                block.blockData = data // 必要ない?
            } else if (it.data.material.isRail()) {
                // レールの回転
                val rail = (it.data as Rail)
                rail.shape = rail.shape.rotateRight(times)
            }
        }
        return this
    }

    fun rotateAroundZ(angle: Double): BlockSet {
        if (angle == 0.0) return this
        blocks.forEach { it.pos.rotateAroundZ(angle) }
        normalize()
        return this
    }

    /**
     * 位置を調整するやーつ
     */
    private fun normalize() {
//        blocks.forEach { it.pos = it.pos.toBlockPos() }
//        val minx = blocks.minOf { it.pos.blockX }.toInt()
//        val miny = blocks.minOf { it.pos.blockY }.toInt()
//        val minz = blocks.minOf { it.pos.blockZ }.toInt()
//        blocks.forEach {
//            it.pos = it.pos.add(Vector(-minx, -miny, -minz))
//        }
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

    fun toBox(base: Location): Box {
        return Box(
            base,
            blocks.maxOf { it.pos.blockX } - blocks.minOf { it.pos.blockX } + 1,
            blocks.maxOf { it.pos.blockZ } - blocks.minOf { it.pos.blockZ } + 1,
            blocks.maxOf { it.pos.blockY } - blocks.minOf { it.pos.blockY } + 1
        )
    }

    fun copy(): BlockSet {
        return BlockSet(copyDataDeeply(), direction)
    }

    private fun copyDataDeeply(): MutableList<BlockData> {
        val copied = mutableListOf<BlockData>()
        blocks.forEach {
            copied.add(it.copy())
        }

        return copied
    }
}