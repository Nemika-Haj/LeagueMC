package dev.bytestobits.leaguemc.events

import dev.bytestobits.leaguemc.Kits.Neeko
import dev.bytestobits.leaguemc.LeagueMC
import dev.bytestobits.leaguemc.Messages
import dev.bytestobits.leaguemc.Util
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.util.Vector

class NeekoListener(private val plugin: LeagueMC): Listener {
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

        val resetItem = ItemStack(Material.BARRIER)
        val meta = resetItem.itemMeta
        meta.displayName(Component.text(Messages.color("&c&lReset disguise")))
        resetItem.itemMeta = meta
        inventory.addItem(resetItem)

        openInventories.add(inventory)
        player.openInventory(inventory)
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        plugin.changePlayerSkin(event.player, null)
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val clickedInventory = event.clickedInventory
        val player = event.whoClicked as Player
        val clickedItem = event.currentItem

        if(clickedInventory != null && clickedInventory in openInventories && clickedItem != null) {
            if(clickedItem.type == Material.PLAYER_HEAD) {
                val skullMeta = clickedItem.itemMeta as SkullMeta
                val clickedPlayer = Bukkit.getServer().getPlayer(skullMeta.owningPlayer!!.uniqueId)
                if(clickedPlayer == null) {
                    player.sendMessage(Messages.color("&cCan't morph to this player right now."))
                    return
                }
                openInventories.remove(clickedInventory)
                clickedInventory.close()

                plugin.changePlayerSkin(player, clickedPlayer.name)
                player.displayName(Component.text(clickedPlayer.name))
                player.playerListName(Component.text(clickedPlayer.name))
                player.isCustomNameVisible = true
                player.sendMessage(Messages.color("&d&lTransforming to ${clickedPlayer.name}..."))
            } else if(clickedItem.type == Material.BARRIER) {
                clickedInventory.close()
                plugin.changePlayerSkin(player, null)
                player.displayName(Component.text(player.name))
                player.playerListName(Component.text(player.name))
                player.customName(Component.text(player.name))
                player.isCustomNameVisible = false
                player.sendMessage(Messages.color("&d&lResetting morph..."))
            }
        }
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val item = player.inventory.itemInMainHand

        if(Util.isKitItem(item, Neeko.UNIQUE_ID)) {
             if(item.type == Material.SLIME_BALL) {
                 player.location.getNearbyEntities(10.0, 10.0, 10.0).forEach { if(it != player) it.velocity = Vector(0.0, 2.0, 0.0) }
             } else if(item.type == Material.NAME_TAG) {
                 openPlayerSelectGUI(player)
             }
        }
    }

}