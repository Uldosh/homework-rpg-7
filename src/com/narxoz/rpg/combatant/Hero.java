package com.narxoz.rpg.combatant;

public class Hero {

    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackPower;
    private final int defense;

    private CombatStrategy strategy;

    public Hero(String name, int hp, int attackPower, int defense) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
    }

    public String getName()        { return name; }
    public int getHp()             { return hp; }
    public int getMaxHp()          { return maxHp; }
    public int getAttackPower()    { return attackPower; }
    public int getDefense()        { return defense; }
    public boolean isAlive()       { return hp > 0; }

    public CombatStrategy getStrategy() { return strategy; }

    public void setStrategy(CombatStrategy strategy) {
        if (strategy == null) throw new IllegalArgumentException("Strategy must not be null");
        this.strategy = strategy;
    }

    public int getEffectiveDamage() {
        return (strategy != null) ? strategy.calculateDamage(attackPower) : attackPower;
    }

    public int getEffectiveDefense() {
        return (strategy != null) ? strategy.calculateDefense(defense) : defense;
    }

    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    @Override
    public String toString() {
        String stratName = (strategy != null) ? strategy.getName() : "none";
        return String.format("Hero[%s | HP=%d/%d | ATK=%d | DEF=%d | strategy=%s]",
                name, hp, maxHp, attackPower, defense, stratName);
    }
}
