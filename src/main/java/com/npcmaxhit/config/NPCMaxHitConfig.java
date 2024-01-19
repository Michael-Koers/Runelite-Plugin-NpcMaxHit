package com.npcmaxhit.config;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.*;

@ConfigGroup("npcmaxhit")
public interface NPCMaxHitConfig extends Config {
    @ConfigItem(
            keyName = "Location",
            name = "Max hit location",
            description = "Location on NPC where to show max hit information"
    )
    default InfoLocation location() {
        return InfoLocation.TOP;
    }

    @ConfigItem(
            keyName = "Color",
            name = "Max hit color",
            description = "Color of the NPC max hit information"
    )
    default Color textcolor() {
        return Color.CYAN;
    }

    /**
     * TODO:
     *  - More custimization:
     *      - Images vs text vs color
     *  - Caching?
     *  - 1 npc vs all of the same type look up?
     *  - Better DI, cyclomatic dependency between overlay and plugin
     */
}
