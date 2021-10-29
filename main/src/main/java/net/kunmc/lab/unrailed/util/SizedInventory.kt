package net.kunmc.lab.unrailed.util

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.graalvm.compiler.graph.IterableNodeType
import kotlin.math.ceil
import kotlin.math.min

/**
 * define limited inventory
 * can be exported to Bukkit#Inventory
 * @param size maximum number of stacks to accept to add to this inventory
 */
class SizedInventory(var size: Int) {
    private var stacks = mutableListOf<ItemStack?>()

    /**
     * set stack at exact slot of this inventory
     * @param index index which the stack should be set to
     * @param stack stack to set
     * @param isOverwrite if allow over write
     *
     * @return if the operation is completed
     */
    fun setStackAt(index: Int, stack: ItemStack?, isOverwrite: Boolean = false): Boolean {
        if (isOverwrite) {
            stacks[index] = stack
            return true
        } else {
            val s = stacks[index]
            if (s != null) return false
            else stacks[index] = stack;return true
        }
    }

    /**
     * Add Stack to Inventory
     * @return if The operation is Completed
     * @note this function returns false when the amount of stacks exceeds the limit
     */
    fun addStack(stack: ItemStack): Boolean {
        if (stacks.size >= size) {
            return false
        }
        stacks.add(stack)
        sort()
        return true
    }

    /**
     * @return if The operation is Completed
     * @note this function returns false when any operation caused false result
     */
    fun addStacks(vararg stack: ItemStack): Boolean {
        return !stack.any {
            !addStack(it)
        }
    }

    fun getStackAt(index: Int) = stacks[index]

    /**
     * Copy to Bukkit#Inventory
     * @note this function do Force Overwrite
     */
    fun copyTo(inventory: Inventory) {
        for (index in 0..min(inventory.size - 1, stacks.size - 1)) {
            inventory.setItem(index, stacks[index])
        }
    }

    /**
     * Copy to SizedInventory
     * @note this function do Force Overwrite
     */
    fun copyTo(inventory: SizedInventory) {
        for (index in 0..min(inventory.size - 1, stacks.size - 1)) {
            inventory.setStackAt(index, stacks[index])
        }
    }

    /**
     * @return all amount of this inventory
     */
    fun count(): MutableMap<ItemStack, Int> {
        val counts = mutableMapOf<ItemStack, Int>()
        stacks.filterNotNull().forEach {
            val matched = counts.toList().filter { m -> m.first.isSimilar(it) }
            if (matched.isNotEmpty()) {
                if (matched.size == 1) {
                    counts[matched[0].first] = matched[0].second + it.amount
                } else {
                    error("in SizedInventory#sort not expected")
                }
            } else {
                counts[it] = it.amount
            }
        }

        return counts
    }

    /**
     * Sort this inventory
     * If you call this function,this inventory \'s item stacks will be arranged/organized
     */
    fun sort() {
        val count = count()
        var size = 0
        count.forEach { (t, u) ->
            size += ceil(u / t.maxStackSize.toDouble()).toInt()
        }

        val inventory = SizedInventory(size)

        count.forEach { t, u ->
            var rest = u
            while (rest > 0) {
                inventory.addStack(t.clone().also { it.amount = min(rest, it.maxStackSize) })
            }
        }

        this.stacks = inventory.stacks
    }

    fun getAll() = stacks.toMutableList()
}