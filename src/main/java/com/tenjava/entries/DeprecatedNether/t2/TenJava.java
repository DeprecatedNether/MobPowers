package com.tenjava.entries.DeprecatedNether.t2;

import com.tenjava.entries.DeprecatedNether.t2.listeners.InteractEvent;
import com.tenjava.entries.DeprecatedNether.t2.listeners.TJInventoryEvent;
import com.tenjava.entries.DeprecatedNether.t2.listeners.KillEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TenJava extends JavaPlugin {
    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new InteractEvent(this), this);
        pm.registerEvents(new KillEvent(this), this);
        pm.registerEvents(new TJInventoryEvent(this), this);
    }
}
