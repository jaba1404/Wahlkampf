package de.itg.wahlkampf;

import javax.swing.*;
import java.awt.*;

public class Window extends Canvas {

        public Window (String gameTitle, int width, int height, Game game) {
            //Creating Dimension
            Dimension dimension = new Dimension(width, height);

            //Creating JFrame
            JFrame jFrame = new JFrame(gameTitle);

            //Handle sizing of JFrame
            jFrame.setPreferredSize(dimension);
            jFrame.setMaximumSize(dimension);
            jFrame.setMinimumSize(dimension);

            jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            jFrame.setResizable(false);
            jFrame.setLocationRelativeTo(null);
            jFrame.add(game);
            jFrame.setVisible(true);
            game.start();
        }
    }
