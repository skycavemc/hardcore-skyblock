package de.leonheuer.skycave.hardcore.managers

import de.leonheuer.skycave.hardcore.HardcoreSkyblock
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard
import java.util.*

class ScoreboardManager(private val main: HardcoreSkyblock) {

    fun initScoreboard(player: Player) {
        val board = createNewBoard()

        val money = board.registerNewTeam("money")
        money.addEntry("§1")
        money.prefix(Component.text("§r §7 §r §f${getFormattedMoney(player)}$"))

        val vc = board.registerNewTeam("vc")
        vc.addEntry("§3")
        vc.prefix(Component.text("§r §7 §r §f0")) //TODO

        val online = board.registerNewTeam("online")
        online.addEntry("§5")
        online.prefix(Component.text("§r §7 §r §f${Bukkit.getOnlinePlayers().size} §7/ §f" +
                Bukkit.getMaxPlayers()))

        player.scoreboard = board
    }

    private fun createNewBoard() : Scoreboard {
        val board = Bukkit.getScoreboardManager().newScoreboard
        val lpa = main.luckPermsAdapter

        main.luckPerms.groupManager.loadedGroups.forEach {
            var prefix = it.cachedData.metaData.prefix ?: return@forEach
            prefix = prefix.replace("&", "§")
            board.registerNewTeam(lpa.getGroupTabListName(it))
                .prefix(Component.text("$prefix §8| §7"))
        }
        Bukkit.getOnlinePlayers().forEach {
            val group = lpa.getUserGroup(it)
            if (group != null) {
                val team = board.getTeam(lpa.getGroupTabListName(group)) ?: return@forEach
                team.addPlayer(it)
            }
        }

        val obj = board.registerNewObjective("ScoreBoard", "dummy",
            Component.text("§f §r §f §r §f§lSky§3§lCave§b§l.de §r §f §r "))
        obj.displaySlot = DisplaySlot.SIDEBAR

        obj.getScore("§0").score = 9
        obj.getScore("§7§l▸ §3Geld:").score = 8
        obj.getScore("§1").score = 7
        obj.getScore("§2").score = 6
        obj.getScore("§7§l▸ §3VoteCoins:").score = 5
        obj.getScore("§3").score = 4
        obj.getScore("§4").score = 3
        obj.getScore("§7§l▸ §3Online:").score = 2
        obj.getScore("§5").score = 1
        obj.getScore("§6").score = 0

        return board
    }

    fun updateScoreBoard(player: Player) {
        val board = player.scoreboard

        val money = board.getTeam("money")
        money?.prefix(Component.text("§r §7 §r §f${getFormattedMoney(player)}$"))

        val vc = board.getTeam("vc")
        vc?.prefix(Component.text("§r §7 §r §f0")) //TODO

        val online = board.getTeam("online")
        online?.prefix(Component.text("§r §7 §r §f${Bukkit.getOnlinePlayers().size} §7/ §f" +
                Bukkit.getMaxPlayers()))
    }

    private fun getFormattedMoney(player: Player): String {
        return String.format(Locale.GERMAN, "%,.2f", main.economy.getBalance(player))
    }

    @Suppress("Deprecation")
    fun addPlayerToScoreboard(holder: Player, newPlayer: Player) {
        val board = holder.scoreboard
        val lpa = main.luckPermsAdapter
        val group = lpa.getUserGroup(newPlayer)
        if (group != null) {
            val team = board.getTeam(lpa.getGroupTabListName(group)) ?: return
            team.addPlayer(newPlayer)
        }
    }

    @Suppress("Deprecation")
    fun removePlayerFromScoreboard(holder: Player, remove: Player) {
        val board = holder.scoreboard
        val lpa = main.luckPermsAdapter
        val group = lpa.getUserGroup(remove)
        if (group != null) {
            val team = board.getTeam(lpa.getGroupTabListName(group)) ?: return
            team.removePlayer(remove)
        }

    }

}