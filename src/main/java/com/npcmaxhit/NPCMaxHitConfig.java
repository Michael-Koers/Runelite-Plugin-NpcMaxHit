package com.npcmaxhit;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("npcmaxhit")
public interface NPCMaxHitConfig extends Config {
    @ConfigItem(
            keyName = "greeting",
            name = "Welcome Greeting",
            description = "The message to show to the user when they login"
    )
    default String greeting() {
        return "Hello";
    }

    /**
     * TODO:
     *  - Remove Text after NPC dies
     *  - More custimization:
     *      - Images vs text vs color
     *  - Caching?
     *  - 1 npc vs all of the same type look up?
     *  - Better DI, cyclomatic dependency between overlay and plugin
     */
}
