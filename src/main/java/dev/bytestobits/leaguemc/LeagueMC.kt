package dev.bytestobits.leaguemc

import dev.bytestobits.leaguemc.commands.LeagueKit
import dev.bytestobits.leaguemc.events.*
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class LeagueMC : JavaPlugin() {
    override fun onEnable() {
        Bukkit.getPluginCommand("leaguekit")?.setExecutor(LeagueKit())

        Bukkit.getPluginManager().registerEvents(YorickListener(this), this)
        Bukkit.getPluginManager().registerEvents(AnnieListener(this), this)
        Bukkit.getPluginManager().registerEvents(NeekoListener(), this)
        Bukkit.getPluginManager().registerEvents(AkshanListener(this), this)
        Bukkit.getPluginManager().registerEvents(GangplankListener(), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}