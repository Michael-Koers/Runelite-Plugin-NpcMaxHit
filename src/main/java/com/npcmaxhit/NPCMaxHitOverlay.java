package com.npcmaxhit;

import com.npcmaxhit.config.NPCMaxHitConfig;
import com.npcmaxhit.wiki.NpcCombatStats;
import lombok.SneakyThrows;
import net.runelite.api.NPC;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

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
                renderTextOnNpc(graphics, npc.getKey(), "Failed to reach wiki", getZOffset(npc.getKey()));
            } else if (npc.getValue().isDone()) {
                renderMaxHitOnNpc(graphics, npc.getKey(), npc.getValue().get());
            } else {
                renderTextOnNpc(graphics, npc.getKey(), "Loading...", getZOffset(npc.getKey()));
            }
        }
        return null;
    }

    private void renderTextOnNpc(Graphics2D graphics, NPC npc, String text, int zOffset) {
        Point textLocation = npc.getCanvasTextLocation(graphics, text, zOffset);

        OverlayUtil.renderTextLocation(graphics, textLocation, text, config.textcolor());
    }

    private void renderMaxHitOnNpc(Graphics2D graphics, NPC npc, NpcCombatStats value) {
        // Support for multiple lines
        int zOffset = getZOffset(npc);

        renderTextOnNpc(graphics, npc, "Aggr.: " + value.isAggressive() + ", poison: " + value.isPoisonous(), zOffset);
        renderTextOnNpc(graphics, npc, "Style: " + value.getAttackType() + ", max hit: " + value.getMaxHit().toString(), zOffset+ config.textmargin());
    }

    private int getZOffset(NPC npc) {
        // Some Java 11 switch-case statement ugliness
        int zOffset;
        switch (config.location()) {
            case UNDER:
                zOffset = -40;
                break;
            case CENTER:
                zOffset = npc.getLogicalHeight() / 2;
                break;
            case ABOVE:
            default:
                zOffset = npc.getLogicalHeight() + 40;
                break;
        }
        return zOffset;
    }
}
