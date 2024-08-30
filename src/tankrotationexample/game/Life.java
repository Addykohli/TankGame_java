package tankrotationexample.game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Objects;

public class Life extends PowerUp {
    private final Sound lifeSound = new Sound("getLife.wav");

    public Life(int x, int y) {
        super(x, y, loadImage());
    } // adds a life, cannot pick with full health

    @Override
    public void applyEffect(Tank tank) {
        int maxLives = 6;
        if (tank.lives <= maxLives) {
            tank.lives++;
            lifeSound.play();
        }
    }

    private static BufferedImage loadImage() {
        try {
            return ImageIO.read(Objects.requireNonNull(Life.class.getClassLoader().getResource("live.png")));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
