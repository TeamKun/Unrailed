package net.kunmc.lab.unrailed.util

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.md_5.bungee.api.ChatColor
import org.bukkit.Color
import org.bukkit.DyeColor
import org.bukkit.Material

/**
 * @param isSimple それとなく優先的に使ってほしいなぁという色
 */
enum class WoolColor(
    val woolMaterial: Material,
    val dyeColor: DyeColor,
    val displayName: String,
    val namedTextColor: NamedTextColor?,
    val chatColor: ChatColor?,
    val isSimple: Boolean
) {
    WHITE(Material.WHITE_WOOL, DyeColor.WHITE, "白", NamedTextColor.WHITE, ChatColor.WHITE, false),
    ORANGE(Material.ORANGE_WOOL, DyeColor.ORANGE, "橙", null, null, false),
    MAGENTA(Material.MAGENTA_WOOL, DyeColor.MAGENTA, "赤紫", NamedTextColor.LIGHT_PURPLE, ChatColor.LIGHT_PURPLE, false),
    LIGHT_BLUE(Material.LIGHT_BLUE_WOOL, DyeColor.LIGHT_BLUE, "空", NamedTextColor.BLUE, ChatColor.BLUE, false),
    YELLOW(Material.YELLOW_WOOL, DyeColor.YELLOW, "黄", NamedTextColor.YELLOW, ChatColor.YELLOW, true),
    LIME(Material.LIME_WOOL, DyeColor.LIME, "黄緑", NamedTextColor.GREEN, ChatColor.GREEN, true),
    PINK(Material.PINK_WOOL, DyeColor.PINK, "桃", NamedTextColor.RED, ChatColor.RED, true),
    GRAY(Material.GRAY_WOOL, DyeColor.GRAY, "灰", NamedTextColor.DARK_GRAY, ChatColor.DARK_GRAY, false),
    LIGHT_GRAY(Material.LIGHT_GRAY_WOOL, DyeColor.LIGHT_GRAY, "薄灰", NamedTextColor.GRAY, ChatColor.GRAY, true),
    CYAN(Material.CYAN_WOOL, DyeColor.CYAN, "青緑", null, null, false),
    PURPLE(Material.PURPLE_WOOL, DyeColor.PURPLE, "紫", NamedTextColor.DARK_PURPLE, ChatColor.DARK_PURPLE, false),
    BLUE(Material.BLUE_WOOL, DyeColor.BLUE, "青", NamedTextColor.DARK_BLUE, ChatColor.DARK_BLUE, false),
    BROWN(Material.BROWN_WOOL, DyeColor.BROWN, "茶", null, null, false),
    GREEN(Material.GREEN_WOOL, DyeColor.GREEN, "緑", NamedTextColor.DARK_GREEN, ChatColor.DARK_GREEN, false),
    RED(Material.RED_WOOL, DyeColor.RED, "赤", NamedTextColor.DARK_RED, ChatColor.DARK_RED, false),
    BLACK(Material.BLACK_WOOL, DyeColor.BLACK, "黒", NamedTextColor.BLACK, ChatColor.BLACK, false);

    fun color(): Color = dyeColor.color

    fun toTextColor(): TextColor {
        val color = color()
        return TextColor.color(color.red, color.green, color.blue)
    }

    /**
     * @see namedTextColor
     */
    fun toNamedTextColorForce(): NamedTextColor {
        return NamedTextColor.nearestTo(toTextColor())
    }

    companion object {
        fun fromMaterial(material: Material): WoolColor? {
            return values().filter { it.woolMaterial == material }.getOrNull(0)
        }

        fun namedTextColor() = values().filter { it.namedTextColor != null }
    }
}

private fun Color.toAWTColor(): java.awt.Color {
    return java.awt.Color(red, green, blue)
}
