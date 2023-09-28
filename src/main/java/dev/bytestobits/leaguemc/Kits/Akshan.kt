package dev.bytestobits.leaguemc.Kits

import dev.bytestobits.leaguemc.Messages
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object Akshan {
    const val uniqueId = 4000

    fun kit(player: Player) {
        val wStar = ItemStack(Material.NETHER_STAR)

        val meta = wStar.itemMeta
        meta.displayName(Component.text(Messages.color("&6&l[W] Going Rogue")))
        meta.lore(mutableListOf(
            Component.text(Messages.color("&7Become invisible to people further than 5 blocks away from you."))
        ))
        meta.addEnchant(Enchantment.LUCK, 1, true)
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        meta.setCustomModelData(uniqueId)

        wStar.itemMeta = meta

        player.inventory.addItem(wStar)
    }
}