package com.npcmaxhit.wiki;

import java.util.HashMap;
import java.util.Map;

public class NpcCombatStats {

    private String name;
    private Integer hitpoints;
    private Integer combatLevel;
    private boolean aggressive;
    private boolean poisonous;
    private AttackType attackType;
    private Map<String, Integer> styleMaxhits = new HashMap<>();
}
