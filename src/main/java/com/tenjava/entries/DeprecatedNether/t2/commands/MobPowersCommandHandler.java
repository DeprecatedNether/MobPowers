package com.tenjava.entries.DeprecatedNether.t2.commands;

import com.tenjava.entries.DeprecatedNether.t2.TenJava;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MobPowersCommandHandler implements CommandExecutor {

    private TenJava main;

    public MobPowersCommandHandler(TenJava main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        return true;
    }
}
