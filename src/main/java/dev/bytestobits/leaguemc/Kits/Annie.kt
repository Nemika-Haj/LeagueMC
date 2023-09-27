package dev.bytestobits.leaguemc.Kits

import dev.bytestobits.leaguemc.Messages
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object Annie {
    const val uniqueId = 2000

    fun kit(player: Player) {
        val rItem = ItemStack(Material.MAGMA_CREAM)
        val meta = rItem.itemMeta
        meta.displayName(Component.text(Messages.color("&4&l[R] Summon: Tibbers")))
        meta.lore(mutableListOf(
            Component.text(Messages.color("&7Annie summons Tibbers to the target location."))
        ))
        meta.addEnchant(Enchantment.LUCK, 1, false)
        meta.setCustomModelData(uniqueId)

        rItem.itemMeta = meta

        player.inventory.addItem(rItem)
    }
}