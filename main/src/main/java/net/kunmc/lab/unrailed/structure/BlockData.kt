package net.kunmc.lab.unrailed.structure

import net.kunmc.lab.unrailed.util.copy
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Server
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.util.Vector

class BlockData(
    val data: BlockData,
    val material: Material,
    var pos: Vector
) {
    constructor(loc: Location, base: Location) : this(loc.block, base)
    constructor(block: Block, base: Location) : this(block.blockData.clone(), block.type, base, block.location)
    constructor(blockData: BlockData, material: Material, base: Location, pos: Location) : this(
        blockData,
        material,
        pos.toVector().add(base.toVector().multiply(-1))
    )

    constructor(materialName: String, blockData: String, server: Server, pos: Vector) : this(
        server.createBlockData(blockData),
        Material.valueOf(materialName),
        pos
    )


    fun copyTo(loc: Location): Block {
        // さてどうなるか
        val l = pos.copy().add(loc.toVector()).toLocation(loc.world)
        l.block.type = material
        l.block.blockData = data
        return l.block
    }

    override fun toString(): String {
        return "material=${material.name},data=${data.asString},pos=[${pos}]"
    }

    fun toConfig(yaml: ConfigurationSection) {
        yaml["material"] = material.name
        yaml["data"] = data.asString
        yaml["pos"] = pos
    }

    fun copy(): net.kunmc.lab.unrailed.structure.BlockData {
        return BlockData(data.clone(), material, pos.copy())
    }
}