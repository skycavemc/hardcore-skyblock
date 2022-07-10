package de.leonheuer.skycave.hardcore.listeners.player

import de.leonheuer.skycave.hardcore.HardcoreSkyblock
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.time.Duration

class PlayerJoin(private val main: HardcoreSkyblock) : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        val version = Bukkit.getServer().minecraftVersion
        player.showTitle(Title.title(
            Component.text("§8» §3Willkommen §8«"),
            Component.text("§7auf §cHardcore §7$version"),
            Title.Times.times(
                Duration.ofSeconds(1),
                Duration.ofSeconds(2),
                Duration.ofSeconds(1)
            )
        ))
        player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.7F)
        event.joinMessage(Component.text("§8[§3+§8] §3${player.name} §7hat den Server betreten."))

        var prefix = main.luckPermsAdapter.getPrefix(player)
        if (prefix != null) {
            prefix = ChatColor.translateAlternateColorCodes('&', prefix)
            player.playerListName(Component.text("$prefix §8| §7${player.name}"))
        }

        main.scoreboardManager.initScoreboard(player)
        Bukkit.getOnlinePlayers().forEach {
            if (it != player) {
                main.scoreboardManager.addPlayerToScoreboard(it, player)
            }
        }
    }

}