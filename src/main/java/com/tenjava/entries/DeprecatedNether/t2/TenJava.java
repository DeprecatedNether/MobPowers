package com.tenjava.entries.DeprecatedNether.t2;

import com.tenjava.entries.DeprecatedNether.t2.commands.MobPowersCommandHandler;
import com.tenjava.entries.DeprecatedNether.t2.listeners.InteractEvent;
import com.tenjava.entries.DeprecatedNether.t2.listeners.TJInventoryEvent;
import com.tenjava.entries.DeprecatedNether.t2.listeners.KillEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class TenJava extends JavaPlugin {

    public MobPowersMethods methods;

    @Override
    public void onEnable() {
        File file = new File(getDataFolder(), "players.yml");
        if (!file.isFile()) {
            try {
                file.createNewFile();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                getLogger().severe("Couldn't access/create the players.yml file, meaning we have no way of accessing the players' token data. I'll just disable myself.");
                getServer().getPluginManager().disablePlugin(this);
            }
        }
        getCommand("mobpowers").setExecutor(new MobPowersCommandHandler(this));
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new InteractEvent(this), this);
        pm.registerEvents(new KillEvent(this), this);
        pm.registerEvents(new TJInventoryEvent(this), this);
        saveDefaultConfig();
        this.methods = new MobPowersMethods(this);
    }
}
