package dev.bytestobits.leaguemc.events

import dev.bytestobits.leaguemc.Kits.Gangplank
import dev.bytestobits.leaguemc.Util
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import kotlin.math.min

class GangplankListener: Listener {

    @EventHandler
    fun playerEat(event: PlayerItemConsumeEvent) {
        val player = event.player
        val item = event.item

        if(Util.isKitItem(item, Gangplank.UNIQUE_ID)) {
            event.isCancelled = true
            player.clearActivePotionEffects()
            player.health = min(player.health + 3, 20.0)
            player.foodLevel += 5
        }
    }
}