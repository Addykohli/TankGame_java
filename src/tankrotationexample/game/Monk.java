package tankrotationexample.game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Monk extends PowerUp {
    public Monk(int x, int y) { // tank reflects bullets for 15 seconds
        super(x, y, loadImage());
    }

    @Override
    public void applyEffect(Tank tank) {
        tank.setMonk(true);
        tank.img = tank.imgMonk;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                tank.setMonk(false);
                if (tank.damage == 20) {
                    tank.img = tank.imgRage;
                } else if (tank.damage == 10) {
                    tank.img = tank.normImg;

                }

            }
        }, 15000);
    }

    private static BufferedImage loadImage() {
        try {
            return ImageIO.read(Objects.requireNonNull(Rage.class.getClassLoader().getResource("monk.png")));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
