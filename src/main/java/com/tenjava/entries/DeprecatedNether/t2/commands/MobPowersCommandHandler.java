package com.tenjava.entries.DeprecatedNether.t2.commands;

import com.tenjava.entries.DeprecatedNether.t2.TenJava;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MobPowersCommandHandler implements CommandExecutor {

    private TenJava main;

    public MobPowersCommandHandler(TenJava main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "If you aren't a player, how are you supposed to kill mobs to get tokens?");
            return true;
        }

        return true;
    }
}
