package de.itg.wahlkampf.menu.optionui;

import java.awt.*;

public interface IComponent {

     void drawScreen(Graphics graphics, int mouseX, int mouseY);
     void mouseClicked(int mouseX, int mouseY, int mouseButton);
     void mouseDragged(int mouseX, int mouseY, int mouseButton);
     void mousePressed(int mouseX, int mouseY, int mouseButton);
     void mouseReleased(int mouseX, int mouseY);


    }
