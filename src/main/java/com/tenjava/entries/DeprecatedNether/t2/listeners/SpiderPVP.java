package com.tenjava.entries.DeprecatedNether.t2.listeners;

import com.tenjava.entries.DeprecatedNether.t2.TenJava;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpiderPVP implements Listener {

    private TenJava main;

    public SpiderPVP(TenJava main) {
        this.main = main;
    }

    @EventHandler
    public void hit(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player && e.getDamager() instanceof Player)) {
            return;
        }
        Player damagee = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();
        if (main.methods.hasCaveSpiderAttack(damager)) {
            damagee.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60*20, 1, false));
            damager.sendMessage(ChatColor.GREEN + "You used Cave Spider attack!");
        }
    }
}
