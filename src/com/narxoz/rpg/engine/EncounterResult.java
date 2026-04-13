package com.narxoz.rpg.engine;
import java.util.*;

public class EncounterResult {

    private final boolean heroesWon;
    private final int roundsPlayed;
    private final List<Hero> survivingHeroes;
    private final int bossHpRemaining;

    public EncounterResult(boolean heroesWon, int roundsPlayed, List<Hero> survivingHeroes, bossHpRemaining) {
        this.heroesWon = heroesWon;
        this.roundsPlayed = roundsPlayed;
        this.survivingHeroes = List.copyOf(survivingHeroes);
        this.bossHpRemaining  = bossHpRemaining;
    }

    public boolean isHeroesWon()      { return heroesWon; }
    public int getRoundsPlayed()      { return roundsPlayed; }
    public List<Hero> getSurvivingHeroes()  { return survivingHeroes; }
    public int getBossHpRemaining()         { return bossHpRemaining; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nENCOUNTER RESULT");
        sb.append(String.format("  Outcome      : %s%n",
                heroesWon ? "HEROES VICTORIOUS!" : "PARTY WIPED — Boss wins."));
        sb.append(String.format("  Rounds fought: %d%n", roundsPlayed));
        sb.append(String.format("  Boss HP left : %d%n", bossHpRemaining));
        if (!survivingHeroes.isEmpty()) {
            sb.append("  Survivors    : ");
            survivingHeroes.forEach(h ->
                    sb.append(h.getName()).append(" (").append(h.getHp()).append(" HP)  "));
            sb.append("\n");
        } else {
            sb.append("  Survivors    : none\n");
        }
        return sb.toString();
    }
}
