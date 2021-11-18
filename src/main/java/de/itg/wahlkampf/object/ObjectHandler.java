package de.itg.wahlkampf.object;

import de.itg.wahlkampf.object.objects.StageBlock;
import de.itg.wahlkampf.object.players.Player;
import de.itg.wahlkampf.object.stage.Stage;
import de.itg.wahlkampf.utilities.Math;

import java.util.ArrayList;

public class ObjectHandler {

    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    private final Math math = new Math();
    public Stage stage;
    private int x = 0;
    private int y = 500;

    public ObjectHandler() {
        for (int i = 0; i < 2; i++) {
            int width = math.getRandomInt(100, 600);
            addObject(new StageBlock(x, y, width, 20));
            if(i == 0)
            stage = new Stage(x, width, 0, 0, y, 0, 0, 0);
            if (i == 1) {
                stage.setX1(x);
                stage.setWidth1(width);
                stage.setY1(y);
            }else {

            }
            x += math.getRandomInt(width, width + 200);
        }
        addObject(new Player());

    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void addObject(GameObject gameObject) {
        this.getGameObjects().add(gameObject);
    }
}
