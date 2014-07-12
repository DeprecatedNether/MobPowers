package com.tenjava.entries.DeprecatedNether.t2;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ShowParticles extends BukkitRunnable {

    private TenJava main;

    public ShowParticles(TenJava main) {
        this.main = main;
    }

    @Override
    public void run() {
        for (Player player : main.getServer().getOnlinePlayers()) {
            if (main.methods.hasCaveSpiderAttack(player)) {
                // show particles
                for (Player other : main.getServer().getOnlinePlayers()) {
                    if (other.getWorld().equals(player.getWorld())) {
                        other.playEffect(player.getEyeLocation(), Effect.MOBSPAWNER_FLAMES, null);
                    }
                }
            }
        }
    }
}
