package net.kunmc.lab.unrailed.gui.util

import net.kunmc.lab.unrailed.game.player.GamePlayer
import org.bukkit.inventory.ItemStack

/**
 * @see net.kunmc.lab.unrailed.gui.UpgradeGUI
 *
 * GUI用に用意するUpgradeable
 *
 * 基本的にGUIにボタンとして表示される
 */
interface Upgradable {
    /**
     * @return 表示されるItemStack
     */
    fun itemStack(gamePlayer: GamePlayer): ItemStack

    /**
     * @return 購入可能かどうか
     */
    fun isEnough(gamePlayer: GamePlayer): Boolean

    /**
     * @return 購入時処理
     */
    fun onBuy(gamePlayer: GamePlayer)
}