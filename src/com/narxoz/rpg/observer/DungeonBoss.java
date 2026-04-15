package com.narxoz.rpg.observer;
import com.narxoz.rpg.strategy.*;

public class DungeonBoss implements GameObserver{
    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackPower;
    private final int defense;

    private int currentPhase = 1;

    private CombatStrategy strategy;

    private final CombatStrategy phase1Strategy = new BossPhase1Strategy();
    private final CombatStrategy phase2Strategy = new BossPhase2Strategy();
    private final CombatStrategy phase3Strategy = new BossPhase3Strategy();

    private final EventPublisher publisher;

    public DungeonBoss(String name, int hp, int attackPower, int defense,
                       EventPublisher publisher) {
        this.name        = name;
        this.hp          = hp;
        this.maxHp       = hp;
        this.attackPower = attackPower;
        this.defense     = defense;
        this.publisher   = publisher;

        this.strategy    = phase1Strategy;

        publisher.addObserver(this);
    }

    public String getName()          { return name; }
    public int getHp()               { return hp; }
    public int getMaxHp()            { return maxHp; }
    public int getAttackPower()      { return attackPower; }
    public int getDefense()          { return defense; }
    public int getCurrentPhase()     { return currentPhase; }
    public CombatStrategy getStrategy() { return strategy; }
    public boolean isAlive()         { return hp > 0; }

    public int getEffectiveDamage() {
        return strategy.calculateDamage(attackPower);
    }

    public int getEffectiveDefense() {
        return strategy.calculateDefense(defense);
    }

    public void takeDamage(int amount) {
        if (!isAlive()) return;

        int hpBefore = hp;
        hp = Math.max(0, hp - amount);

        checkPhaseTransition(hpBefore);

        if (hp == 0) {
            publisher.publish(new GameEvent(GameEventType.BOSS_DEFEATED, name, 0));
        }
    }

    private void checkPhaseTransition(int hpBefore) {
        double pctBefore = (double) hpBefore / maxHp;
        double pctAfter  = (double) hp       / maxHp;

        if (currentPhase == 1 && pctAfter < 0.60 && hp > 0) {
            currentPhase = 2;
            publisher.publish(new GameEvent(GameEventType.BOSS_PHASE_CHANGED, name, 2));
        }
        else if (currentPhase == 2 && pctAfter < 0.30 && hp > 0) {
            currentPhase = 3;
            publisher.publish(new GameEvent(GameEventType.BOSS_PHASE_CHANGED, name, 3));
        }
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() != GameEventType.BOSS_PHASE_CHANGED) return;
        if (!event.getSourceName().equals(name)) return;

        int newPhase = event.getValue();
        CombatStrategy newStrategy = switch (newPhase) {
            case 2  -> phase2Strategy;
            case 3  -> phase3Strategy;
            default -> phase1Strategy;
        };

        String oldName = strategy.getName();
        strategy = newStrategy;

        System.out.printf("%n  *** %s enters Phase %d! Strategy: [%s] → [%s] ***%n%n",
                name, newPhase, oldName, strategy.getName());
    }

    @Override
    public String toString() {
        return String.format("DungeonBoss[%s | HP=%d/%d | Phase=%d | strategy=%s]",
                name, hp, maxHp, currentPhase, strategy.getName());
    }
}
