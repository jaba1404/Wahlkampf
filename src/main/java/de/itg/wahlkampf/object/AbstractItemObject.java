package de.itg.wahlkampf.object;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.object.boundingbox.AxisAligned;

import java.awt.*;

public abstract class AbstractItemObject extends AbstractGameObject {
    private AbstractPlayerObject playerObject;

    public AbstractItemObject(String name, int positionX, int positionY) {
        super(name, Type.NON_COLLIDABLE, positionX, positionY, 30, 30, false);
    }

    @Override
    public void onRender(Graphics graphics) {
        getRenderer().drawFillRectangle(graphics, getPositionX(), getPositionY(), getWidth(), getHeight(), Color.WHITE);
    }

    @Override
    public void onTick() {
        final AxisAligned itemAxisAligned = new AxisAligned(this.getPositionX(), this.getPositionX() + this.getWidth(), this.getPositionY(), this.getPositionY() + this.getHeight());
        Game.instance.getObjectHandler().getGameObjects().stream().filter(gameObject -> gameObject instanceof AbstractPlayerObject).forEach(gameObject -> {
            final AxisAligned playerAxisAligned = new AxisAligned(gameObject.getPositionX(), gameObject.getPositionX() + gameObject.getWidth(), gameObject.getPositionY(), gameObject.getPositionY() + gameObject.getHeight());
            if (playerAxisAligned.getMinY() <= itemAxisAligned.getMaxY() && playerAxisAligned.getMaxY() >= itemAxisAligned.getMinY() && playerAxisAligned.getMinX() >= itemAxisAligned.getMinX() && playerAxisAligned.getMaxX() <= itemAxisAligned.getMaxX()) {
                playerObject = (AbstractPlayerObject) gameObject;
                onPickUp();
                deleteObject();
            }
        });
    }

    public abstract void onPickUp();

    public AbstractPlayerObject getPlayerObject() {
        return playerObject;
    }
}
