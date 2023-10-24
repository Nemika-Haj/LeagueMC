package dev.bytestobits.leaguemc

import dev.bytestobits.leaguemc.commands.LeagueKit
import dev.bytestobits.leaguemc.events.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.lang.Exception

class LeagueMC : JavaPlugin() {

    override fun onEnable() {
        Bukkit.getPluginCommand("leaguekit")?.setExecutor(LeagueKit())

        Bukkit.getPluginManager().registerEvents(YorickListener(this), this)
        Bukkit.getPluginManager().registerEvents(AnnieListener(this), this)
        Bukkit.getPluginManager().registerEvents(NeekoListener(this), this)
        Bukkit.getPluginManager().registerEvents(AkshanListener(this), this)
        Bukkit.getPluginManager().registerEvents(GangplankListener(), this)
        Bukkit.getPluginManager().registerEvents(GalioListener(), this)
        Bukkit.getPluginManager().registerEvents(BriarListener(this), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    fun changePlayerSkin(player: Player, targetSkin: String?) {
        val command = if (targetSkin != null) "skin set ${player.name} $targetSkin" else "skin clear ${player.name}"
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command)
    }
}