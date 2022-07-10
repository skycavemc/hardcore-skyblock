package de.leonheuer.skycave.hardcore.adapters

import de.leonheuer.skycave.hardcore.HardcoreSkyblock
import net.luckperms.api.LuckPerms
import net.luckperms.api.model.group.Group
import net.luckperms.api.node.Node
import net.luckperms.api.node.NodeType
import net.luckperms.api.node.types.InheritanceNode
import org.bukkit.entity.Player
import java.time.Duration
import java.util.stream.Collectors
import kotlin.math.absoluteValue

class LuckPermsAdapter(private val luckPerms: LuckPerms) {

    fun getUserGroup(player: Player): Group? {
        val user = luckPerms.getPlayerAdapter(Player::class.java).getUser(player)

        val groupsWithPrefix = HashMap<String, Group>()
        for (group in luckPerms.groupManager.loadedGroups) {
            if (group.cachedData.metaData.prefix != null) {
                groupsWithPrefix[group.name] = group
            }
        }

        val primaryGroup = user.getNodes(NodeType.INHERITANCE).stream()
            .map { it as InheritanceNode }
            .filter { groupsWithPrefix.contains(it.groupName) }
            .map { groupsWithPrefix[it.groupName]!! }
            .sorted { first, second -> second.weight.asInt.compareTo(first.weight.asInt) }
            .collect(Collectors.toList())
        if (primaryGroup.isEmpty()) return null
        return primaryGroup.first()
    }

    fun getUserGroupExpiry(player: Player, groupName: String): Duration? {
        val user = luckPerms.getPlayerAdapter(Player::class.java).getUser(player)
        val expiry = user.getNodes(NodeType.INHERITANCE).stream()
            .filter(Node::hasExpiry)
            .filter{ (it as InheritanceNode).groupName == groupName }
            .map{ it.expiryDuration }
            .collect(Collectors.toList())
        if (expiry.isEmpty()) return null
        return expiry.first()
    }

    fun getGroupTabListName(group: Group): String {
        // inversion of group weight: highest group should be at top
        val weight = (group.weight.asInt - HardcoreSkyblock.PERMISSION_GROUPS_MAX_WEIGHT).absoluteValue
        return weight.toString().padStart(5, '0') + group.name
    }

    fun getPrefix(player: Player): String? {
        val user = luckPerms.getPlayerAdapter(Player::class.java).getUser(player)
        return user.cachedData.metaData.prefix
    }

}