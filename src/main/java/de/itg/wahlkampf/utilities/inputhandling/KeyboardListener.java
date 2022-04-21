package de.itg.wahlkampf.utilities.inputhandling;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.object.AbstractGameObject;
import de.itg.wahlkampf.object.AbstractPlayerObject;
import net.java.games.input.*;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class KeyboardListener extends KeyAdapter {

    public static final List<Integer> KEY_LIST = new ArrayList<>();

    @Override
    public void keyPressed(KeyEvent e) {
        if (!KEY_LIST.contains(e.getKeyCode()))
            KEY_LIST.add(e.getKeyCode());
        super.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        KEY_LIST.removeIf(integer -> integer == e.getKeyCode());
        super.keyReleased(e);
    }
}
