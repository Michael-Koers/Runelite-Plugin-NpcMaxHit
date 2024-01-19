package com.npcmaxhit;

import com.npcmaxhit.config.NPCMaxHitConfig;
import com.npcmaxhit.wiki.NpcCombatStats;
import lombok.SneakyThrows;
import net.runelite.api.NPC;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.awt.*;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class NPCMaxHitOverlay extends Overlay {

    private final NPCMaxHitPlugin plugin;

    private final NPCMaxHitConfig config;

    @Inject
    NPCMaxHitOverlay(NPCMaxHitPlugin plugin, NPCMaxHitConfig config) {
        this.plugin = plugin;
        this.config = config;
        this.setPosition(OverlayPosition.DYNAMIC);
    }

    @SneakyThrows
    @Override
    public Dimension render(Graphics2D graphics) {
        for (Map.Entry<NPC, CompletableFuture<NpcCombatStats>> npc : plugin.getTaggedNPCs().entrySet()) {
            if (npc.getValue().isCompletedExceptionally()) {
                renderTextOnNpc(graphics, npc.getKey(), "Failed to retrieve information from wiki");
            } else if (npc.getValue().isDone()) {
                renderMaxHitOnNpc(graphics, npc.getKey(), npc.getValue().get());
            } else {
                renderTextOnNpc(graphics, npc.getKey(), "Loading...");
            }
        }
        return null;
    }

    private void renderTextOnNpc(Graphics2D graphics, NPC npc, String text) {
        String npcName = Text.removeTags(npc.getName());
        Point textlocation = npc.getCanvasTextLocation(graphics, npcName, getzOffset(npc));
        OverlayUtil.renderTextLocation(graphics, textlocation, text, config.textcolor());
    }

    private void renderMaxHitOnNpc(Graphics2D graphics, NPC npc, NpcCombatStats value) {
        renderTextOnNpc(graphics, npc, value.getMaxHit().toString());
    }

    private int getzOffset(NPC npc) {
        // Some Java 11 switch-case statement ugliness
        int zOffset;
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
        return zOffset;
    }
}
