package dev.bytestobits.leaguemc.events

import dev.bytestobits.leaguemc.Kits.Yorick
import dev.bytestobits.leaguemc.LeagueMC
import dev.bytestobits.leaguemc.Messages
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Vex
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.min

class YorickListener(private val plugin: LeagueMC): Listener {
    private val ghouls = mutableMapOf<Player, MutableList<Vex>>()
    private val targets = mutableMapOf<Vex, LivingEntity>()

    private fun clearGhouls(owner: Player) {
        ghouls[owner]?.let { ghoul ->
            ghoul.forEach {
                targets.remove(it)
                if(it.isValid) it.remove()
            }
            ghouls.remove(owner)
        }
    }

    @EventHandler
    fun playerLeave(event: PlayerQuitEvent) {
        clearGhouls(event.player)
    }

    @EventHandler
    fun hitEvent(event: EntityDamageByEntityEvent) {
        val attacker = event.damager
        val victim = event.entity

        if(victim !is LivingEntity) return

        // Return to attacker once victim dies
        if(attacker is Vex) {
            if(victim is Player && ghouls[victim]?.contains(attacker) == true) {
                event.isCancelled = true
                return
            }

            if(event.finalDamage >= victim.health) {
                for(pair in ghouls) {
                    val ghouls = pair.component2()

                    if(ghouls.contains(attacker)) {
                        targets.remove(attacker)
                    }
                }
            }
        }

        // Cancel hitting allied ghouls
        if(ghouls[attacker]?.contains(victim) == true) {
            event.isCancelled = true
            return
        }

        if(attacker is Player) {
            val item = attacker.inventory.itemInMainHand
            if(item.itemMeta == null || !item.itemMeta.hasCustomModelData()) return

            // Hit by Q
            if(item.itemMeta.customModelData == Yorick.uniqueId) {
                if(item.type == Material.DIAMOND_SHOVEL) {
                    attacker.health = min(attacker.health + 0.75, 20.0)
                    event.damage = event.damage * 1.25

                    // Player Death by Q
                    if(victim is Player && event.finalDamage >= victim.health) {
                        Bukkit.getServer().broadcast(Component.text(Messages.color("&9&l${attacker.name}: &e${victim.name}&7, I shall send you back to the grave.")))
                    }

                    if(event.finalDamage >= victim.health) {
                        spawnGhoul(attacker)
                    }
                }
            }

            ghouls[attacker]?.let {
                it.forEach { ghoul ->
                    targets[ghoul] = victim
                }
            }
        }
    }

    private fun spawnGhoul(owner: Player) {
        val vex = owner.world.spawn(owner.location, Vex::class.java)
        vex.isInvulnerable = true
        vex.customName(Component.text(Messages.color("&3&l${owner.name}'s Ghoul")))
        vex.let {
            if(owner !in ghouls.keys) {
                ghouls[owner] = mutableListOf(it)
            } else {
                ghouls[owner]!!.add(it)
            }

            Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                it.remove()
                ghouls[owner]?.remove(it)
                targets.remove(it)
            }, 1200L)
        }
        vex.target = owner

        object : BukkitRunnable() {
            override fun run() {
                if(vex.isValid) {
                    if(targets.contains(vex)) {
                        vex.target = targets[vex]
                    } else {
                        vex.pathfinder.moveTo(owner)
                    }
                } else {
                    this.cancel()
                }
            }
        }.runTaskTimer(plugin, 0L, 20L)
    }
}