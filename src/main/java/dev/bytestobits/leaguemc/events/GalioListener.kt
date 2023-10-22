package dev.bytestobits.leaguemc.events

import dev.bytestobits.leaguemc.Kits.Galio
import dev.bytestobits.leaguemc.Messages
import dev.bytestobits.leaguemc.Util
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.util.Vector

class GalioListener: Listener {
    val openInventories = mutableListOf<Inventory>()

    fun openPlayerSelectGUI(player: Player) {
        val inventory = Bukkit.createInventory(null, 54, "Select Player")
        Bukkit.getOnlinePlayers().forEach { onlinePlayer ->
            if(onlinePlayer.uniqueId != player.uniqueId) {
                val head = ItemStack(Material.PLAYER_HEAD)
                val meta = head.itemMeta as SkullMeta
                meta.owningPlayer = onlinePlayer
                meta.displayName(Component.text(Messages.color("&6${onlinePlayer.name}")))
                head.itemMeta = meta

                inventory.addItem(head)
            }
        }
        openInventories.add(inventory)
        player.openInventory(inventory)
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val clickedInventory = event.clickedInventory
        val player = event.whoClicked as Player
        val clickedItem = event.currentItem

        if(clickedInventory != null && clickedInventory in openInventories && clickedItem != null && clickedItem.type == Material.PLAYER_HEAD) {
            val skullMeta = clickedItem.itemMeta as SkullMeta
            val clickedPlayer = Bukkit.getServer().getPlayer(skullMeta.owningPlayer!!.uniqueId)
            if(clickedPlayer == null) {
                player.sendMessage(Messages.color("&cCan't land on this player right now."))
                return
            }
            openInventories.remove(clickedInventory)
            clickedInventory.close()

            val location = clickedPlayer.location.add(0.0, 200.0, 0.0)
            player.teleport(location)

            player.velocity = player.velocity.add(Vector(0.0, -20.0, 0.0))

            clickedPlayer.sendMessage(Messages.color("&6&lA hero is coming..."))
        }
    }

    @EventHandler
    fun onPlayerDamage(event: EntityDamageEvent) {
        val entity = event.entity

        if(entity is Player) {
            if(event.cause == EntityDamageEvent.DamageCause.FALL) {
                val item = entity.inventory.itemInMainHand

                if(Util.isKitItem(item, Galio.UNIQUE_ID)) {
                    event.isCancelled = true
                    entity.sendMessage(Messages.color("&fYou big strong Galio, no fall damage HUGE BIG VAST."))
                }
            }
        }
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val item = player.inventory.itemInMainHand

        if(Util.isKitItem(item, Galio.UNIQUE_ID)) {
            openPlayerSelectGUI(player)
        }
    }
}