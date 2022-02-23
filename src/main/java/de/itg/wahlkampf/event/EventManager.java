package de.itg.wahlkampf.event;

import de.itg.wahlkampf.Game;

public class EventManager {

    public void onEvent(AbstractEvent abstractEvent) {
        if (!abstractEvent.isCancelled()) {
            Game.instance.onEvent(abstractEvent);
        }
    }
}
