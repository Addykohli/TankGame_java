package tankrotationexample.game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Objects;

public class Haste extends PowerUp {
    private final Sound hasteSound = new Sound("hasteSound.wav");

    public Haste(int x, int y) { // increases speed, stacks up
        super(x, y, loadImage());

    }

    @Override
    public void applyEffect(Tank tank) {
        tank.setSpeed(tank.getSpeed()+0.5f);
        this.hasteSound.play();
    }
    public static void resetEffect(Tank tank) {
        tank.setSpeed(2.4f);
    }

    private static BufferedImage loadImage() {
        try {
            return ImageIO.read(Objects.requireNonNull(Rage.class.getClassLoader().getResource("haste.png")));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
