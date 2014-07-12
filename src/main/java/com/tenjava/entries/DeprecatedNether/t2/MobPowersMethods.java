package com.tenjava.entries.DeprecatedNether.t2;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MobPowersMethods {

    List<EntityType> mobs = new ArrayList<EntityType>();
    private TenJava main;

    public MobPowersMethods(TenJava main) {
        this.main = main;
        // load mobs we have superpowers for
        mobs.add(EntityType.CREEPER);
        mobs.add(EntityType.ENDERMAN);
        mobs.add(EntityType.GHAST);
        mobs.add(EntityType.SQUID);
        mobs.add(EntityType.HORSE);
        mobs.add(EntityType.SPIDER);
    }

    /**
     * Gives the player a token for killing a mob based on the percentage set in the config.
     * For example, if the entity type's drop chance is set to 0, this will never do anything.
     * @param entity
     */
    public boolean giveToken(Player player, EntityType entity) {
        if (!mobs.contains(entity)) {
            return false;
        }
        Random random = new Random();
        int chance = main.getConfig().getInt("powers." + entity.toString().toLowerCase() + ".drop-chance");
        if (random.nextInt(100) + 1 > chance) { // gives us a number between 1 and 100. If it's less than or equal to "chance", do something. Larger the chance, more likely this will happen
            return false;
        }
        File file = new File(main.getDataFolder(), "players.yml");
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        fileConfiguration.set(player.getName() + "." + entity.toString().toLowerCase(), 1);
        try {
            fileConfiguration.save(file);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Takes enough tokens from the player to use the mob power.
     * @param player The player.
     * @param entity The mob.
     * @return True if all good, false if fail (player doesn't have enough coins)
     */
    public boolean takeToken(Player player, EntityType entity) {
        if (mobs.contains(entity)) return false;
        File file = new File(main.getDataFolder(), "players.yml");
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        if (!fileConfiguration.isInt(player.getName() + "." + entity.toString().toLowerCase())) return false; // Haven't killed this entity yet
        int tokens = fileConfiguration.getInt(player.getName() + "." + entity.toString().toLowerCase());
        int price = main.getConfig().getInt("powers." + entity.toString().toLowerCase() + ".price");
        if (tokens < price) return false;
        fileConfiguration.set(player.getName() + "." + entity.toString().toLowerCase(), tokens-price);
        try {
            fileConfiguration.save(file);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        return true;
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
