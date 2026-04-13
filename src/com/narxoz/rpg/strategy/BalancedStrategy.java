package com.narxoz.rpg.strategy;

public class BalancedStrategy implements CombatStrategy{
    @Override
    public int calculateDamage(int basePower) {
        // No modification — pure base attack
        return basePower;
    }

    @Override
    public int calculateDefense(int baseDefense) {
        // No modification — pure base defense
        return baseDefense;
    }

    @Override
    public String getName() {
        return "Balanced";
    }
}
