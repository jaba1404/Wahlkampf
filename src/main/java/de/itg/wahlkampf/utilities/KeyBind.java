package de.itg.wahlkampf.utilities;

import java.util.HashMap;
import java.util.Map;

public class KeyBind {
    private Tuple<String, Float> controllerTuple;
    private int keyCode;

    public KeyBind(Tuple<String, Float> controllerTuple, int keyCode) {
        this.controllerTuple = controllerTuple;
        this.keyCode = keyCode;
    }

    public Tuple<String, Float> getControllerTuple() {
        return controllerTuple;
    }

    public int getKeyCode() {
        return keyCode;
    }
}
