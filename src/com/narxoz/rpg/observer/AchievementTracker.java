package com.narxoz.rpg.observer;

import java.util.LinkedHashSet;
import java.util.Set;

public class AchievementTracker implements GameObserver{
    private final Set<String> unlocked = new LinkedHashSet<>();
    private int heroDeaths      = 0;
    private int attacksLanded   = 0;
    private boolean bossDefeated = false;

    @Override
    public void onEvent(GameEvent event) {
        switch (event.getType()) {

            case ATTACK_LANDED -> {
                attacksLanded++;

                if (attacksLanded == 1) {
                    unlock("First Blood",
                            "The first strike of the encounter has landed!");
                }

                if (event.getValue() >= 60) {
                    unlock("Overkill",
                            event.getSourceName() + " dealt a devastating blow of "
                                    + event.getValue() + " damage!");
                }
            }

            case HERO_DIED -> {
                heroDeaths++;
                unlock("Martyr's Courage",
                        event.getSourceName() + " gave their life for the party.");
            }

            case BOSS_DEFEATED -> {
                bossDefeated = true;


                if (heroDeaths == 0) {
                    unlock("Flawless Victory",
                            "The boss was slain without losing a single hero!");
                }

                if (attacksLanded >= 50) {
                    unlock("Persistent Party",
                            "The party landed " + attacksLanded + " attacks before the boss fell.");
                }
            }

            default -> {  }
        }
    }

    private void unlock(String name, String description) {
        if (!unlocked.contains(name)) {
            unlocked.add(name);
            System.out.println();
            System.out.println("  ★ ACHIEVEMENT UNLOCKED: «" + name + "»");
            System.out.println("    " + description);
            System.out.println();
        }
    }

    public Set<String> getUnlocked() { return Set.copyOf(unlocked); }
}
