package de.itg.wahlkampf.event;

import de.itg.wahlkampf.Game;

public class EventManager {

    public void onEvent(Event event) {
        if (!event.isCancelled()) {
            Game.instance.onEvent(event);
        }
    }
}
