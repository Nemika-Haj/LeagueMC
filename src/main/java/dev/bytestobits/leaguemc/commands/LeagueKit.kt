package dev.bytestobits.leaguemc.commands

import dev.bytestobits.leaguemc.Kits.Annie
import dev.bytestobits.leaguemc.Kits.Neeko
import dev.bytestobits.leaguemc.Kits.Yorick
import dev.bytestobits.leaguemc.Messages
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class LeagueKit: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player) {
            sender.sendMessage("Only players can execute this command.")
        } else {
            if(args.isEmpty()) {
                sender.sendMessage(Messages.color("&cYou have to specify a kit first."))
            } else {

                when (val kit = args.first()) {
                    "yorick" -> Yorick.kit(sender)
                    "annie" -> Annie.kit(sender)
                    "neeko" -> Neeko.kit(sender)
                    else -> {
                        sender.sendMessage(Messages.color("&cKit &e$kit &cnot found."))
                    }
                }
            }
        }

        return true
    }
}