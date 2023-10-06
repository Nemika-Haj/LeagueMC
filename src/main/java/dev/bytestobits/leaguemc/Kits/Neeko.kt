package dev.bytestobits.leaguemc.Kits

import dev.bytestobits.leaguemc.Messages
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object Neeko {
    const val UNIQUE_ID = 3000

    fun kit(player: Player) {
        val rSlime = ItemStack(Material.SLIME_BALL)

        val meta = rSlime.itemMeta
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        meta.addEnchant(Enchantment.LUCK, 1, true)
        meta.displayName(Component.text(Messages.color("&d&l[R] Pop Blossom")))
        meta.lore(mutableListOf(
            Component.text(Messages.color("&7Knock everyone up within a 10 block radius."))
        ))
        meta.setCustomModelData(UNIQUE_ID)

        rSlime.itemMeta = meta

        player.inventory.addItem(rSlime)
    }

}