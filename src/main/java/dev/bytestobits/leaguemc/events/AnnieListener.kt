package dev.bytestobits.leaguemc.events

import dev.bytestobits.leaguemc.Kits.Annie
import dev.bytestobits.leaguemc.LeagueMC
import dev.bytestobits.leaguemc.Messages
import dev.bytestobits.leaguemc.Util
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.PolarBear
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable

class AnnieListener(private val plugin: LeagueMC): Listener {
    val tibbers = mutableMapOf<Player, PolarBear>()
    val targets = mutableMapOf<PolarBear, LivingEntity>()

    private fun clearTibbers(owner: Player) {
        tibbers[owner]?.let {
            if(it.isValid) {
                it.remove()
            }
            targets.remove(it)
            tibbers.remove(owner)
        }
    }

    @EventHandler
    fun playerDeath(event: PlayerDeathEvent) {
        clearTibbers(event.player)
    }

    @EventHandler
    fun playerLeave(event: PlayerQuitEvent) {
        clearTibbers(event.player)
    }

    @EventHandler
    fun attackEvent(event: EntityDamageByEntityEvent) {
        val attacker = event.damager
        val victim = event.entity

        if(victim !is LivingEntity) return

        if(attacker is Player && tibbers.keys.contains(attacker)) {
            val bear = tibbers[attacker]
            if(victim == bear) {
                event.isCancelled = true
                return
            }

            bear?.let {
                targets[bear] = victim
            }
        }

        if(attacker is PolarBear && tibbers.values.contains(attacker)) {
            if (tibbers[victim] == attacker) {
                event.isCancelled = true
                return
            }
            victim.fireTicks = 100
        }

        if(victim is Player && victim in tibbers.keys && attacker is LivingEntity) {
            tibbers[victim]?.let {
                targets[it] = attacker
            }
        }

        if(event.finalDamage > victim.health && tibbers.values.contains(victim)) {
            for(owner in tibbers.keys) {
                if(tibbers[owner] == victim) {
                    tibbers.remove(owner)
                    return
                }
            }
        }
    }

    @EventHandler
    fun playerInteraction(event: PlayerInteractEvent) {
        val player = event.player
        val item = player.inventory.itemInMainHand

        if(Util.isKitItem(item, Annie.UNIQUE_ID)) {
            if(player in tibbers.keys) return

            val bear = player.world.spawn(player.location, PolarBear::class.java)
            tibbers[player] = bear
            bear.customName(Component.text(Messages.color("&c&l${player.name}'s Tibbers")))
            bear.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 100.0
            bear.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.let {
                it.baseValue = it.baseValue * 1.25
            }

            if(player.level < 16) {
                bear.setBaby()
            }

            Bukkit.getServer().broadcast(Component.text(Messages.color("&4&l${player.name}: &7Get 'em, Tibbers!")))

            object : BukkitRunnable() {
                override fun run() {
                    if(bear.isValid) {
                        if(bear in targets) {
                            val target = targets[bear]!!
                            if(target.isValid) {
                                bear.target =  targets[bear]
                            } else {
                                targets.remove(bear)
                            }
                        } else {
                            val distance = Util.calculateDistance(bear.location, player.location)
                            if(distance >= 20) {
                                bear.teleport(player.location)
                            }

                            bear.pathfinder.moveTo(player)
                        }
                    } else {
                        this.cancel()
                    }
                }
            }.runTaskTimer(plugin, 0L, 20L)

            Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                clearTibbers(player)
            }, 6000)
        }
    }

}