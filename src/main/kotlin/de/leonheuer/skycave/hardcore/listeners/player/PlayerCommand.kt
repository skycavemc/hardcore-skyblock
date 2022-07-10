package de.leonheuer.skycave.hardcore.listeners.player

import de.leonheuer.skycave.hardcore.HardcoreSkyblock
import de.leonheuer.skycave.hardcore.enums.BlockedCommand
import de.leonheuer.skycave.hardcore.enums.Message
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class PlayerCommand(private val main: HardcoreSkyblock): Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onCommand(event: PlayerCommandPreprocessEvent) {
        val player = event.player
        val cmd = event.message.split(" ")[0]

        if (cmd.lowercase().startsWith("/help")) {
            event.isCancelled = true
            player.sendMessage("")
            Message.HELP_HEADER.get().send(player, false)
            Message.HELP_HUB.get().send(player, false)
            Message.HELP_SPAWN.get().send(player, false)
            Message.HELP_IS.get().send(player, false)
            Message.HELP_MSG.get().send(player, false)
            Message.HELP_WIKI.get().send(player, false)
            player.sendMessage("")
            return
        }

        if (player.hasPermission("skybee.core.bypass.commands")) {
            return
        }
        if (main.server.commandMap.getCommand(cmd.replaceFirst("/", "")) == null) {
            return
        }

        if (cmd.contains(":")) {
            val partial = cmd.split(":").toTypedArray()
            if (!partial[0].contains(" ")) {
                event.isCancelled = true
                Message.COMMAND_BLOCKED.get().send(player)
            }
        } else {
            for (blocked in BlockedCommand.values()) {
                if (cmd.lowercase().startsWith("/" + blocked.content)) {
                    event.isCancelled = true
                    Message.COMMAND_BLOCKED.get().send(player)
                }
            }
        }
    }

}