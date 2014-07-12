package com.tenjava.entries.DeprecatedNether.t2.listeners;

import com.tenjava.entries.DeprecatedNether.t2.TenJava;
import org.bukkit.ChatColor;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class KillEvent implements Listener {
    private TenJava main;

    public KillEvent(TenJava main) {
        this.main = main;
    }

    @EventHandler
    public void kill(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Damageable)) {
            return;
        }
        if (!(e.getDamager() instanceof Player)) {

        }
        Damageable damageable = (Damageable) e.getEntity();
        Player damager = (Player) e.getDamager();
        if (damageable.getHealth() - e.getDamage() <= 0) {
            if (main.methods.giveToken(damager, e.getEntityType())) {
                damager.sendMessage(ChatColor.GOLD + "You earned one " + e.getEntityType().toString().toLowerCase() + " token.");
            }
        }
    }
}
