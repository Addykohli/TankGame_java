package tankrotationexample.game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet {
    private float x;
    private float y;
    private final float angle;
    private final BufferedImage img;
    private final Animation hitAnimation;
    private boolean active;
    private final Tank owner;

    public Bullet(float x, float y, float angle, BufferedImage img, Tank owner) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.img = img;
        this.active = true;
        this.owner = owner;
        this.hitAnimation = new Animation(GameWorld.bulletHitFrames, 8);
    }

    public void update() {
        if (!active) {
            if (hitAnimation.isPlaying()) {
                hitAnimation.update();
            }
            return;
        }

        x += (float) (5 * Math.cos(Math.toRadians(angle)));
        y += (float) (5 * Math.sin(Math.toRadians(angle)));

        // Check for collisions with walls
        for (Obstacle obstacle : GameWorld.obstacles) {
            if (getBoundingBox().intersects(obstacle.getBoundingBox())) {
                if (obstacle instanceof BreakableWall) {
                    ((BreakableWall) obstacle).takeDamage(10);
                }
                if (obstacle instanceof Water) {
                    break;
                }
                this.active = false;
                GameWorld.bulletHitSound.play();
                this.hitAnimation.start(obstacle.getX()-30, obstacle.getY()-30);
                return;
            }
        }

        // Check for collisions with tanks
        if (owner != GameWorld.t1 && getBoundingBox().intersects(GameWorld.t1.getBoundingBox())) {
            if (GameWorld.t1.isMonk){
                GameWorld.t1.reflect(this.angle);
            } else {
                GameWorld.t1.takeDamage(GameWorld.t2.damage);
                this.hitAnimation.start(GameWorld.t1.getX() - 38, GameWorld.t1.getY() - 38);
                GameWorld.bulletHitSound.play();
            }
            this.active = false;
            return;
        }

        if (owner != GameWorld.t2 && getBoundingBox().intersects(GameWorld.t2.getBoundingBox())) {
            if (GameWorld.t2.isMonk){
                GameWorld.t2.reflect(this.angle);
            } else {
                GameWorld.t2.takeDamage(GameWorld.t1.damage);
                this.hitAnimation.start(GameWorld.t2.getX() - 38, GameWorld.t2.getY() - 38);
                GameWorld.bulletHitSound.play();
            }
            this.active = false;
            return;
        }
    }

    public void drawImage(Graphics g) {
        if (!active) {
            if (hitAnimation.isPlaying()) {
                g.drawImage(hitAnimation.getCurrentFrame(), (int) hitAnimation.getX(), (int) hitAnimation.getY(), null);
            }
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        g2d.drawImage(this.img, rotation, null);
    }

    public Rectangle getBoundingBox() {
        return new Rectangle((int) x, (int) y, img.getWidth(), img.getHeight());
    }

    public boolean isOutOfBounds(int width, int height) {
        return x < 0 || x > width || y < 0 || y > height;
    }
}
