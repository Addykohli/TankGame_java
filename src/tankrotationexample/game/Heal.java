package tankrotationexample.game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class Heal extends PowerUp {
    private final Sound healSound = new Sound("healSound.wav");
    public Heal(int x, int y) {
        super(x, y, loadImage());
    } // heals till max heath, cannot pick with full health

    @Override
    public void applyEffect(Tank tank) {
        int currHealth = tank.getHealth();
        if (currHealth <= 50) {
            tank.setHealth(currHealth + 50);
        }
        tank.setHealth(100);
        healSound.play();
    }

    private static BufferedImage loadImage() {
        try {
            return ImageIO.read(Objects.requireNonNull(Heal.class.getClassLoader().getResource("healSpell.png")));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
