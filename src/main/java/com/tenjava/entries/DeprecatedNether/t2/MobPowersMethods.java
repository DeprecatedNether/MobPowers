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
import org.bukkit.scheduler.BukkitTask;

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
    private HashMap<String, Long> skeletons = new HashMap<String, Long>();
    private HashMap<String, Long> spiders = new HashMap<String, Long>();
    public BukkitTask particles;

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

        // load descriptions
        descriptions.put(EntityType.CREEPER, main.getConfig().getString("powers.creeper.description"));
        descriptions.put(EntityType.ENDERMAN, main.getConfig().getString("powers.enderman.description"));
        descriptions.put(EntityType.GHAST, main.getConfig().getString("powers.ghast.description"));
        descriptions.put(EntityType.SQUID, main.getConfig().getString("powers.squid.description"));
        descriptions.put(EntityType.HORSE, main.getConfig().getString("powers.horse.description"));
        descriptions.put(EntityType.SPIDER, main.getConfig().getString("powers.spider.description"));
        descriptions.put(EntityType.CAVE_SPIDER, main.getConfig().getString("powers.cave_spider.description"));
        descriptions.put(EntityType.SKELETON, main.getConfig().getString("powers.skeleton.description"));

        // load materials
        materials.put(EntityType.CREEPER, Material.matchMaterial(main.getConfig().getString("powers.creeper.material").replace(" ", "_")));
        materials.put(EntityType.ENDERMAN, Material.matchMaterial(main.getConfig().getString("powers.enderman.material").replace(" ", "_")));
        materials.put(EntityType.GHAST, Material.matchMaterial(main.getConfig().getString("powers.ghast.material").replace(" ", "_")));
        materials.put(EntityType.SQUID, Material.matchMaterial(main.getConfig().getString("powers.squid.material").replace(" ", "_")));
        materials.put(EntityType.HORSE, Material.matchMaterial(main.getConfig().getString("powers.horse.material").replace(" ", "_")));
        materials.put(EntityType.SPIDER, Material.matchMaterial(main.getConfig().getString("powers.spider.material").replace(" ", "_")));
        materials.put(EntityType.CAVE_SPIDER, Material.matchMaterial(main.getConfig().getString("powers.cave_spider.material").replace(" ", "_")));
        materials.put(EntityType.SKELETON, Material.matchMaterial(main.getConfig().getString("powers.skeleton.material").replace(" ", "_")));

        if (main.getConfig().getBoolean("show-particles")) {
            this.particles = new ShowParticles(main).runTaskTimer(main, 10, 10);
        }
    }

    /**
     * Gives the player a token for killing a mob based on the percentage set in the config.
     * For example, if the entity type's drop chance is set to 0, this will never do anything.
     * @param player The player.
     * @param entity The mob.
     */
    public boolean giveToken(Player player, EntityType entity) {
        if (!mobs.contains(entity)) {
            return false;
        }
        if (main.getConfig().getStringList("disabled-worlds").contains(player.getWorld().getName())) return false;
        if (!player.hasPermission("mobpowers.use." + entity.toString().toLowerCase().replace("_", ""))) return false;
        Random random = new Random();
        int chance = main.getConfig().getInt("powers." + entity.toString().toLowerCase() + ".drop-chance");
        if (random.nextInt(100)+1 < chance) { // gives us a number between 1 and 100. If it's less than or equal to "chance", do something. Larger the chance, more likely this will happen
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
        if (main.getConfig().getStringList("disabled-worlds").contains(player.getWorld().getName())) return false;
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
     * @return True if successful, false if not.
     */
    @SuppressWarnings("deprecation")
    public boolean useSuperPower(Player player, EntityType entity) {
        if (!player.hasPermission("mobpowers.use." + entity.toString().toLowerCase().replace("_", ""))) {
            player.sendMessage(ChatColor.RED + "You don't have access to this MobPower.");
            return false;
        }
        if (main.getConfig().getStringList("disabled-worlds").contains(player.getWorld().getName())) {
            player.sendMessage(ChatColor.RED + "MobPowers are disabled in this world.");
            return false;
        }
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
                player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 60*20, 1, main.getConfig().getBoolean("show-particles")));
                break;
            case HORSE:
                Random random = new Random();
                PotionEffectType type = random.nextInt(2) == 1 ? PotionEffectType.JUMP : PotionEffectType.SPEED;
                player.removePotionEffect(type);
                player.addPotionEffect(new PotionEffect(type, 60*20, random.nextInt(2)+1, main.getConfig().getBoolean("show-particles")));
                break;
            case SPIDER:
                player.removePotionEffect(PotionEffectType.SPEED);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60*20, 2, main.getConfig().getBoolean("show-particles")));
                break;
            case CAVE_SPIDER:
                spiders.put(player.getName(), System.currentTimeMillis()+60000);
                break;
            case SKELETON:
                skeletons.put(player.getName(), System.currentTimeMillis()+60000);
                break;
        }
        player.sendMessage(ChatColor.GREEN + "Enjoy your " + name + " powers!");
        return true;
    }

    /**
     * Checks if a mobpower is enabled.
     * @param type The entity.
     * @return True if enabled, false if not.
     */
    public boolean isPowerEnabled(EntityType type) {
        return main.getConfig().getBoolean("powers." + type.toString().toLowerCase() + ".enabled");
    }

    /**
     * Opens the MobPowers user interface where they can select a power or view their statistics.
     * @param player The player about to see their MobPowers UI.
     */
    public void openGUI(Player player) {
        if (main.getConfig().getStringList("disabled-worlds").contains(player.getWorld().getName())) {
            player.sendMessage(ChatColor.RED + "MobPowers are disabled in this world.");
            return;
        }
        Inventory inventory = main.getServer().createInventory(player, 9, "MobPowers Selector");
        HashMap<EntityType, Integer> tokenData = new HashMap<EntityType, Integer>();
        // +1 because we need to account for the statistics. / 2 because we need the same (or one more/less) air on both sides.
        // For example, if we have 4 mobs enabled, we'll need (9-(4+1)/2) = 2 airs on each side
        int slot = (9 - (mobs.size() + 1)) / 2;
        for (EntityType mob : mobs) {
            int tokens = getTokens(player, mob);
            inventory.setItem(slot, craftStack(mob, tokens, getPrice(mob), player.hasPermission("mobpowers.use." + mob.toString().toLowerCase().replace("_", ""))));
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
    public ItemStack craftStack(EntityType type, int tokens, int price, boolean hasPermission) {
        ItemStack itemStack = new ItemStack(materials.get(type), 1);
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<String>();
        meta.setDisplayName(ChatColor.DARK_GREEN + getEntityName(type)); // Turn "ENDERMAN" into "Enderman"
        if (hasPermission) {
            lore.add(ChatColor.GREEN + descriptions.get(type));
            lore.add(ChatColor.GOLD + "Price: " + (tokens < price ? ChatColor.RED : ChatColor.GREEN) + price);
        } else {
            lore.add(ChatColor.DARK_RED + "No permission");
        }
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * Gets the item containing statistics for MobPowers (number of tokens per mob).
     * @param data Hashmap containing data in the (EntityType mobType, Integer numberOfTokens) format
     * @return
     */
    public ItemStack craftStatisticsStack(HashMap<EntityType, Integer> data) {
        ItemStack itemStack = new ItemStack(Material.matchMaterial(main.getConfig().getString("statistics-material").replace(" ", " ")), 1);
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

    /**
     * Gets the entity's "nice" name - properly capitalized, without underscores.
     * @param type The entity type.
     * @return The nice name.
     */
    public String getEntityName(EntityType type) {
        return type.toString().toUpperCase().substring(0, 1) + type.toString().toLowerCase().substring(1).replace("_", " ");
    }

    /**
     * Returns the entity type which is represented by a certain material.
     * @param material The material.
     * @return The entity type.
     */
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

    /**
     * Checks if the player has Spider attack, ie. gives Poison to anybody they hit.
     * @param player The player.
     * @return True if yes, false if no.
     */
    public boolean hasCaveSpiderAttack(Player player) {
        if (!spiders.containsKey(player.getName())) return false;
        if (spiders.get(player.getName()) >= System.currentTimeMillis()) return true;
        spiders.remove(player.getName());
        return false;
    }

    /**
     * Checks if the player has Skeleton attack, ie. shoots arrows 2x faster.
     * @param player The player.
     * @return True if yes, false if no.
     */
    public boolean hasSkeletonAttack(Player player) {
        if (!skeletons.containsKey(player.getName())) return false;
        if (skeletons.get(player.getName()) >= System.currentTimeMillis()) return true;
        skeletons.remove(player.getName());
        return false;
    }
}
