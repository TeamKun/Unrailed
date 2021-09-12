package net.kunmc.lab.unrailed.test

import net.kunmc.lab.unrailed.Unrailed
import net.kunmc.lab.unrailed.generator.GenerateSetting
import net.kunmc.lab.unrailed.generator.StandardGenerator
import net.kunmc.lab.unrailed.generator.terrain.generator.BaseTerrainGenerator
import net.kunmc.lab.unrailed.util.*
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import kotlin.random.Random

class GeneratorTest(unrailed: Unrailed) : TestCase(unrailed) {
    companion object {
        private var ins: GeneratorTest? = null
        fun getInstance(unrailed: Unrailed): GeneratorTest {
            if (ins == null) ins = GeneratorTest(unrailed)
            return ins!!
        }
    }

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        if (isGoingOn) {
            if (e.action == Action.RIGHT_CLICK_BLOCK) {
                val clickedBlock = e.clickedBlock!!
                val generator = StandardGenerator(unrailed)
                generator.onGenerate(
                    GenerateSetting(
                        clickedBlock.location,
                        Direction.fromBlockFace(e.player.facing)!!,
                        5,
                        false,
                        Material.BEDROCK,
                        9,
                        Random.nextLong(Long.MIN_VALUE, Long.MAX_VALUE),
                        10,
                        3,
                        WoolColor.BLACK.random(Unrailed.goingOnGame?.lanes?.map { it.generateSetting.teamColor }
                            .nullMap { listOf() })!!
                    )
                ) {}
            }
        }
    }
}