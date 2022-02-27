package de.itg.wahlkampf.utilities;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.object.AbstractGameObject;
import de.itg.wahlkampf.object.AbstractPlayerObject;
import net.java.games.input.*;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class InputListener extends KeyAdapter {

    private final Game game;
    public static final List<Integer> KEY_LIST = new ArrayList<>();
    private static final Predicate<AbstractGameObject> PLAYER_OBJECT_PREDICATE = gameObject -> gameObject instanceof AbstractPlayerObject;
    public static final List<Controller> CONTROLLER_LIST = new ArrayList<>();


    public InputListener(Game game) {
        this.game = game;
        CONTROLLER_LIST.addAll(Arrays.asList(ControllerEnvironment.getDefaultEnvironment().getControllers()));
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if (!KEY_LIST.contains(e.getKeyCode()))
            KEY_LIST.add(e.getKeyCode());
        game.getObjectHandler().getGameObjects().stream().filter(PLAYER_OBJECT_PREDICATE).forEach(gameObject -> gameObject.onKeyPressed(e));
        super.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        KEY_LIST.removeIf(integer -> integer == e.getKeyCode());
        game.getObjectHandler().getGameObjects().stream().filter(PLAYER_OBJECT_PREDICATE).forEach(gameObject -> gameObject.keyReleased(e));
        super.keyReleased(e);
    }
}
