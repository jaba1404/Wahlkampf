package de.itg.wahlkampf.utilities.particlesystem;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.utilities.TimeHelper;

import java.awt.*;

public abstract class Particle {
    private int x, y, size;
    private Color color;
    private ParticleType particleType;
    private Graphics graphics;
    private TimeHelper timeHelper;


    public Particle(int x, int y, int size, Color color, ParticleType particleType) {
        timeHelper = new TimeHelper();
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
        this.particleType = particleType;
        graphics = Game.instance.getGraphics();
    }

    public abstract void drawParticle(Graphics graphics);

    public abstract void fadeParticle(Graphics graphics);

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ParticleType getParticleType() {
        return particleType;
    }

    public void setParticleType(ParticleType particleType) {
        this.particleType = particleType;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public TimeHelper getTimeHelper() {
        return timeHelper;
    }

    public void setTimeHelper(TimeHelper timeHelper) {
        this.timeHelper = timeHelper;
    }
}
