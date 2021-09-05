package net.kunmc.lab.unrailed.structure

import net.kunmc.lab.unrailed.Unrailed
import org.bukkit.configuration.file.YamlConfiguration
import java.io.InputStreamReader

class StructureCollection(val unrailed: Unrailed) {
    companion object {
        private var ins: StructureCollection? = null
        fun getInstance(unrailed: Unrailed): StructureCollection {
            if (ins != null) return ins!!
            ins = StructureCollection(unrailed)
            return ins!!
        }
    }

    val stations = Stations(this)

    /**
     * @param fileName .yml除くresourcesからのパス
     * @param configSectionName コンフィグファイル内のパス
     */
    fun loadStructure(fileName: String, configSectionName: String): BlockSet? {
        val config = unrailed.getResource("${fileName}.yml") ?: return null
        val yamlConfig = YamlConfiguration.loadConfiguration(InputStreamReader(config))
        val section = yamlConfig.getConfigurationSection(configSectionName) ?: return null
        return BlockSet(section, unrailed.server)
    }
}

class Stations(private val structureCollection: StructureCollection) {
    val SandStation = structureCollection.loadStructure("SandStation", "SandStation")!!
}