package de.itg.wahlkampf.utilities.particlesystem;

import de.itg.wahlkampf.utilities.particlesystem.particles.RoundParticle;
import de.itg.wahlkampf.utilities.particlesystem.particles.SquaredParticle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ParticleHandler {
    private final List<Particle> particleList = new ArrayList<>();

    public void bloomEffect(int x, int y, int size, Color color, ParticleType particleType, int spread, int degreeFrom, int degreeTo, int travelDistance) {
        for (int i = degreeFrom; i < degreeTo; i++) {

        }
    }

    public void snakeEffect(int x, int y, int size, Color color, ParticleType particleType, int spread, int amount, long time) {
        for (int i = 0; i < amount; i++) {
            for (int j = -(spread / 2); j < spread / 2; j++) {
                drawParticle(x + 5, y + spread, size, color, particleType);
            }
        }
        if (!particleList.isEmpty()) {
            for (int i = 0; i < particleList.size(); i++) {
                Particle particle = particleList.get(i);
                if (particle.getTimeHelper().hasPassed(time)) {
                    particleList.remove(particle);
                }
            }
        }

    }

    private void drawParticle(int x, int y, int size, Color color, ParticleType particleType) {
        switch (particleType) {
            case SQUARED -> particleList.add(new SquaredParticle(x, y, size, color));
            case ROUND -> particleList.add(new RoundParticle(x, y, size, color));
        }
    }

    public List<Particle> getParticleList() {
        return particleList;
    }
}
