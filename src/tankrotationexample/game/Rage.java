package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Objects;

public class Rage extends PowerUp {
    private static Animation rageAnimation;
    private final Sound rageSound = new Sound("rageSound.wav");

    public Rage(int x, int y) { // doubles damage, does not stack up
        super(x, y, loadImage());
        rageAnimation = new Animation(GameWorld.rageCircle, 100);
    }

    @Override
    public void applyEffect(Tank tank) {
        tank.setDamage(20);
        this.rageSound.play();
        rageAnimation.start(tank.getX()-110, tank.getY()-45);
    }

    public static void resetEffect(Tank tank) {
        tank.setDamage(10);
    }
    public static void update() {
        if (rageAnimation.isPlaying()) {
            rageAnimation.update();
        }
    }
    public static void draw(Graphics2D g2) {
        if (rageAnimation.isPlaying()) {
            rageAnimation.draw(g2);
        }
    }

    private static BufferedImage loadImage() {
        try {
            return ImageIO.read(Objects.requireNonNull(Rage.class.getClassLoader().getResource("rage.png")));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
