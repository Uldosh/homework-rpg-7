package com.narxoz.rpg;
import com.narxoz.rpg.observer.DungeonBoss;
import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.engine.DungeonEngine;
import com.narxoz.rpg.engine.EncounterResult;
import com.narxoz.rpg.observer.*;
import com.narxoz.rpg.strategy.*;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Hero aragorn = new Hero("Aragorn",  160, 52, 22);
        Hero legolas  = new Hero("Legolas",  120, 62, 15);
        Hero gimli    = new Hero("Gimli",   200, 40, 32);

        aragorn.setStrategy(new BalancedStrategy());
        legolas.setStrategy(new AggressiveStrategy());
        gimli.setStrategy(new DefensiveStrategy());

        List<Hero> party = List.of(aragorn, legolas, gimli);

        EventPublisher publisher = new EventPublisher();

        DungeonBoss shadowlord = new DungeonBoss(
                "The Shadowlord", 360, 48, 18, publisher
        );

        BattleLogger       logger=new BattleLogger();
        AchievementTracker tracker=new AchievementTracker();
        PartySupport       support=new PartySupport(party);
        HeroStatusMonitor  monitor=new HeroStatusMonitor(party);
        LootDropper        loot=new LootDropper();

        publisher.addObserver(logger);
        publisher.addObserver(tracker);
        publisher.addObserver(support);
        publisher.addObserver(monitor);
        publisher.addObserver(loot);
        publisher.addObserver(event -> {
            if (event.getType() == GameEventType.BOSS_PHASE_CHANGED
                    && event.getValue() == 2
                    && legolas.isAlive()) {
                System.out.println();
                System.out.println("  [STRATEGY SWITCH] Legolas reads the battle shift —");
                System.out.printf ("    Legolas: [%s] → [Defensive]  (protecting himself in phase 2)%n",
                        legolas.getStrategy().getName());
                legolas.setStrategy(new DefensiveStrategy());
            }
        });

        DungeonEngine engine = new DungeonEngine(party, shadowlord, publisher);
        EncounterResult result = engine.run();

        System.out.println(result);

        System.out.println("  Achievements unlocked this run:");
        if (tracker.getUnlocked().isEmpty()) {
            System.out.println("    (none)");
        } else {
            tracker.getUnlocked().forEach(a -> System.out.println("    ★ " + a));
        }
        System.out.println();
        System.out.printf("  Total battle log entries: %d%n", logger.getEventCount());
    }
}
