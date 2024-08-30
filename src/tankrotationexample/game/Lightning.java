package tankrotationexample.game;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Lightning extends PowerUp {

    private Timer damageTimer;
    private final int damage = 15;
    private static final int HIT_COUNT = 3;
    private static final int DAMAGE_INTERVAL = 1500;
    private int hitsLeft = HIT_COUNT;
    private final Sound thunder = new Sound("thunder.wav");
    private final Sound getLightning = new Sound("LightningGet.wav");
    private static Animation lightningAnimation;
    public Lightning(int x, int y) { //strikes opponent with 3 lightning bolts
        super(x, y, loadImage());
        lightningAnimation = new Animation(GameWorld.lightningFrames, 10);
    }

    @Override
    public void applyEffect(Tank tank) {
        getLightning.play();
        damageTimer = new Timer();
        damageTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (hitsLeft > 0) {
                    lightningAnimation.start(tank.getX()-210, tank.getY()-395);
                    thunder.play();
                    tank.takeDamage(damage);
                    hitsLeft--;
                } else {
                    damageTimer.cancel();
                }
            }
        }, 0, DAMAGE_INTERVAL);
    }

    public static void update() {
        if (lightningAnimation.isPlaying()) {
            lightningAnimation.update();
        }
    }

    public static void draw(Graphics2D g2) {
        if (lightningAnimation.isPlaying()) {
            lightningAnimation.draw(g2);
        }
    }

    private static BufferedImage loadImage() {
        try {
            return ImageIO.read(Objects.requireNonNull(Lightning.class.getClassLoader().getResource("lightning.png")));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
