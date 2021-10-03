package net.kunmc.lab.unrailed.car

import net.kunmc.lab.unrailed.util.CarInteractEvent
import net.kunmc.lab.unrailed.util.asNotNull
import org.bukkit.Location
import org.bukkit.entity.Minecart
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.plugin.java.JavaPlugin

open class BaseCar(
    val plugin: JavaPlugin,
    spawnLocation: Location,
    private val Name: String,
    carClass: Class<out Minecart> = Minecart::class.java,
    carInit: (Minecart) -> Unit = {}
) :
    AbstractCar() {
    private val mine: Minecart = spawnLocation.world.spawn(spawnLocation, carClass).also {
        it.isSlowWhenEmpty = false
        carInit(it)
    }

    override fun name(): String = Name

    override fun getMinecart(): Minecart = mine

    override fun onInteract(e: CarInteractEvent) {
        // To Override
    }
}