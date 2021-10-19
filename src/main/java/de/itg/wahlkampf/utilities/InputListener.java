package de.itg.wahlkampf.utilities;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.object.Type;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class InputListener extends KeyAdapter {
    private final Game game;
    public static final List<Integer> KEY_LIST = new ArrayList<>();

    public InputListener(Game game) {
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // KEY_MAP.put(e.getKeyCode(), true);
        if (!KEY_LIST.contains(e.getKeyCode()))
            KEY_LIST.add(e.getKeyCode());
        game.objectHandler.getGameObjects().stream().filter(gameObject -> gameObject.getType().equals(Type.PLAYER)).forEach(gameObject -> gameObject.onKeyPressed(e));
        super.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // KEY_MAP.remove(e.getKeyCode());
        KEY_LIST.removeIf(integer -> integer.equals(e.getKeyCode()));
        game.objectHandler.getGameObjects().stream().filter(gameObject -> gameObject.getType().equals(Type.PLAYER)).forEach(gameObject -> gameObject.keyReleased(e));
        super.keyReleased(e);
    }
}
