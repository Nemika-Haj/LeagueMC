package dev.bytestobits.leaguemc.events

import dev.bytestobits.leaguemc.Kits.Akshan
import dev.bytestobits.leaguemc.LeagueMC
import dev.bytestobits.leaguemc.Messages
import dev.bytestobits.leaguemc.Util
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.scheduler.BukkitRunnable

class AkshanListener(private val plugin: LeagueMC): Listener {
    private val invisiblePlayers = mutableListOf<Player>()

    @EventHandler
    fun playerJoin(event: PlayerJoinEvent) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "dynmap show ${event.player.name}")
    }

    @EventHandler
    fun playerMove(event: PlayerMoveEvent) {
        val player = event.player

        if(player in invisiblePlayers) {
            hidePlayer(player)
        }
    }

    @EventHandler
    fun playerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val item = player.inventory.itemInMainHand

        if(item.hasItemMeta() && item.itemMeta.hasCustomModelData() && item.itemMeta.customModelData == Akshan.uniqueId && item.type == Material.NETHER_STAR) {
            if(invisiblePlayers.contains(player)) {
                invisiblePlayers.remove(player)
                player.sendMessage(Messages.color("&7You have &erevealed &7yourself."))
                player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
                revealPlayer(player)
            } else {
                invisiblePlayers.add(player)
                hidePlayer(player)
                player.sendMessage(Messages.color("&7You are now &ehidden &7from all players that are not close to you."))
                player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
            }
        }
    }

    private fun revealPlayer(player: Player) {
        Bukkit.getServer().onlinePlayers.forEach { it.showPlayer(plugin, player) }
    }

    private fun hidePlayer(player: Player) {
        Bukkit.getServer().onlinePlayers.forEach {
            if(Util.calculateDistance(player.location, it.location) <= 5) {
                it.showPlayer(plugin, player)
            } else {
                it.hidePlayer(plugin, player)
            }
        }
    }
}