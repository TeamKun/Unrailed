package net.kunmc.lab.unrailed.car.upgrade

/**
 * @param cost アップグレード先のレベル -> アップグレードコスト(ボルト) (アップグレード出来なければ-1)
 * @param tFunction アップグレード先のレベル -> T(何かのステータスやプロパティ)
 * @param description アップグレード先のレベル,T(何かのステータスやプロパティ) -> 説明
 */
open class UpGradeSetting<T>(val cost: (Int) -> Int, val tFunction: (Int) -> T, val description: (Int, T) -> List<String>) {
}