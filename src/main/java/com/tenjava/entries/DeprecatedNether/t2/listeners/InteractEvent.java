package com.tenjava.entries.DeprecatedNether.t2.listeners;

import com.tenjava.entries.DeprecatedNether.t2.TenJava;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractEvent implements Listener {
    private TenJava main;

    public InteractEvent(TenJava main) {
        this.main = main;
    }

    @EventHandler
    public void interact(PlayerInteractEvent e) {
        if (!e.getPlayer().hasPermission("mobpowers.use")) return;
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        ItemStack inhand = e.getPlayer().getItemInHand();
        if (inhand == null || inhand.getType() != Material.STICK) return;
        if (inhand.getDurability() != 2) return;
        main.methods.openGUI(e.getPlayer());
    }
}
