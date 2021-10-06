package net.kunmc.lab.unrailed

import com.github.bun133.flylib2.commands.Commander
import com.github.bun133.flylib2.commands.CommanderBuilder
import com.github.bun133.flylib2.commands.TabChain
import net.kunmc.lab.unrailed.util.debug

val upgradeCommand: (Unrailed) -> Commander<Unrailed> = { unrailed ->
    Commander(
        unrailed, "Upgrade Command for Game", "/<command>",
        CommanderBuilder<Unrailed>()
            .addTabChain(TabChain())
            .setInvoker { unrailed, commandSender, strings ->
                // TODO Upgrade Command処理
                debug("UG")
                return@setInvoker true
            }
    )
}