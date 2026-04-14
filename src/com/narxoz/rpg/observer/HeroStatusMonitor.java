package com.narxoz.rpg.observer;
import com.narxoz.rpg.combatant.Hero;

import java.util.List;

public class HeroStatusMonitor implements GameObserver{
    private final List<Hero> party;

    public HeroStatusMonitor(List<Hero> party) {
        this.party = party;
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() != GameEventType.HERO_LOW_HP
                && event.getType() != GameEventType.HERO_DIED) return;

        String trigger = (event.getType() == GameEventType.HERO_DIED)
                ? "☠  " + event.getSourceName() + " has FALLEN!"
                : "⚠  " + event.getSourceName() + " is CRITICALLY LOW!";

        System.out.println();
        System.out.println("  ┌─── PARTY STATUS ─────────────────────────────────┐");
        System.out.println("  │  Trigger: " + trigger);
        System.out.println("  │");
        for (Hero h : party) {
            String bar    = hpBar(h);
            String status = !h.isAlive()        ? "DEAD   "
                    : isLow(h)            ? "LOW HP "
                    : "OK     ";
            System.out.printf("  │  %-14s  %s  [%s] %d/%d HP%n",
                    h.getName(), bar, status, h.getHp(), h.getMaxHp());
        }
        System.out.println("  └──────────────────────────────────────────────────┘");
        System.out.println();
    }

    private boolean isLow(Hero h) {
        return h.isAlive() && h.getHp() < h.getMaxHp() * 0.30;
    }

    private String hpBar(Hero h) {
        if (!h.isAlive()) return "[##########]";
        int filled = (int)Math.round(10.0 * h.getHp() / h.getMaxHp());
        filled = Math.max(0, Math.min(10, filled));
        return "[" + "█".repeat(filled) + "░".repeat(10 - filled) + "]";
    }
}
