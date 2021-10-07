package de.itg.wahlkampf.object;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.utilities.Math;

import java.util.ArrayList;

public class ObjectHandler {

    private ArrayList<GameObject> gameObjects = new ArrayList<>();

    public ObjectHandler() {

    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void addObject(GameObject gameObject) {
        this.getGameObjects().add(gameObject);
    }

    public void addObjects() {
    }
}
