package de.leonheuer.skycave.hardcore.enums

import de.leonheuer.skycave.hardcore.HardcoreSkyblock
import de.leonheuer.skycave.hardcore.models.ChatMessage
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.Contract

enum class Message(private val message: String) {

    // command blocker messages
    COMMAND_BLOCKED("&cDieser Befehl wurde blockiert."),
    COMMAND_CORRECTION("&eDu hast %input eingegeben, aber trotzdem wurde %result ausgeführt."),

    // help command messages
    HELP_HEADER("&eDie wichtigsten Grundbefehle:"),
    HELP_HUB("&a/hub &8» &7Bringt dich zurück in die Lobby."),
    HELP_IS("&a/is &8» &7Teleportiert dich auf deine Insel. Falls du keine hast, wird eine neue erstellt."),
    HELP_SPAWN("&a/spawn &8» &7Teleportiert dich zum Spawn."),
    HELP_MSG("&a/msg <spieler> <nachricht> &8» &7Versendet eine private Nachricht."),
    HELP_WIKI("&eFür eine ausführliche Befehlsübersicht siehe: &b&nhttps://skycave.gitbook.io/skyblock/"),
    ;

    @Contract(" -> new")
    open fun get(): ChatMessage {
        return ChatMessage(JavaPlugin.getPlugin(HardcoreSkyblock::class.java), message)
    }
}