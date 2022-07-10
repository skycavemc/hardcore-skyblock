package de.leonheuer.skycave.hardcore

import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import de.leonheuer.skycave.hardcore.adapters.LuckPermsAdapter
import de.leonheuer.skycave.hardcore.annotations.CreateDataFolder
import de.leonheuer.skycave.hardcore.annotations.Prefix
import de.leonheuer.skycave.hardcore.listeners.player.PlayerChat
import de.leonheuer.skycave.hardcore.listeners.player.PlayerCommand
import de.leonheuer.skycave.hardcore.listeners.player.PlayerJoin
import de.leonheuer.skycave.hardcore.listeners.player.PlayerLeave
import de.leonheuer.skycave.hardcore.managers.ScoreboardManager
import de.leonheuer.skycave.hardcore.models.SkyCavePlugin
import net.luckperms.api.LuckPerms
import net.milkbowl.vault.economy.Economy

@Prefix("&b&l| &fSky&3Cave &8» ")
@CreateDataFolder
class HardcoreSkyblock : SkyCavePlugin() {

    companion object {
        const val PERMISSION_GROUPS_MAX_WEIGHT = 150
    }

    lateinit var luckPerms: LuckPerms
        private set
    lateinit var economy: Economy
        private set
    lateinit var scoreboardManager: ScoreboardManager
        private set
    lateinit var luckPermsAdapter: LuckPermsAdapter
        private set

    private lateinit var mongoClient: MongoClient

    override fun onEnable() {
        // database
        val clientSettings = MongoClientSettings.builder().build()
        mongoClient = MongoClients.create(clientSettings)
        val database = mongoClient.getDatabase("hardcore")

        // dependencies
        luckPerms = getRegisteredPlugin(LuckPerms::class.java) ?: return
        economy = getRegisteredPlugin(Economy::class.java) ?: return

        // managers
        scoreboardManager = ScoreboardManager(this)
        luckPermsAdapter = LuckPermsAdapter(luckPerms)

        // listeners
        registerEvents(
            PlayerChat(this),
            PlayerCommand(this),
            PlayerJoin(this),
            PlayerLeave(this),
        )
    }

    override fun onDisable() {
        mongoClient.close()
    }

}