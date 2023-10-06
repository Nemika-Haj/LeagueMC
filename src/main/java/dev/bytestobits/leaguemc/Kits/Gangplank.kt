package dev.bytestobits.leaguemc.Kits

import dev.bytestobits.leaguemc.Messages
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object Gangplank {
    const val UNIQUE_ID = 5000

    fun kit(player: Player) {
        val wOrange = ItemStack(Material.APPLE)
        val meta = wOrange.itemMeta
        meta.displayName(Component.text(Messages.color("&6&l[W] Remove Scurvy")))
        meta.lore(mutableListOf(
            Component.text(Messages.color("&7Consume to remove all effects, heal, and restore hunger."))
        ))
        meta.addEnchant(Enchantment.LUCK, 1, true)
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        meta.setCustomModelData(UNIQUE_ID)

        wOrange.itemMeta = meta

        player.inventory.addItem(wOrange)
    }
}