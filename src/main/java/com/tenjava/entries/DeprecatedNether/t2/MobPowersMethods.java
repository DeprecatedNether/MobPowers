package com.tenjava.entries.DeprecatedNether.t2;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MobPowersMethods {

    List<EntityType> mobs = new ArrayList<EntityType>();
    private TenJava main;
    private HashMap<EntityType, String> descriptions = new HashMap<EntityType, String>();
    private HashMap<EntityType, Material> materials = new HashMap<EntityType, Material>();

    public MobPowersMethods(TenJava main) {
        this.main = main;
        // load mobs we have superpowers for
        if (isPowerEnabled(EntityType.CREEPER)) mobs.add(EntityType.CREEPER);
        if (isPowerEnabled(EntityType.ENDERMAN)) mobs.add(EntityType.ENDERMAN);
        if (isPowerEnabled(EntityType.GHAST)) mobs.add(EntityType.GHAST);
        if (isPowerEnabled(EntityType.SQUID)) mobs.add(EntityType.SQUID);
        if (isPowerEnabled(EntityType.HORSE)) mobs.add(EntityType.HORSE);
        if (isPowerEnabled(EntityType.SPIDER)) mobs.add(EntityType.SPIDER);
        if (isPowerEnabled(EntityType.CAVE_SPIDER)) mobs.add(EntityType.CAVE_SPIDER);
        if (isPowerEnabled(EntityType.SKELETON)) mobs.add(EntityType.SKELETON);

        descriptions.put(EntityType.CREEPER, "Create an explosion where you're standing.");
        descriptions.put(EntityType.ENDERMAN, "Teleport to the block you're looking at.");
        descriptions.put(EntityType.GHAST, "Shoot a fireball.");
        descriptions.put(EntityType.SQUID, "Breathe underwater.");
        descriptions.put(EntityType.HORSE, "Jump higher or run faster... you never know!");
        descriptions.put(EntityType.SPIDER, "Crawl faster.");
        descriptions.put(EntityType.CAVE_SPIDER, "Make your bite worse than your bark - give players poison when you hit them.");
        descriptions.put(EntityType.SKELETON, "We told Mojang to nerf the insanely fast-shooting skeletons, but they didn't listen!");

        materials.put(EntityType.CREEPER, Material.SULPHUR);
        materials.put(EntityType.ENDERMAN, Material.ENDER_PEARL);
        materials.put(EntityType.GHAST, Material.FIREBALL);
        materials.put(EntityType.SQUID, Material.INK_SACK);
        materials.put(EntityType.HORSE, Material.DIAMOND_BARDING);
        materials.put(EntityType.SPIDER, Material.SPIDER_EYE);
        materials.put(EntityType.CAVE_SPIDER, Material.FERMENTED_SPIDER_EYE);
        materials.put(EntityType.SKELETON, Material.ARROW);
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
        FileConfiguration fileConfiguration = getPlayersFile();
        fileConfiguration.set(player.getName() + "." + entity.toString().toLowerCase(), fileConfiguration.getInt(player.getName() + "." + entity.toString().toLowerCase()) + 1);
        return savePlayersFile(fileConfiguration);
    }

    /**
     * Takes enough tokens from the player to use the mob power.
     * @param player The player.
     * @param entity The mob.
     * @return True if all good, false if fail (player doesn't have enough coins)
     */
    public boolean takeToken(Player player, EntityType entity) {
        FileConfiguration fileConfiguration = getPlayersFile();
        if (!fileConfiguration.isInt(player.getName() + "." + entity.toString().toLowerCase())) return false; // Haven't killed this entity yet
        int tokens = fileConfiguration.getInt(player.getName() + "." + entity.toString().toLowerCase());
        int price = main.getConfig().getInt("powers." + entity.toString().toLowerCase() + ".price");
        if (tokens < price) return false;
        fileConfiguration.set(player.getName() + "." + entity.toString().toLowerCase(), tokens-price);
        return savePlayersFile(fileConfiguration);
    }

    /**
     * Applies the mob power to the player.
     * @param player The player.
     * @param entity The mob whose powers they should get.
     * @return
     */
    @SuppressWarnings("deprecation")
    public boolean useSuperPower(Player player, EntityType entity) {
        String name = getEntityName(entity);
        if (!takeToken(player, entity)) {
            player.sendMessage(ChatColor.RED + "You do not have enough " + name + " tokens to get " + name + " powers.");
            return false;
        }
        switch (entity) {
            case CREEPER:
                final Location location = player.getLocation();
                player.sendMessage(ChatColor.GRAY + "You have 5 seconds to get away from your Creeper explosion.");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        location.getWorld().createExplosion(location, 5);
                    }
                }.runTaskLater(main, 5*20);
                break;
            case ENDERMAN:
                player.teleport(player.getTargetBlock(null, 50).getLocation());
                break;
            case GHAST:
                player.launchProjectile(Fireball.class);
                break;
            case SQUID:
                player.removePotionEffect(PotionEffectType.WATER_BREATHING);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 60*20, 1, true));
                break;
            case HORSE:
                Random random = new Random();
                PotionEffectType type = random.nextInt(2) == 1 ? PotionEffectType.JUMP : PotionEffectType.SPEED;
                player.removePotionEffect(type);
                player.addPotionEffect(new PotionEffect(type, 60*20, random.nextInt(2)+1, true));
                break;
            case SPIDER:
                player.removePotionEffect(PotionEffectType.SPEED);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60*20, 2, true));
                break;
        }
        player.sendMessage(ChatColor.GREEN + "Enjoy your " + name + " powers!");
        return true;
    }

    public boolean isPowerEnabled(EntityType type) {
        return main.getConfig().getBoolean("powers." + type.toString().toLowerCase() + ".enabled");
    }

    /**
     * Opens the GUI for the player.
     * @param player The player.
     */
    public void openGUI(Player player) {
        Inventory inventory = main.getServer().createInventory(player, 9, "MobPowers Selector");
        HashMap<EntityType, Integer> tokenData = new HashMap<EntityType, Integer>();
        // +1 because we need to account for the statistics. / 2 because we need the same (or one more/less) air on both sides.
        // For example, if we have 4 mobs enabled, we'll need (9-(4+1)/2) = 2 airs on each side
        int slot = (9 - (mobs.size() + 1)) / 2;
        for (EntityType mob : mobs) {
            int tokens = getTokens(player, mob);
            inventory.setItem(slot, craftStack(mob, tokens, getPrice(mob)));
            tokenData.put(mob, tokens);
            slot++;
        }
        inventory.setItem(slot, craftStatisticsStack(tokenData));
        player.openInventory(inventory);
    }

    public int getPrice(EntityType type) {
        return main.getConfig().getInt("powers." + type.toString().toLowerCase() + ".price");
    }

    /**
     * Gets the item for a particular mob.
     * @param type The entity.
     * @param tokens The number of tokens the player has.
     * @param price The number of tokens the player needs.
     */
    public ItemStack craftStack(EntityType type, int tokens, int price) {
        ItemStack itemStack = new ItemStack(materials.get(type), 1);
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<String>();
        meta.setDisplayName(ChatColor.DARK_GREEN + getEntityName(type)); // Turn "ENDERMAN" into "Enderman"
        lore.add(ChatColor.GREEN + descriptions.get(type));
        lore.add(ChatColor.GOLD + "Price: " + (tokens < price ? ChatColor.RED : ChatColor.GREEN) + price);
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack craftStatisticsStack(HashMap<EntityType, Integer> data) {
        ItemStack itemStack = new ItemStack(Material.REDSTONE, 1);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "MobPowers Statistics");
        List<String> lore = new ArrayList<String>();
        for (EntityType type : data.keySet()) {
            lore.add(ChatColor.AQUA + getEntityName(type) + " Tokens: " + ChatColor.DARK_AQUA + data.get(type));
        }
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public String getEntityName(EntityType type) {
        return type.toString().toUpperCase().substring(0, 1) + type.toString().toLowerCase().substring(1);
    }

    public EntityType materialToEntityType(Material material) {
        for (EntityType type : materials.keySet()) {
            if (materials.get(type) == material) {
                return type;
            }
        }
        return null;
    }

    /**
     * Gets the number of tokens that player has for that particular mob.
     * @param player The player.
     * @param entity The mob.
     * @return The number of tokens.
     */
    public int getTokens(Player player, EntityType entity) {
        return getPlayersFile().getInt(player.getName() + "." + entity.toString().toLowerCase());
    }

    /**
     * Gets the players file.
     * @return The players file.
     */
    public FileConfiguration getPlayersFile() {
        File file = new File(main.getDataFolder(), "players.yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Saves the players file.
     * @param configuration The FileConfiguration that has been modified.
     */
    public boolean savePlayersFile(FileConfiguration configuration) {
        try {
            configuration.save(new File(main.getDataFolder(), "players.yml"));
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }
}
