package com.narxoz.rpg.engine;
import com.narxoz.rpg.boss.DungeonBoss;
import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.observer.*;

import java.util.List;


public class DungeonEngine {
    private static final int MAX_ROUNDS = 100;

    private final List<Hero>   party;
    private final DungeonBoss  boss;
    private final EventPublisher publisher;

    public DungeonEngine(List<Hero> party, DungeonBoss boss, EventPublisher publisher) {
        this.party     = party;
        this.boss      = boss;
        this.publisher = publisher;
    }

    public EncounterResult run() {
        System.out.println("═".repeat(60));
        System.out.println("  THE CURSED DUNGEON — BOSS ENCOUNTER BEGINS");
        System.out.println("═".repeat(60));
        printRoster();

        int round = 0;

        while (boss.isAlive() && anyAlive() && round < MAX_ROUNDS) {
            round++;
            System.out.println("\n" + "─".repeat(60));
            System.out.printf("  ROUND %d  |  Boss: %s  HP=%d/%d  Phase=%d  [%s]%n",
                    round,
                    boss.getName(), boss.getHp(), boss.getMaxHp(),
                    boss.getCurrentPhase(), boss.getStrategy().getName());
            System.out.println("─".repeat(60));

            for (Hero hero : party) {
                if (!hero.isAlive() || !boss.isAlive()) continue;

                int rawDamage  = hero.getEffectiveDamage();
                int bossDefense = boss.getEffectiveDefense();
                int netDamage  = Math.max(1, rawDamage - bossDefense);

                boss.takeDamage(netDamage);

                System.out.printf("  %s [%s] attacks %s for %d dmg (raw=%d, def=%d) → Boss HP=%d%n",
                        hero.getName(), hero.getStrategy().getName(),
                        boss.getName(), netDamage, rawDamage, bossDefense,
                        boss.getHp());

                publisher.publish(new GameEvent(
                        GameEventType.ATTACK_LANDED, hero.getName(), netDamage));

                if (!boss.isAlive()) break;
            }

            if (!boss.isAlive()) continue;

            for (Hero hero : party) {
                if (!hero.isAlive()) continue;

                int rawDamage   = boss.getEffectiveDamage();
                int heroDefense = hero.getEffectiveDefense();
                int netDamage   = Math.max(1, rawDamage - heroDefense);

                hero.takeDamage(netDamage);

                System.out.printf("  %s [%s] attacks %s for %d dmg (raw=%d, def=%d) → Hero HP=%d/%d%n",
                        boss.getName(), boss.getStrategy().getName(),
                        hero.getName(), netDamage, rawDamage, heroDefense,
                        hero.getHp(), hero.getMaxHp());

                publisher.publish(new GameEvent(
                        GameEventType.ATTACK_LANDED, boss.getName(), netDamage));

                if (!hero.isAlive()) {
                    publisher.publish(new GameEvent(
                            GameEventType.HERO_DIED, hero.getName(), 0));
                } else if (hero.getHp() < hero.getMaxHp() * 0.30) {
                    publisher.publish(new GameEvent(
                            GameEventType.HERO_LOW_HP, hero.getName(), hero.getHp()));
                }
            }
        }

        List<Hero> survivors = party.stream().filter(Hero::isAlive).toList();
        boolean heroesWon    = !boss.isAlive();

        if (!heroesWon && !anyAlive()) {
            System.out.println("\n  The entire party has been wiped out...");
        } else if (!heroesWon) {
            System.out.println("\n  Max rounds reached — the encounter is inconclusive.");
        }

        return new EncounterResult(heroesWon, round, survivors, boss.getHp());
    }

    private boolean anyAlive() {
        return party.stream().anyMatch(Hero::isAlive);
    }

    private void printRoster() {
        System.out.println();
        System.out.println("  PARTY:");
        for (Hero h : party) {
            System.out.printf("    • %-14s  HP=%d  ATK=%d  DEF=%d  strategy=[%s]%n",
                    h.getName(), h.getMaxHp(), h.getAttackPower(),
                    h.getDefense(), h.getStrategy().getName());
        }
        System.out.printf("%n  BOSS: %-14s  HP=%d  ATK=%d  DEF=%d  strategy=[%s]%n%n",
                boss.getName(), boss.getMaxHp(), boss.getAttackPower(),
                boss.getDefense(), boss.getStrategy().getName());
    }
}
