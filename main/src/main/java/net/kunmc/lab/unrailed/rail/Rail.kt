package net.kunmc.lab.unrailed.rail

import org.bukkit.Location
import org.bukkit.block.Block

class Rail(val first: Block): AbstractRail() {
    val rails = mutableListOf<Block>()

    override fun getAll(): MutableList<Block> = rails.toMutableList()

    override fun add(block: Block): Boolean {
        return if(Rails.contains(block.type)){
            if(joinOf(block.location)){
                // このレール郡と引数のレールが接続できる
                rails.add(block)
                true
            }else{
                // できない
                false
            }
        }else{
            // そもそもレールじゃない
            false
        }
    }

    override fun joinOf(loc: Location): Boolean {
        TODO("Not yet implemented")
    }

    init{
        add(first)
    }
}