package net.kunmc.lab.unrailed.car

import org.bukkit.entity.Minecart
import org.bukkit.event.player.PlayerInteractEntityEvent

/**
 * ワゴン
 * @see AbstractTrain
 */
abstract class AbstractCar {
    /**
     * @return このワゴンの表示名
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