package de.itg.wahlkampf.object;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.object.boundingbox.AxisAligned;
import de.itg.wahlkampf.object.objects.blocks.StageBlock;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public abstract class AbstractItemObject extends AbstractGameObject {
    private AbstractPlayerObject playerObject;
    private BufferedImage itemImage;

    private boolean onGround;

    public AbstractItemObject(String name, int positionX, int positionY) {
        super(name, Type.NON_COLLIDABLE, positionX, positionY, 30, 30, false);
        itemImage = null;
    }

    public AbstractItemObject(String name, int positionX, int positionY, URL itemImagePath) {
        super(name, Type.NON_COLLIDABLE, positionX, positionY, 30, 30, false);
        try {
            this.itemImage = ImageIO.read(itemImagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRender(Graphics graphics) {
        if (itemImage == null) {
            getRenderer().drawFillRectangle(graphics, getPositionX(), getPositionY(), getWidth(), getHeight(), Color.WHITE);
        } else {
            getRenderer().drawFillCircle(graphics, getPositionX(), getPositionY(), getWidth(), getHeight(), new Color(255, 255, 255, 100));
            getRenderer().img(graphics, itemImage, getPositionX(), getPositionY(), getWidth(), getHeight());
        }
    }

    @Override
    public void onTick() {
        final AxisAligned itemAxisAligned = new AxisAligned(this.getPositionX(), this.getPositionX() + this.getWidth(), this.getPositionY(), this.getPositionY() + this.getHeight());
        Game.instance.getObjectHandler().getGameObjects().stream().filter(gameObject -> gameObject instanceof AbstractPlayerObject).forEach(gameObject -> {
            final AxisAligned playerAxisAligned = new AxisAligned(gameObject.getPositionX(), gameObject.getPositionX() + gameObject.getWidth(), gameObject.getPositionY(), gameObject.getPositionY() + gameObject.getHeight());
            if ((playerAxisAligned.getMinY() <= itemAxisAligned.getMaxY() && playerAxisAligned.getMinY() >= itemAxisAligned.getMinY() || playerAxisAligned.getMaxY() >= itemAxisAligned.getMinY() && playerAxisAligned.getMaxY() <= itemAxisAligned.getMaxY()) && (playerAxisAligned.getMinX() >= itemAxisAligned.getMinX() && playerAxisAligned.getMinX() <= itemAxisAligned.getMaxX() || playerAxisAligned.getMaxX() <= itemAxisAligned.getMaxX() && playerAxisAligned.getMaxX() >= itemAxisAligned.getMinX())) {
                playerObject = (AbstractPlayerObject) gameObject;
                onPickUp();
                deleteObject();
            }
        });
        if (!onGround) {
            setPositionY(getPositionY() + 5);
            handleCollision();
        }

    }

    public void handleCollision() {
        final AxisAligned playerAxisAligned = new AxisAligned(this.getPositionX(), this.getPositionX() + this.getWidth(), this.getPositionY(), this.getPositionY() + this.getHeight());
        onGround = false;
        for (AbstractGameObject gameObject : Game.instance.getObjectHandler().getGameObjects()) {
            if (gameObject instanceof AbstractPlayerObject || gameObject.getType() == Type.NON_COLLIDABLE)
                break;

            final AxisAligned objectsAxisAligned = new AxisAligned(gameObject.getPositionX(), gameObject.getPositionX() + gameObject.getWidth(), gameObject.getPositionY(), gameObject.getPositionY() + gameObject.getHeight());
            if (playerAxisAligned.getMaxX() > objectsAxisAligned.getMinX() && playerAxisAligned.getMinX() < objectsAxisAligned.getMaxX()) {
                if (playerAxisAligned.getMaxY() >= objectsAxisAligned.getMinY() - 1 && (playerAxisAligned.getMinY() + (this.getHeight() / 2)) < (objectsAxisAligned.getMinY() + objectsAxisAligned.getMaxY()) / 2) {
                    if (this.getPositionY() + this.getHeight() != objectsAxisAligned.getMinY() - 1) {
                        setPositionY(objectsAxisAligned.getMinY() - this.getHeight() - 1);
                    }
                    onGround = true;
                    break;
                }
            }
        }
    }

    public abstract void onPickUp();

    public AbstractPlayerObject getPlayerObject() {
        return playerObject;
    }
}
