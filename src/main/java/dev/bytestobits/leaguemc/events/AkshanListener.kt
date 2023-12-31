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
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable

class AkshanListener(private val plugin: LeagueMC): Listener {
    private val invisiblePlayers = mutableListOf<Player>()

    @EventHandler
    fun playerLeave(event: PlayerQuitEvent) {
        invisiblePlayers.remove(event.player)
        revealPlayer(event.player)
    }

    @EventHandler
    fun playerDeath(event: PlayerDeathEvent) {
        invisiblePlayers.remove(event.player)
        revealPlayer(event.player)
    }

    @EventHandler
    fun playerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val item = player.inventory.itemInMainHand

        if(Util.isKitItem(item, Akshan.UNIQUE_ID)) {
            event.isCancelled = true
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
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "dynmap hide ${event.player.name}")
            }
        }
    }

    private fun revealPlayer(player: Player) {
        Bukkit.getServer().onlinePlayers.forEach { it.showPlayer(plugin, player) }
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "dynmap show ${player.name}")
    }

    private fun hidePlayer(player: Player) {
        object : BukkitRunnable() {
            override fun run() {
                if(player !in invisiblePlayers) {
                    this.cancel()
                }

                Bukkit.getServer().onlinePlayers.forEach {
                    if(Util.calculateDistance(player.location, it.location) <= 5) {
                        it.showPlayer(plugin, player)
                    } else {
                        it.hidePlayer(plugin, player)
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 5L)
    }
}