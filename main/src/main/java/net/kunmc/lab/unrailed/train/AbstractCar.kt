package net.kunmc.lab.unrailed.train

import org.bukkit.entity.Minecart
import org.bukkit.event.player.PlayerInteractEntityEvent

/**
 * 車両
 * @see AbstractTrain
 */
abstract class AbstractCar {
    /**
     * @return この車両の表示名
     */
    abstract fun name():String

    /**
     * @return Minecart
     */
    abstract fun getMinecart():Minecart

    /**
     * このトロッコに向かってInteractした時の動作
     * 冷却車など...
     */
    abstract fun onInteract(e:PlayerInteractEntityEvent)
}