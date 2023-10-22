package dev.bytestobits.leaguemc.Kits

import dev.bytestobits.leaguemc.Messages
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object Galio {
    const val UNIQUE_ID = 6000

    fun kit(player: Player) {
        val rFeather = ItemStack(Material.FEATHER)

        val meta = rFeather
                .itemMeta
        meta.displayName(Component.text(Messages.color("&f&l[R] Hero's Entrance")))
        meta.lore(mutableListOf(
                Component.text(Messages.color("&7Arrive into a player's position in insane speed."))
        ))
        meta.addEnchant(Enchantment.LUCK, 1, true)
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        meta.setCustomModelData(UNIQUE_ID)

        rFeather.itemMeta = meta

        player.inventory.addItem(rFeather)
    }
}