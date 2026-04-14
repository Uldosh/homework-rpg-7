package com.narxoz.rpg.observer;

import java.util.Random;

public class LootDropper implements GameObserver{
    private static final String[][] PHASE_LOOT = {
            {},
            {},
            {
                    "Fractured Soul Shard",
                    "Tainted Ichor Flask",
                    "Corrupted Rune Tablet",
                    "Hollow Bone Charm"
            },
            {
                    "Cursed Dungeon Key Fragment",
                    "Voidstone Splinter",
                    "Wraith-Touched Amulet",
                    "Shadow Essence Crystal"
            }
    };


    private static final String[] DEFEAT_LOOT = {
            "Dungeon Master's Crown",
            "Eternal Darkness Gauntlets",
            "Heartstone of the Fallen Boss",
            "Tome of Forbidden Strategies",
            "Void-Forged Battle Axe"
    };

    private final Random rng = new Random();

    @Override
    public void onEvent(GameEvent event) {
        switch (event.getType()) {

            case BOSS_PHASE_CHANGED -> {
                int phase = event.getValue();
                if (phase >= 2 && phase <= 3) {
                    String[] table = PHASE_LOOT[phase];
                    String item    = table[rng.nextInt(table.length)];
                    System.out.printf("%n  [LOOT DROP] The boss's rage cracks the dungeon walls!%n");
                    System.out.printf("              Phase %d transition loot: ✦ %s ✦%n%n", phase, item);
                }
            }

            case BOSS_DEFEATED -> {
                String item = DEFEAT_LOOT[rng.nextInt(DEFEAT_LOOT.length)];
                System.out.println();
                System.out.println("  ╔════════════════════════════════════════════╗");
                System.out.println("  ║            ✦  LEGENDARY LOOT  ✦           ║");
                System.out.println("  ╠════════════════════════════════════════════╣");
                System.out.printf( "  ║  %-42s║%n", " » " + item);
                System.out.println("  ╚════════════════════════════════════════════╝");
                System.out.println();
            }

            default -> { }
        }
    }
}
