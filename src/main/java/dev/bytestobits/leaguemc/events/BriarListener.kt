package dev.bytestobits.leaguemc.events

import dev.bytestobits.leaguemc.Kits.Briar
import dev.bytestobits.leaguemc.Kits.Galio
import dev.bytestobits.leaguemc.LeagueMC
import dev.bytestobits.leaguemc.Messages
import dev.bytestobits.leaguemc.Util
import org.bukkit.Location
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class BriarListener(private val plugin: LeagueMC): Listener {

    fun launchPlayer(player: Player, location: Location, speed: Double) {
        val direction = location.toVector().subtract(player.location.toVector())

        direction.normalize().multiply(speed)
        player.velocity = direction
    }

    @EventHandler
    fun onPlayerDamage(event: EntityDamageEvent) {
        val entity = event.entity

        if(entity is Player) {
            if(event.cause == EntityDamageEvent.DamageCause.FALL) {
                val item = entity.inventory.itemInMainHand

                if(Util.isKitItem(item, Briar.UNIQUE_ID)) {
                    event.isCancelled = true
                }
            }
        }
    }

    @EventHandler
    fun playerHitEvent(event: EntityDamageByEntityEvent) {
        val attacker = event.damager
        val victim = event.entity

        if(attacker is Arrow) {
            val shooter = attacker.shooter
            if(shooter is Player) {
                val item = shooter.inventory.itemInMainHand
                if(Util.isKitItem(item, Briar.UNIQUE_ID)) {
                    shooter.velocity = Vector(0.0, 10.0, 0.0)

                    object : BukkitRunnable() {
                        override fun run() {
                            launchPlayer(shooter, victim.location, 100.0)
                        }
                    }.runTaskLater(plugin, 20*2)
                }
            }
        }
    }

}