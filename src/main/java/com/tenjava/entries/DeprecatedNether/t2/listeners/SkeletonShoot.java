package com.tenjava.entries.DeprecatedNether.t2.listeners;

import com.tenjava.entries.DeprecatedNether.t2.TenJava;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class SkeletonShoot implements Listener {
    private TenJava main;

    public SkeletonShoot(TenJava main) {
        this.main = main;
    }

    @EventHandler
    public void shoot(EntityShootBowEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        if (main.methods.hasSkeletonAttack((Player) e.getEntity())) {
            e.getProjectile().setVelocity(e.getProjectile().getVelocity().multiply(2));
        }
    }
}
