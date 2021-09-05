package net.kunmc.lab.unrailed.game

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.car.EngineCar
import net.kunmc.lab.unrailed.game.player.GamePlayer
import net.kunmc.lab.unrailed.generator.AbstractGenerator
import net.kunmc.lab.unrailed.generator.GenerateSetting
import net.kunmc.lab.unrailed.generator.StandardGenerator
import net.kunmc.lab.unrailed.rail.Rail
import net.kunmc.lab.unrailed.rail.RailBlockException
import net.kunmc.lab.unrailed.rail.RailException
import net.kunmc.lab.unrailed.train.Train
import net.kunmc.lab.unrailed.util.getCenter

/**
 * 1つのゲームを表すクラス
 */
class GameInstance(val unrailed: Unrailed) {
    val lanes = mutableListOf<LaneInstance>()

    fun addLane(
        g: GenerateSetting,
        members: List<GamePlayer> = listOf(),
        generator: AbstractGenerator = StandardGenerator()
    ): LaneInstance? {
        try {
            val startTerrainCenter = g.startLocation.add(g.direction.toVector(g.terrainSize.getCenter().toDouble()))
            val rail = Rail(startTerrainCenter.block)
            val lane = LaneInstance(
                this,
                Train(EngineCar(unrailed, startTerrainCenter.add(.0, 1.0, .0)), rail, unrailed),
                g,
                generator
            )

            lane.teamMember.addAll(members)

            lanes.add(lane)

            return lane
        } catch (e: RailBlockException) {
            e.print()
            return null
        } catch (e: RailException) {
            e.print()
            return null
        }
    }
}