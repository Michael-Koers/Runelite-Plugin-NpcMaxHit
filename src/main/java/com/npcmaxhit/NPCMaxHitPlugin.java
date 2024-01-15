package com.npcmaxhit;

import com.google.inject.Provides;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.npcoverlay.HighlightedNpc;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@PluginDescriptor(
        name = "NPC Max Hit", description = "Plugin for displaying max hit of NPC's"
)
@Singleton
public class NPCMaxHitPlugin extends Plugin {

    @Inject
    private Client client;

    @Inject
    private NPCMaxHitConfig config;

    @Inject
    private NPCMaxHitOverlay npcMaxHitOverlay;

    @Inject
    private OverlayManager overlayManager;

    @Getter
    private final Map<NPC, HighlightedNpc> highlightedNpcs = new HashMap<>();

    @Override
    protected void startUp() {
        overlayManager.add(npcMaxHitOverlay);
    }

    @Override
    protected void shutDown() {
        overlayManager.remove(npcMaxHitOverlay);
        this.highlightedNpcs.clear();
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {

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
                .onClick((entry) -> this.lookup(entry, npc));
    }

    private void lookup(MenuEntry entry, NPC npc) {
        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Find max hit of: " + npc.getName(), null);
        this.highlightedNpcs.put(npc, HighlightedNpc.builder()
                .npc(npc)
                .name(true)
                .highlightColor(Color.CYAN)
                .build());
    }

    @Provides
    NPCMaxHitConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(NPCMaxHitConfig.class);
    }
}
