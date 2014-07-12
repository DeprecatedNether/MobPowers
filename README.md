DeprecatedNether's ten.java submission
==============================

[![ten.java](https://cdn.mediacru.sh/hu4CJqRD7AiB.svg)](https://tenjava.com/)

This is a submission for the 2014 ten.java contest.

- __Theme:__ How can energy be harnessed and used in the Minecraft world?
- __Time:__ Time 2 (7/12/2014 09:00 to 7/12/2014 19:00 UTC)
- __MC Version:__ 1.7.9 (latest Bukkit beta)
- __Stream URL:__ https://twitch.tv/DeprecatedNether

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
4. Kill mobs and collect their tokens.
5. Once you get enough tokens, issue the command `/mobpowers` to select which power to active (alternatively, give yourself a MobPowers wand by issuing `/mobpowers wand`)

Mob powers:
* **Creeper** allows you to create an explosion.
* **Enderman** allows you to teleport.
* **Ghast** allows you to shoot a fireball.
* **Squid** allows you to breathe underwater for one minute.
* **Horse** allows you to jump higher than normally for one minute.

This way, when you kill a mob, none of its **energy** goes to waste - you are able to **harness it and reuse it** to continue doing what the mob was doing before... well, before the player came along and brutally murdered it :)

In the configuration file, you may choose to configure certain settings for each mob power:
* **enabled** allows you to enable or disable that power.
* **drop-chance** allows you to set a percentage chance of the drop. For example, if you set this to 50, on average every other mob of the same type will give you a token.
* **price** specifies how many tokens you need to be able to activate the mob's power.
