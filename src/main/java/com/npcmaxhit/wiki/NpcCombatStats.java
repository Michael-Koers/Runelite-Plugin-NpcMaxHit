package com.npcmaxhit.wiki;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

@Getter
@Builder
public class NpcCombatStats {

    private String name;
    private Integer hitpoints;
    private Integer combatLevel;
    private Integer maxHit;
    private boolean aggressive;
    private boolean poisonous;
    private String attackType;
    private Map<String, Integer> styleMaxhits;

}
