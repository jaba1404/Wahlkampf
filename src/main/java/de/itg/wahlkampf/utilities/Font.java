package de.itg.wahlkampf.utilities;

import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Font extends java.awt.Font {

    private final java.awt.Font font;

    private final AffineTransform affinetransform;
    private final FontRenderContext frc;

    public Font(String fontName, int type, int size) {
        super(fontName, type, size);
        this.affinetransform = new AffineTransform();
        this.frc = new FontRenderContext(affinetransform, true, true);
        font = new java.awt.Font(fontName, type, size);
    }

    public Rectangle2D getStringSize(String text) {
        return font.getStringBounds(text, frc);
    }

    public AffineTransform getAffinetransform() {
        return affinetransform;
    }

    public FontRenderContext getFrc() {
        return frc;
    }
}
