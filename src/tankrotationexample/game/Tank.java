package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.geom.AffineTransform;
import java.util.Objects;

import tankrotationexample.GameConstants;

import javax.imageio.ImageIO;

public class Tank {

    private float x;
    private float y;
    private float vx;
    private float vy;
    public float angle;

    public static float R = 2.4f;
    private final float ROTATIONSPEED = 1.7f;

    public BufferedImage img;
    public BufferedImage normImg;
    public BufferedImage imgRage;
    public BufferedImage imgMonk;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;

    final List<Bullet> bullets = new ArrayList<>();
    private final BufferedImage bulletImg;
    private final Animation deathAnimation;

    private Rectangle lastBounds;

    public int health = 100;
    public int lives = 3;
    public int damage = 10;
    public boolean isWinner = true;
    public int spawnX;
    public int spawnY;
    public boolean isMonk = false;

    public Tank(float x, float y, float vx, float vy, float angle, BufferedImage normimg, BufferedImage imgRage, BufferedImage imgMonk, BufferedImage bulletImg, int SpawnX, int SpawnY) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.img = normimg;
        this.imgRage = imgRage;
        this.normImg = normimg;
        this.imgMonk = imgMonk;
        this.angle = angle;
        this.bulletImg = bulletImg;
        this.lastBounds = new Rectangle((int) x, (int) y, img.getWidth(), img.getHeight());
        this.deathAnimation = new Animation(GameWorld.deadAnmFrames, 15);
        this.spawnX = SpawnX;
        this.spawnY = SpawnY;
    }

    float getX() {
        return x;
    }

    float getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getLives() {
        return lives;
    }

    public void setMonk(boolean m) {this.isMonk = m;}

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    private void checkCollision() {
        Rectangle tankBounds = this.getBoundingBox();

        // Check collision with walls
        for (Obstacle wall : GameWorld.obstacles) {
            if (tankBounds.intersects(wall.getBoundingBox())) {
                this.handleCollision();
                return;
            }
        }

        // Check collision with other tanks
        if (this != GameWorld.t1 && tankBounds.intersects(GameWorld.t1.getBoundingBox())) {
            this.handleCollision();
            return;
        }

        if (this != GameWorld.t2 && tankBounds.intersects(GameWorld.t2.getBoundingBox())) {
            this.handleCollision();
        }
    }

    public void setDamage(int damage) {
        this.damage = damage;
        if (this.damage ==20) {
            this.img = imgRage;

        } else {
            this.img = normImg;
        }
        }

    public synchronized void shoot() {
        float centerX = this.x + (img.getWidth() / 8.0f);
        float centerY = this.y + (img.getHeight() / 8.0f);

        double radians = Math.toRadians(angle);
        double dx = Math.cos(radians) * 8;
        double dy = Math.sin(radians) * 8;

        float bulletStartX = (float) (centerX - dx);
        float bulletStartY = (float) (centerY - dy);

        Bullet bullet = new Bullet(bulletStartX, bulletStartY, this.angle, this.bulletImg, this);
        bullets.add(bullet);
        recoil();
    }
    public synchronized void reflect(float ang) {
        float centerX = this.x + (img.getWidth() / 8.0f);
        float centerY = this.y + (img.getHeight() / 8.0f);

        double radians = Math.toRadians(angle);
        double dx = Math.cos(radians) * 8;
        double dy = Math.sin(radians) * 8;

        float bulletStartX = (float) (centerX - dx);
        float bulletStartY = (float) (centerY - dy);

        Bullet bullet = new Bullet(bulletStartX, bulletStartY, ang -180, this.bulletImg, this);
        bullets.add(bullet);
    }

    public void recoil() {
        float prevX = this.x;
        float prevY = this.y;

        double radians = Math.toRadians(angle);
        double dx = Math.cos(radians) * -10; // Move back by 10 units in the opposite direction
        double dy = Math.sin(radians) * -10; // Move back by 10 units in the opposite direction

        // Update tank position
        this.x += (float) dx;
        this.y += (float) dy;

        // Check for collisions
        checkCollision();

        // Check for border constraints
        checkBorder();

        // If collision with border or wall/tank is detected, revert to the previous position
        if (this.x == prevX && this.y == prevY) {
            this.x -= (float) dx;
            this.y -= (float) dy;
        }
    }

    public synchronized void update() {
        if (deathAnimation.isPlaying()) {
            deathAnimation.update();
        }
        // Save the last position before movement
        lastBounds = new Rectangle((int) this.x, (int) this.y, img.getWidth(), img.getHeight());

        if (this.UpPressed) {
            this.moveForwards();
        }

        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }

        if (this.RightPressed) {
            this.rotateRight();
        }

        // Update bullets
        GameWorld.bulletUpdate(this);
        /*
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.update();
            if (bullet.isOutOfBounds(GameConstants.GAME_WORLD_WIDTH, GameConstants.GAME_WORLD_HEIGHT)) {
                bulletIterator.remove();
            }
        }
         */
    }

    public Rectangle getBoundingBox() {
        return new Rectangle((int) this.x, (int) this.y, img.getWidth(), img.getHeight());
    }


    public void handleCollision() {
        this.x = lastBounds.x;
        this.y = lastBounds.y;
    }

    public void respawn() {
        this.x = this.spawnX;
        this.y = this.spawnY;
        this.angle = 0;
        this.health = 100;
    }

    public void dead() {
        if (this.lives > 0) {
            this.deathAnimation.start(this.x - 135, this.y);
            GameWorld.deathSound.play();
            this.lives--;
            respawn();
            Rage.resetEffect(this);
            Haste.resetEffect(this);
        } else {
            this.isWinner = false;
            GameWorld.endGame();
        }
    }

    public void takeDamage(int damage) {
        if (this.health > 0) {
            this.health -= damage;
        }
        if (this.health <= 0 ) {
            dead();
        }
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx = (float) (R * Math.cos(Math.toRadians(angle)));
        vy = (float) (R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        checkBorder();
    }

    private void moveForwards() {
        vx = (float) (R * Math.cos(Math.toRadians(angle)));
        vy = (float) (R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
    }

    private void checkBorder() {
        if (x < 0) {
            x = 0;
        }
        if (x > GameConstants.GAME_WORLD_WIDTH - img.getWidth()) {
            x = GameConstants.GAME_WORLD_WIDTH - img.getWidth();
        }
        if (y < 0) {
            y = 0;
        }
        if (y > GameConstants.GAME_WORLD_HEIGHT - img.getHeight()) {
            y = GameConstants.GAME_WORLD_HEIGHT - img.getHeight();
        }
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }

    public synchronized void drawImage(Graphics g) {
        if (deathAnimation.isPlaying()) {
            g.drawImage(deathAnimation.getCurrentFrame(), (int) deathAnimation.getX(), (int) deathAnimation.getY(), null);
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        g2d.drawImage(this.img, rotation, null);

        // Draw bullets using an Iterator to avoid ConcurrentModificationException
        synchronized (bullets) {
            Iterator<Bullet> bulletIterator = bullets.iterator();
            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();
                bullet.drawImage(g);
            }
        }
    }

    public void setSpeed(float speed) {
        R = speed;
    }

    public float getSpeed() {
        return R;
    }

}

