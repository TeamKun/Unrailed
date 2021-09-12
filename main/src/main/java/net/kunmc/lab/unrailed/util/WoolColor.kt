package net.kunmc.lab.unrailed.util

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.DyeColor
import org.bukkit.Material

enum class WoolColor(
    val woolMaterial: Material,
    val dyeColor: DyeColor,
    val displayName: String,
    val namedTextColor: NamedTextColor?
) {
    WHITE(Material.WHITE_WOOL, DyeColor.WHITE, "白", NamedTextColor.WHITE),
    ORANGE(Material.ORANGE_WOOL, DyeColor.ORANGE, "橙", null),
    MAGENTA(Material.MAGENTA_WOOL, DyeColor.MAGENTA, "赤紫", NamedTextColor.LIGHT_PURPLE),
    LIGHT_BLUE(Material.LIGHT_BLUE_WOOL, DyeColor.LIGHT_BLUE, "空", NamedTextColor.BLUE),
    YELLOW(Material.YELLOW_WOOL, DyeColor.YELLOW, "黄", NamedTextColor.YELLOW),
    LIME(Material.LIME_WOOL, DyeColor.LIME, "黄緑", NamedTextColor.GREEN),
    PINK(Material.PINK_WOOL, DyeColor.PINK, "桃", NamedTextColor.RED),
    GRAY(Material.GRAY_WOOL, DyeColor.GRAY, "灰", NamedTextColor.DARK_GRAY),
    LIGHT_GRAY(Material.LIGHT_GRAY_WOOL, DyeColor.LIGHT_GRAY, "薄灰", NamedTextColor.GRAY),
    CYAN(Material.CYAN_WOOL, DyeColor.CYAN, "青緑", null),
    PURPLE(Material.PURPLE_WOOL, DyeColor.PURPLE, "紫", NamedTextColor.DARK_PURPLE),
    BLUE(Material.BLUE_WOOL, DyeColor.BLUE, "青", NamedTextColor.DARK_BLUE),
    BROWN(Material.BROWN_WOOL, DyeColor.BROWN, "茶", null),
    GREEN(Material.GREEN_WOOL, DyeColor.GREEN, "緑", NamedTextColor.DARK_GREEN),
    RED(Material.RED_WOOL, DyeColor.RED, "赤", NamedTextColor.DARK_RED),
    BLACK(Material.BLACK_WOOL, DyeColor.BLACK, "黒", NamedTextColor.BLACK);

    fun color() = dyeColor.color

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