package de.itg.wahlkampf.utilities.inputhandling;

import de.itg.wahlkampf.Game;
import net.java.games.input.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ControllerListener {

    public static final List<Controller> CONTROLLER_LIST = new ArrayList<>();

    public ControllerListener() {
        CONTROLLER_LIST.addAll(Arrays.asList(ControllerEnvironment.getDefaultEnvironment().getControllers()));
    }

    public void handleController(Controller controller, List<Component> componentList) {
        controller.poll();
        final EventQueue eventQueue = controller.getEventQueue();
        final Event event = new Event();
        while (eventQueue.getNextEvent(event)) {
            if (Objects.equals(event.getComponent().getIdentifier().getName(), "pov")) {
                if (event.getComponent().getPollData() != 0) {
                    if (!componentList.contains(event.getComponent())) {
                        componentList.add(event.getComponent());
                    }
                } else {
                    componentList.removeIf(component -> component == event.getComponent());
                }
            } else {
                final float value = Math.abs(Math.round(event.getComponent().getPollData() * 100));
                if (value > 20) {
                    if (!componentList.contains(event.getComponent())) {
                        componentList.add(event.getComponent());
                    }
                } else if (value < 20) {
                    componentList.removeIf(component -> component == event.getComponent());
                }
            }
        }
    }
}
