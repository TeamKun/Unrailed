package net.kunmc.lab.unrailed.train

import org.bukkit.Location
import org.bukkit.entity.Minecart
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.plugin.java.JavaPlugin

class BaseCar(
    val plugin: JavaPlugin,
    spawnLocation: Location,
    private val Name: String,
    carClass: Class<Minecart> = Minecart::class.java
) :
    AbstractCar() {
    private val mine: Minecart = spawnLocation.world.spawn(spawnLocation, carClass)

    override fun name(): String = Name

    override fun getMinecart(): Minecart = mine

    override fun onInteract(e: PlayerInteractEntityEvent) {
        // To Override
    }
}