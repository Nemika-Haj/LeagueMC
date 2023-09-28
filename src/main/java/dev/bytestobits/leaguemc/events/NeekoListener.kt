package dev.bytestobits.leaguemc.events

import dev.bytestobits.leaguemc.Kits.Neeko
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.util.Vector

class NeekoListener: Listener {

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val item = player.inventory.itemInMainHand

        if(item.hasItemMeta() && item.itemMeta.hasCustomModelData() && item.itemMeta.customModelData == Neeko.uniqueId && item.type == Material.SLIME_BALL) {
             player.location.getNearbyPlayers(10.0).forEach { if(it != player) it.velocity = Vector(0.0, 2.0, 0.0) }
        }
    }

}