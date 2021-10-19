package de.itg.wahlkampf.object;

import de.itg.wahlkampf.object.objects.StageBlock;
import de.itg.wahlkampf.object.players.Player;
import de.itg.wahlkampf.utilities.Math;

import java.util.ArrayList;

public class ObjectHandler {

    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    private final Math math = new Math();

    private int x = 0;
    private int y = 500;

    public ObjectHandler() {
        addObject(new Player());
        for (int i = 0; i < 2; i++) {
            int width = math.getRandomInt(100, 600);
            addObject(new StageBlock(x, y, width, 20));
            x += math.getRandomInt(width, width + 200);
        }
    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void addObject(GameObject gameObject) {
        this.getGameObjects().add(gameObject);
    }
}
