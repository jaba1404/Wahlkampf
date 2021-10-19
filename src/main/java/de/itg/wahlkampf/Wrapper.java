package de.itg.wahlkampf;

import de.itg.wahlkampf.utilities.Renderer;
import de.itg.wahlkampf.utilities.particlesystem.ParticleHandler;

public class Wrapper {
    public static final Wrapper WRAPPER_INSTANCE = new Wrapper();
    public Renderer renderer = new Renderer();
    public ParticleHandler particleHandler = new ParticleHandler();

}
