package dev.bytestobits.leaguemc.Kits

import dev.bytestobits.leaguemc.Messages
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object Yorick {
//    val items = mutableListOf<ItemStack>()
    const val uniqueId = 1000

    fun kit(player: Player) {
        val qShovel = ItemStack(Material.DIAMOND_SHOVEL)
        val meta = qShovel.itemMeta
        meta.displayName(Component.text(Messages.color("&b&l[Q] Last Rites")))
        meta.lore(mutableListOf(
            Component.text(Messages.color("&7Yorick deals bonus damage on his next attack and heals himself. If the target dies a grave will be dug."))
        ))
        meta.addEnchant(Enchantment.LUCK, 1, true)
        meta.setCustomModelData(uniqueId)
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)

        qShovel.itemMeta = meta
//        items.add(qShovel)

        player.inventory.addItem(qShovel)
    }

}