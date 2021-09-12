package net.kunmc.lab.unrailed.util

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.DyeColor
import org.bukkit.Material
import org.checkerframework.checker.nullness.qual.NonNull

enum class WoolColor(val woolMaterial: Material, val dyeColor: DyeColor) {
    WHITE(Material.WHITE_WOOL, DyeColor.WHITE),
    ORANGE(Material.ORANGE_WOOL, DyeColor.ORANGE),
    MAGENTA(Material.MAGENTA_WOOL, DyeColor.MAGENTA),
    LIGHT_BLUE(Material.LIGHT_BLUE_WOOL, DyeColor.LIGHT_BLUE),
    YELLOW(Material.YELLOW_WOOL, DyeColor.YELLOW),
    LIME(Material.LIME_WOOL, DyeColor.LIME),
    PINK(Material.PINK_WOOL, DyeColor.PINK),
    GRAY(Material.GRAY_WOOL, DyeColor.GRAY),
    LIGHT_GRAY(Material.LIGHT_GRAY_WOOL, DyeColor.LIGHT_GRAY),
    CYAN(Material.CYAN_WOOL, DyeColor.CYAN),
    PURPLE(Material.PURPLE_WOOL, DyeColor.PURPLE),
    BLUE(Material.BLUE_WOOL, DyeColor.BLUE),
    BROWN(Material.BROWN_WOOL, DyeColor.BROWN),
    GREEN(Material.GREEN_WOOL, DyeColor.GREEN),
    RED(Material.RED_WOOL, DyeColor.RED),
    BLACK(Material.BLACK_WOOL, DyeColor.BLACK);

    fun color() = dyeColor.color

    fun toTextColor(): TextColor {
        val color = color()
        return TextColor.color(color.red, color.green, color.blue)
    }

    fun toNamedTextColor(): NamedTextColor {
        return NamedTextColor.nearestTo(toTextColor())
    }

    companion object {
        fun fromMaterial(material: Material): WoolColor? {
            return values().filter { it.woolMaterial == material }.getOrNull(0)
        }
    }
}