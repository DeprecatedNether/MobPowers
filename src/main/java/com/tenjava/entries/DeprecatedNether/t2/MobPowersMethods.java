package com.tenjava.entries.DeprecatedNether.t2;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class MobPowersMethods {
    /**
     * Gives the player a token for killing a mob based on the percentage set in the config.
     * For example, if the entity type's drop chance is set to 0, this will never do anything.
     * @param entity
     */
    public void giveToken(Player player, EntityType entity) {

    }

    public boolean takeToken(Player player, EntityType entity) {
        return false;
    }

    public boolean useSuperPower(Player player, EntityType entity) {
        if (!takeToken(player, entity)) return false;
        return false;
    }
}
