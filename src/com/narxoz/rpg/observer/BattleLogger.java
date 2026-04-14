package com.narxoz.rpg.observer;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class BattleLogger implements GameObserver{
    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    private int eventCount = 0;

    @Override
    public void onEvent(GameEvent event) {
        eventCount++;
        String timestamp = LocalTime.now().format(TIME_FMT);
        String icon = iconFor(event.getType());

        System.out.printf("[LOG #%03d | %s] %s  %-22s | source=%-18s | value=%d%n",
                eventCount,
                timestamp,
                icon,
                event.getType(),
                event.getSourceName(),
                event.getValue());
    }

    private String iconFor(GameEventType type) {
        return switch (type) {
            case ATTACK_LANDED      -> "⚔ ";
            case HERO_LOW_HP        -> "💛";
            case HERO_DIED          -> "💀";
            case BOSS_PHASE_CHANGED -> "🔴";
            case BOSS_DEFEATED      -> "🏆";
        };
    }

    public int getEventCount() { return eventCount; }
}
