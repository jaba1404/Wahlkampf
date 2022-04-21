package de.itg.wahlkampf.utilities;

import java.util.HashMap;
import java.util.Map;

public class KeyBind {
    private Tuple<String, String> controllerTuple;
    private int keyCode;

    public KeyBind(Tuple<String, String> controllerTuple, int keyCode) {
        this.controllerTuple = controllerTuple;
        this.keyCode = keyCode;
    }

    public KeyBind(Tuple<String, String> controllerTuple) {
        this.controllerTuple = controllerTuple;
    }

    public KeyBind(int keyCode) {
        this.keyCode = keyCode;
    }

    public Tuple<String, String> getControllerTuple() {
        return controllerTuple;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setControllerTuple(Tuple<String, String> controllerTuple) {
        this.controllerTuple = controllerTuple;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }
}
