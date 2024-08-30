package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BreakableWall extends Wall {
    private int health;
    private final BufferedImage hitImg;

    public BreakableWall(int x, int y, BufferedImage img, BufferedImage hitImg) {
        super(x, y, img);
        this.health = 20;
        this.hitImg = hitImg;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 10 && this.health > 0) {
            GameWorld.wallHitSound.play();
            this.img = hitImg;
        } else if (this.health <= 0) {
            GameWorld.obstacles.remove(this);
        }
    }

    @Override
    public void drawImage(Graphics g) {
        super.drawImage(g);
    }

    @Override
    public Rectangle getBoundingBox() {
        return super.getBoundingBox();
    }
}
