package com.narxoz.rpg.strategy;

public class BossPhase3Strategy implements CombatStrategy{
    @Override
    public int calculateDamage(int basePower) {
        return basePower * 2;
    }

    @Override
    public int calculateDefense(int baseDefense) {
        return (int)(baseDefense * 0.4);
    }

    @Override
    public String getName() {
        return "Death's Berserk (Phase 3)";
    }
}
