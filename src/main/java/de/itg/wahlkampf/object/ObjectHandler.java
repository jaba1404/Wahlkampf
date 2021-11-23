package de.itg.wahlkampf.object;

import de.itg.wahlkampf.object.objects.StageBlock;
import de.itg.wahlkampf.object.players.Player;
import de.itg.wahlkampf.utilities.MathHelper;

import java.util.ArrayList;

public class ObjectHandler {

    private final ArrayList<AbstractGameObject> gameObjects = new ArrayList<>();
    private int x = 0;
    private int y = 500;

    public ObjectHandler() {
        for (int i = 0; i < 2; i++) {
            int width = MathHelper.getRandomInt(100, 600);
            addObject(new StageBlock(x, y, width, 20));
            x += MathHelper.getRandomInt(width, width + 200);
        }
        addObject(new Player());

    }

    public ArrayList<AbstractGameObject> getGameObjects() {
        return gameObjects;
    }

    public void addObject(AbstractGameObject gameObject) {
        this.getGameObjects().add(gameObject);
    }
}
