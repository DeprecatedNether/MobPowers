package com.tenjava.entries.DeprecatedNether.t2.listeners;

import com.tenjava.entries.DeprecatedNether.t2.TenJava;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class TJInventoryEvent implements Listener {
    private TenJava main;

    public TJInventoryEvent(TenJava main) {
        this.main = main;
    }

    @EventHandler
    public void interact(InventoryClickEvent e) {
        if (!e.getInventory().getTitle().equals("MobPowers Selector")) return;
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
        EntityType type = main.methods.materialToEntityType(e.getCurrentItem().getType());
        if (type == null) return;
        main.methods.useSuperPower((Player)e.getWhoClicked(), type);
    }
}
