package com.tenjava.entries.DeprecatedNether.t2.listeners;

import com.tenjava.entries.DeprecatedNether.t2.TenJava;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashMap;
import java.util.UUID;

public class KillEvent implements Listener {
    private TenJava main;
    private HashMap<UUID, UUID> lastDamage = new HashMap<UUID, UUID>(); // mob's UUID -> killer's UUID

    public KillEvent(TenJava main) {
        this.main = main;
    }

    @EventHandler
    public void kill(EntityDamageByEntityEvent e) {
        if (!main.methods.isPowerEnabled(e.getEntityType())) return;
        if (!(e.getEntity() instanceof Damageable)) {
            return;
        }
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        if (!((Player) e.getDamager()).hasPermission("mobpowers.use")) return;
        lastDamage.put(e.getEntity().getUniqueId(), e.getDamager().getUniqueId());
    }

    @EventHandler
    public void death(EntityDeathEvent e) {
        if (!main.methods.isPowerEnabled(e.getEntityType())) return;
        if (e.getEntity().getLastDamageCause().getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        UUID uuid = lastDamage.get(e.getEntity().getUniqueId());
        if (uuid == null) return;
        Player killer = main.getServer().getPlayer(uuid);
        if (killer == null) return;
        if (killer.getGameMode() == GameMode.CREATIVE && main.getConfig().getBoolean("ignore-creative-kills")) return;
        if (main.methods.giveToken(killer, e.getEntityType())) {
            killer.sendMessage(ChatColor.GOLD + "You earned one " + main.methods.getEntityName(e.getEntityType()) + " token.");
        }
        lastDamage.remove(e.getEntity().getUniqueId());
    }
}
