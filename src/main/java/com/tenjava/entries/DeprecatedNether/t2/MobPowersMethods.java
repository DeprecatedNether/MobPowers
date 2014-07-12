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

    /**
     * Takes enough tokens from the player to use the mob power.
     * @param player The player.
     * @param entity The mob.
     * @return True if all good, false if fail (player doesn't have enough coins)
     */
    public boolean takeToken(Player player, EntityType entity) {
        return false;
    }

    /**
     * Applies the mob power to the player.
     * @param player
     * @param entity
     * @return
     */
    public boolean useSuperPower(Player player, EntityType entity) {
        if (!takeToken(player, entity)) return false;
        return false;
    }

    /**
     * Opens the GUI for the player.
     * @param player The player.
     */
    public void openGUI(Player player) {

    }

    /**
     * Gets the number of tokens that player has for that particular mob.
     * @param player The player.
     * @param entity The mob.
     * @return The number of tokens.
     */
    public int getTokens(Player player, EntityType entity) {
        return 0;
    }
}
