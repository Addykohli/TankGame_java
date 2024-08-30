package tankrotationexample.game;

import java.awt.image.BufferedImage;

public class Wall extends Obstacle {
    public Wall(int x, int y, BufferedImage img) {
        super(x, y, img);
        this.x = x;
        this.y = y;
    }


    public float getX() {
        return this.x;
    }


    public float getY() {
        return this.y;
    }


}

