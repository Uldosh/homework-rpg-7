package com.narxoz.rpg.observer;
import java.util.*;

public class EventPublisher {
    private final List<GameObserver> observers = new ArrayList<>();

    public void addObserver(GameObserver observer) {
        if (observer == null) throw new IllegalArgumentException("Observer must not be null");
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    public List<GameObserver> getObservers() {
        return Collections.unmodifiableList(observers);
    }


    public void publish(GameEvent event) {
        if (event == null) throw new IllegalArgumentException("Event must not be null");
        List<GameObserver> snapshot = new ArrayList<>(observers);
        for (GameObserver observer : snapshot) {
            try {
                observer.onEvent(event);
            } catch (Exception e) {
                System.err.println("[EventPublisher] Observer threw an exception: " + e.getMessage());
            }
        }
    }
}
