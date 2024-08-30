package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Obstacle {
    protected int x, y;
    protected BufferedImage img;
    public float X;
    public float Y;

    public Obstacle(int x, int y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
    }

    public void drawImage(Graphics g) {
        g.drawImage(img, x, y, null);
    }

    public Rectangle getBoundingBox() {
        return new Rectangle(x, y, img.getWidth(), img.getHeight());
    }

    public float getX() {
        return this.X;
    }

    public float getY() {
        return this.Y;
    }
}
