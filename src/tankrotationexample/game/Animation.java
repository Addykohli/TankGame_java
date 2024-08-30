package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Animation {
    private final BufferedImage[] frames;
    private int currentFrame;
    private final int frameIndex;
    private final int frameDelay;
    private int frameCounter;
    private boolean playing;
    private float x, y;

    public Animation(BufferedImage[] frames, int frameDelay) {
        this.frames = frames;
        this.frameIndex = 0;
        this.frameDelay = frameDelay;
        this.currentFrame = 0;
        this.frameCounter = 0;
        this.playing = false;
    }

    public void start(float x, float y) {
        this.x = x;
        this.y = y;
        this.playing = true;
        this.currentFrame = 0;
        this.frameCounter = 0;
    }

    public void update() {
        if (!playing) return;

        frameCounter++;
        if (frameCounter >= frameDelay) {
            frameCounter = 0;
            currentFrame++;
            if (currentFrame >= frames.length) {
                playing = false;
                currentFrame = 0;
            }
        }
    }

    public boolean isPlaying() {
        return playing;
    }

    public BufferedImage getCurrentFrame() {
        return frames[currentFrame];
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void draw(Graphics2D g2) {
        if (playing) {
            g2.drawImage(frames[currentFrame], (int) x, (int) y, null);
        }
    }

    public int getCurrentFrameIndex() {
        return frameIndex;
    }
}
