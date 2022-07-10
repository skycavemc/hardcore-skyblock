package de.leonheuer.skycave.hardcore.listeners.player

import de.leonheuer.skycave.hardcore.HardcoreSkyblock
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerLeave(private val main: HardcoreSkyblock) : Listener {

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        event.quitMessage(null)
        Bukkit.getOnlinePlayers().forEach { main.scoreboardManager.removePlayerFromScoreboard(it, event.player) }
    }

    @EventHandler
    fun onPlayerKick(event: PlayerKickEvent) {
        event.leaveMessage(Component.text(""))
    }

}