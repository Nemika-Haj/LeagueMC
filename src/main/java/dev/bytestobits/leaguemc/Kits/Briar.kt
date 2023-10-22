package dev.bytestobits.leaguemc.Kits

import dev.bytestobits.leaguemc.Messages
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object Briar {
    const val UNIQUE_ID = 7000

    fun kit(player: Player) {
        val rBow = ItemStack(Material.BOW)

        val meta = rBow
                .itemMeta
        meta.displayName(Component.text(Messages.color("&4&l[R] Certain Death")))
        meta.lore(mutableListOf(
                Component.text(Messages.color("&7Land an arrow on someone to launch yourself to them."))
        ))
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true)
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        meta.setCustomModelData(UNIQUE_ID)

        rBow.itemMeta = meta

        player.inventory.addItem(rBow)
        player.inventory.addItem(ItemStack(Material.ARROW))
    }
}