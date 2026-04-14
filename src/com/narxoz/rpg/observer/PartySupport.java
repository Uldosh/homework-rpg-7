package com.narxoz.rpg.observer;
import com.narxoz.rpg.combatant.Hero;

import java.util.List;
import java.util.Random;

public class PartySupport implements GameObserver{
    private static final int HEAL_AMOUNT = 25;

    private final List<Hero> party;
    private final Random     rng;
    public PartySupport(List<Hero> party) {
        this.party = party;
        this.rng   = new Random();
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() != GameEventType.HERO_LOW_HP) return;

        List<Hero> living = party.stream().filter(Hero::isAlive).toList();
        if (living.isEmpty()) return;

        Hero target = living.get(rng.nextInt(living.size()));
        int before  = target.getHp();
        target.heal(HEAL_AMOUNT);
        int healed  = target.getHp() - before;

        System.out.printf("  [PARTY SUPPORT] %s is critically wounded! "
                        + "%s channels healing energy → %s restored %d HP (now %d/%d).%n",
                event.getSourceName(),
                "The party's spirit",
                target.getName(),
                healed,
                target.getHp(),
                target.getMaxHp());
    }
}
