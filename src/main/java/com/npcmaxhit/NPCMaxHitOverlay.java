package com.npcmaxhit;

import lombok.AllArgsConstructor;
import net.runelite.api.NPC;
import net.runelite.api.Point;
import net.runelite.client.game.npcoverlay.HighlightedNpc;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.util.Map;

@Singleton
public class NPCMaxHitOverlay extends Overlay {

    @Inject
    private NPCMaxHitPlugin plugin;

    @Override
    public Dimension render(Graphics2D graphics) {
        for (Map.Entry<NPC, HighlightedNpc> npc : plugin.getHighlightedNpcs().entrySet()) {
            renderText(graphics, npc.getKey(), npc.getValue());
        }
        return null;
    }

    private void renderText(Graphics2D graphics, NPC key, HighlightedNpc npc) {
        String text = "HIT ME!";
        Point textlocation = npc.getNpc().getCanvasTextLocation(graphics, text, key.getLogicalHeight() + 40);
        System.out.printf("Trying to render: %s @ %s%n", text, textlocation);
        OverlayUtil.renderTextLocation(graphics, textlocation, text, Color.CYAN);
    }
}
