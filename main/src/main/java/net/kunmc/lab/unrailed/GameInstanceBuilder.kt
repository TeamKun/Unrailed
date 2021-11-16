package net.kunmc.lab.unrailed

import net.kunmc.lab.unrailed.game.GameInstance
import net.kunmc.lab.unrailed.game.GameSetting
import net.kunmc.lab.unrailed.generator.AbstractGenerator
import net.kunmc.lab.unrailed.generator.GenerateSetting
import net.kunmc.lab.unrailed.generator.StandardGenerator
import net.kunmc.lab.unrailed.train.TrainBuilder
import org.bukkit.entity.Player

class GameInstanceBuilder(val unrailed: Unrailed) {
    var gameSetting = GameSetting()
    val lanes = mutableListOf<AddLaneData>()

    fun generate(): GameInstance {
        return GameInstance(unrailed, gameSetting).also { g ->
            lanes.forEach {
                g.addLane(it.g, it.members, it.generator, it.trainBuilder)
            }
        }
    }

    fun addLane(
        g: GenerateSetting,
        members: List<Player> = listOf(),
        generator: AbstractGenerator = StandardGenerator(unrailed),
        trainBuilder: TrainBuilder? = null
    ) {
        lanes.add(AddLaneData(g, members, generator, trainBuilder))
    }
}

data class AddLaneData(
    val g: GenerateSetting,
    val members: List<Player>,
    val generator: AbstractGenerator,
    val trainBuilder: TrainBuilder?
)