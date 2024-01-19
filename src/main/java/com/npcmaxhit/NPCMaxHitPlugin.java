package com.npcmaxhit;

import com.google.inject.Provides;
import com.npcmaxhit.config.NPCMaxHitConfig;
import com.npcmaxhit.wiki.NpcCombatStats;
import com.npcmaxhit.wiki.OsrsWikiScraper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.NpcDespawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Slf4j
@PluginDescriptor(
        name = "NPC Max Hit", description = "Plugin for displaying max hit of NPC's"
)
@Singleton
public class NPCMaxHitPlugin extends Plugin {

    @Inject
    private Client client;

    @Inject
    private NPCMaxHitOverlay npcMaxHitOverlay;

    @Inject
    private OverlayManager overlayManager;

    @Getter
    private final Map<NPC, CompletableFuture<NpcCombatStats>> taggedNPCs = new HashMap<>();

    @Override
    protected void startUp() {
        overlayManager.add(npcMaxHitOverlay);
    }

    @Override
    protected void shutDown() {
        overlayManager.remove(npcMaxHitOverlay);
        taggedNPCs.clear();
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        if (gameStateChanged.getGameState().equals(GameState.HOPPING) ||
                gameStateChanged.getGameState().equals(GameState.LOGGED_IN)) {
            taggedNPCs.clear();
        }
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event) {
        final MenuEntry menuEntry = event.getMenuEntry();
        final MenuAction menuAction = menuEntry.getType();
        final NPC npc = menuEntry.getNpc();

        // Don't do anything unless we are examining a seemingly attackable NPC
        if (npc == null
                || npc.getName() == null
                || npc.getCombatLevel() == 0
                || menuAction != MenuAction.EXAMINE_NPC) {
            return;
        }

        client.createMenuEntry(-2)
                .setOption("Max Hit")
                .setTarget(event.getTarget())
                .setIdentifier(event.getIdentifier())
                .setType(MenuAction.RUNELITE)
                .onClick((entry) -> showMaxHit(entry, npc));
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned npcDespawned) {
        taggedNPCs.remove(npcDespawned.getNpc());
    }

    private void showMaxHit(MenuEntry entry, NPC npc) {
        System.out.println("Clicked on " + npc.getName() + ": " + npc.getId() + "/" + npc.getIndex());

        if (taggedNPCs.containsKey(npc)) {
            taggedNPCs.remove(npc);
            return;
        }

        CompletableFuture<NpcCombatStats> npcCombatStats = OsrsWikiScraper.getNpcCombatStats(npc.getName(), npc.getId());
        taggedNPCs.put(npc, npcCombatStats);
    }

    @Provides
    NPCMaxHitConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(NPCMaxHitConfig.class);
    }
}
