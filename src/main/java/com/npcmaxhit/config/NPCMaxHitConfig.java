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
        return InfoLocation.ABOVE;
    }

    @ConfigItem(
            keyName = "Color",
            name = "Information color",
            description = "Color of the NPC information text"
    )
    default Color textcolor() {
        return Color.CYAN;
    }

    @ConfigItem(
            keyName = "Margin",
            name = "Text margin",
            description = "Space between NPC information text lines"
    )
    default int textmargin() {
        return 40;
    }


    /**
     * TODO:
     *  - More custimization:
     *      - Images vs text vs color
     *  - Multiple lines of information
     *  - InfoBox?
     *  - Caching?
     *  - 1 npc vs all of the same type look up?
     *  - Better DI, cyclomatic dependency between overlay and plugin
     *  - Switch scraping technique, current doesn't support all wiki pages
     */
}
