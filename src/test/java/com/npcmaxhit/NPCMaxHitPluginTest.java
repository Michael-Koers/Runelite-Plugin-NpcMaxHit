package com.npcmaxhit;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class NPCMaxHitPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(NPCMaxHitPlugin.class);
		RuneLite.main(args);
	}
}