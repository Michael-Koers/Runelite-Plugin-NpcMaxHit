package com.npcmaxhit;

import net.runelite.api.NPC;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.awt.*;

public class NPCMaxHitOverlay extends Overlay {

    private final NPCMaxHitPlugin plugin;

    private final NPCMaxHitConfig config;

    @Inject
    NPCMaxHitOverlay(NPCMaxHitPlugin plugin, NPCMaxHitConfig config) {
        this.plugin = plugin;
        this.config = config;
        this.setPosition(OverlayPosition.DYNAMIC);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        for (NPC npc : plugin.getTaggedNPCs()) {
            renderMaxHitOnNpc(graphics, npc);
        }
        return null;
    }

    private void renderMaxHitOnNpc(Graphics2D graphics, NPC npc) {
        String text = "HIT ME!";
        String npcName = Text.removeTags(npc.getName());
        int zOffset;

        // Some Java 11 switch-case statement ugliness
        switch (config.location()) {
            case BOTTOM:
                zOffset = 0;
                break;
            case CENTER:
                zOffset = npc.getLogicalHeight() / 2;
                break;
            case TOP:
            default:
                zOffset = npc.getLogicalHeight() + 40;
                break;
        }

        Point textlocation = npc.getCanvasTextLocation(graphics, npcName, zOffset);
        OverlayUtil.renderTextLocation(graphics, textlocation, text, config.textcolor());
    }
}
