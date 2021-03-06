DeprecatedNether's ten.java submission
==============================

[![ten.java](https://cdn.mediacru.sh/hu4CJqRD7AiB.svg)](https://tenjava.com/)

This is a submission for the 2014 ten.java contest.

- __Theme:__ How can energy be harnessed and used in the Minecraft world?
- __Time:__ Time 2 (7/12/2014 09:00 to 7/12/2014 19:00 UTC)
- __MC Version:__ 1.7.9-R0.2 (latest beta)
- __Stream URL:__ n/a

---------------------------------------

Compilation
-----------

- Download & Install [Maven 3](http://maven.apache.org/download.html)
- Clone the repository: `git clone https://github.com/tenjava/DeprecatedNether-t2`
- Compile and create the plugin package using Maven: `mvn`

Maven will download all required dependencies and build a ready-for-use plugin package!

---------------------------------------

Usage
-----

1. Install plugin
2. Start the server.
3. You may choose to disable or reconfigure certain powers in the configuration file.
4. Kill mobs and collect their tokens. Every time a mob is killed by a player, it has a chance of giving that player one token.
5. Once you have enough tokens, you may activate the Mob Power by issuing the command `/mobpowers` (or right-clicking the MobPowers Wand which you can get with `/mobpowers wand`)

Mob powers:
* **Creeper** allows you to create an explosion.
* **Enderman** allows you to teleport.
* **Ghast** allows you to shoot a fireball.
* **Squid** allows you to breathe underwater for one minute.
* **Horse** allows you to jump higher than normally for one minute.
* **Spider** allows you to ~~walk~~ crawl faster.
* **Skeleton** allows you to shoot arrows 2x faster than normally for 1 minute.
* **Cave Spider** allows you to poison anybody you hit (Poison 1, for 20 seconds) for 1 minute.

This way, when you kill a mob, none of its **energy** goes to waste - you are able to **harness it and reuse it** to continue doing what the mob was doing before... well, before the player came along and brutally murdered it :)

Configuration
-------------

In the configuration file, you may choose to configure certain settings for each mob power:
* **enabled** allows you to enable or disable that power.
* **drop-chance** allows you to set a percentage chance of the drop. For example, if you set this to 50, on average every other mob of the same type will give you a token.
* **price** specifies how many tokens you need to be able to activate the mob's power.
* **description** specifies the description shown in the user interface.
* **material** is the material which will represent that mob in the user interface. This must be a valid [Material](http://jd.bukkit.org/beta/apidocs/org/bukkit/Material.html) - however it does not have to be all UPPERCASE and the underscores can be replaced with spaces. For example, "redstone ore" and "redstone_ore" would be valid whereas "redstoneore" would not be.

The following can also be configured:
* **ignore-creative-kills** - Choose whether to ignore kills made by players that were, at the time, in Creative mode.
* **disabled-worlds** - Do not allow players to gain or use MobPowers tokens in these worlds.
* **statistics-material** - The material that shows the player's statistics in the user interface. This must be a valid [Material](http://jd.bukkit.org/beta/apidocs/org/bukkit/Material.html) - however it does not have to be all UPPERCASE and the underscores can be replaced with spaces. For example, "redstone ore" and "redstone_ore" would be valid whereas "redstoneore" would not be.
* **show-particles** - Toggle whether you players with long-term effects (spider, cave spider, horse, squid) should have particles displayed.

Permissions
-----------

The plugin uses the following permission nodes:
* **mobpowers.use**: Allow the player to gain tokens for killing mobs and use those tokens. *Default: all players*
* **mobpowers.use.<mob name>**: Allow the player to gain and use tokens for this specific mob (this is automatically true if the player has the permission node `mobpowers.use` - use this only if you want the player to have access to only specific mob powers or want to negate the permission). For mobs whose names consist of several words (cave spider), use `cavespider`. *Default: all players*
* **mobpowers.wand**: Allows the player to use `/mobpowers wand` which gives them a special stick. *Default: operators only*
