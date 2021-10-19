package de.itg.wahlkampf.utilities.particlesystem.particles;

import de.itg.wahlkampf.utilities.particlesystem.Particle;
import de.itg.wahlkampf.utilities.particlesystem.ParticleType;

import java.awt.*;

public class RoundParticle extends Particle {
    public RoundParticle(int x, int y, int size, Color color) {
        super(x, y, size, color, ParticleType.ROUND);
    }

    @Override
    public void drawParticle(Graphics graphics) {
        graphics.setColor(getColor());
        graphics.drawOval(getX(), getY(), getSize(), getSize());
    }

    @Override
    public void fadeParticle(Graphics graphics) {
        graphics.setColor(getColor().darker());
    }
}
