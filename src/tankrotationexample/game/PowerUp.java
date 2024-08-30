package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class PowerUp {
    protected BufferedImage image;
    protected int x, y;

    public PowerUp(int x, int y, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }

    public abstract void applyEffect(Tank tank);

    public Rectangle getBoundingBox() {
        return new Rectangle(x, y, image.getWidth(), image.getHeight());
    }

    public void drawImage(Graphics2D g2) {
        g2.drawImage(image, x, y, null);
    }

    public boolean isColliding(Tank tank) {
        return getBoundingBox().intersects(tank.getBoundingBox());
    }
}
