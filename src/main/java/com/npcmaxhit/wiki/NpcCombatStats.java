package com.npcmaxhit.wiki;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class NpcCombatStats {

    private String name;
    private Integer hitpoints;
    private Integer combatLevel;
    private Integer maxHit;
    private boolean aggressive;
    private boolean poisonous;
    private AttackType attackType;
    private Map<String, Integer> styleMaxhits;

}
