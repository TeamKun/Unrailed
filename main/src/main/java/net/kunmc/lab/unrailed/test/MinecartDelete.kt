package net.kunmc.lab.unrailed.test

import net.kunmc.lab.unrailed.Unrailed
import org.bukkit.entity.Minecart
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.vehicle.VehicleDamageEvent

class MinecartDelete(unrailed: Unrailed) : TestCase(unrailed) {
    init {
        unrailed.server.pluginManager.registerEvents(this, unrailed)
    }

    companion object {
        private var ins: MinecartDelete? = null
        fun getInstance(unrailed: Unrailed): MinecartDelete {
            if (ins == null) ins = MinecartDelete(unrailed)
            return ins!!
        }
    }

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        if (isGoingOn) {
            if (e.action == Action.RIGHT_CLICK_BLOCK) {
                val block = e.clickedBlock!!
                val minecart = block.location.world.spawn(block.location, Minecart::class.java)
                minecart.remove()                           // Bukkit制作者、地の果てまでも追いかけてやる。
                minecart.damage = 40.1
                println("minecart.getDamage:${minecart.damage}")
                println("minecart.isDead:${minecart.isDead}")
            }
        }
    }

    @EventHandler
    fun onDamage(e: VehicleDamageEvent) {
        if (isGoingOn) {
            println("DamageAmount:${e.damage}")
        }
    }
}