package com.tenjava.entries.DeprecatedNether.t2.listeners;

import com.tenjava.entries.DeprecatedNether.t2.TenJava;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class TJInventoryEvent implements Listener {
    private TenJava main;

    public TJInventoryEvent(TenJava main) {
        this.main = main;
    }

    @EventHandler
    public void interact(InventoryClickEvent e) {
        if (!e.getInventory().getTitle().equals("MobPowers Selector")) return;
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
        e.setCancelled(true);
        EntityType type = main.methods.materialToEntityType(e.getCurrentItem().getType());
        if (type == null) return;
        final Player player = (Player) e.getWhoClicked();
        main.methods.useSuperPower(player, type);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.closeInventory();
            }
        }.runTaskLater(main, 1);
    }
}
