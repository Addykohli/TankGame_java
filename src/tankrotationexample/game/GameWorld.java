package tankrotationexample.game;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

import tankrotationexample.Launcher;
import tankrotationexample.GameConstants;

public class GameWorld extends JPanel implements Runnable {
    private BufferedImage world;
    private BufferedImage background;
    private BufferedImage bulletImg;
    private BufferedImage liveImg;
    private BufferedImage healthImg;
    private BufferedImage hbContainerImg;
    private BufferedImage woodImg;
    public static BufferedImage[] bulletHitFrames;
    public static BufferedImage[] deadAnmFrames;
    public static BufferedImage[] lightningFrames;
    public static BufferedImage[] rageCircle;
    public static Tank t1;
    public static Tank t2;
    public static List<Obstacle> obstacles;
    private static List<PowerUp> powerUps;
    private final Launcher lf;
    public static BufferedImage winImg;
    public static Sound bgSound;
    public static Sound bulletHitSound;
    public static Sound deathSound;
    public static Sound endSound;
    public static Sound wallHitSound;

    public GameWorld(Launcher lf) {
        this.lf = lf;
        initializeImages();
    }

    private void initializeImages() {
        try {
            background = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("bg.png"),
                            "Could not find bg.png")
            );
            bulletImg = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("bullet.png"),
                            "Could not find bullet.png")
            );
            liveImg = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("live.png"),
                            "Could not find live.png")
            );
            healthImg = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("health.png"),
                            "Could not find health.png")
            );
            hbContainerImg = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("hbc.png"),
                            "Could not find hbc.png")
            );
            woodImg = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("wood.png"),
                            "Could not find wood.png")
            );
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        bulletHitFrames = new BufferedImage[7];
        for (int i = 0; i < 7; i++) {
            try {
                bulletHitFrames[i] = ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("bulletHit/bulletHit0" + (i + 1) + ".png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        deadAnmFrames = new BufferedImage[24];
        for (int i = 0; i < 24; i++) {
            try {
                deadAnmFrames[i] = ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("deadAnm/deadAnm0" + (i + 1) + ".png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        lightningFrames = new BufferedImage[16];
        for (int i = 0; i < 16; i++) {
            try {
                lightningFrames[i] = ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("lightningStrike/lightningStrike0" + (i + 1) + ".png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        rageCircle = new BufferedImage[6];
        for (int i = 0; i < 6; i++) {
            try {
                rageCircle[i] = ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("rageCircle01.png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void InitializeSounds() {
        bgSound = new Sound("bg.wav");
        bulletHitSound = new Sound("bulletHit.wav");
        deathSound = new Sound("death.wav");
        endSound = new Sound("endSound.wav");
        wallHitSound = new Sound("wallHit.wav");
    }
    private void checkCollisions(Tank tank) {
        Rectangle tankBounds = tank.getBoundingBox();

        for (Obstacle obstacle : obstacles) {
            if (tankBounds.intersects(obstacle.getBoundingBox())) {
                tank.handleCollision();
                break;
            }
        }
        if (tankBounds.intersects(t1.getBoundingBox()) && tank != t1) {
            tank.handleCollision();
        }

        if (tankBounds.intersects(t2.getBoundingBox()) && tank != t2) {
            tank.handleCollision();
        }
    }

    private void checkPowerUpCollisions() {
        for (PowerUp powerUp : powerUps) {
            if (powerUp.isColliding(t1)) {
                if (powerUp instanceof Lightning){
                    powerUp.applyEffect(t2);
                    powerUps.remove(powerUp);
                    break;
                }
                if (powerUp instanceof Heal && t1.health == 100){
                    break;

                }
                if (powerUp instanceof Rage && t1.damage == 20) {
                    break;
                }
                if (powerUp instanceof Life && t1.lives >= 6) {
                    break;
                } else {
                    powerUp.applyEffect(t1);
                    powerUps.remove(powerUp);
                }
                break;
            }
            if (powerUp.isColliding(t2)) {
                if (powerUp instanceof Lightning){
                    powerUp.applyEffect(t1);
                    powerUps.remove(powerUp);
                    break;
                }
                if (powerUp instanceof Heal && t2.health == 100) {
                    break;
                }
                if (powerUp instanceof Rage && t2.damage == 20) {
                    break;
                }
                if (powerUp instanceof Life && t2.lives >= 6) {
                    break;
                } else {
                    powerUp.applyEffect(t2);
                    powerUps.remove(powerUp);
                }
                break;
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                //this.tick++;
                t1.update();
                t2.update();
                checkCollisions(t1);
                checkCollisions(t2);
                checkPowerUpCollisions();
                Lightning.update();
                Rage.update();
                this.repaint();
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    public void resetGame() {
        this.InitializeGame();
    }

    private static void stopGame() {
        if (Launcher.gameThread != null && Launcher.gameThread.isAlive() && !Launcher.gameThread.isInterrupted()) {
            Launcher.gameThread.interrupt();
        }
    }

    public void InitializeGame() {
        this.world = new BufferedImage(GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_HEIGHT,
                BufferedImage.TYPE_INT_ARGB);

        BufferedImage t1img = null;
        BufferedImage t1imgRage = null;
        BufferedImage t1imgMonk = null;
        BufferedImage t2img = null;
        BufferedImage t2imgRage = null;
        BufferedImage t2imgMonk = null;
        try {
            t1img = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("tank1.png"),
                            "Could not find tank1.png")
            );
            t1imgRage = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("tank1Rage.png"),
                            "Could not find tank1Rage.png")
            );
            t2imgRage = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("tank2Rage.png"),
                            "Could not find tank2Rage.png")
            );
            t2img = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("tank2.png"),
                            "Could not find tank2.png")
            );
            t2imgMonk = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("tank2Monk.png"),
                            "Could not find tank2Monk.png")
            );
            t1imgMonk = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("tank1Monk.png"),
                            "Could not find tank1Monk.png")
            );
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        t1 = new Tank(970, 520, 0, 0, (short) 0, t1img, t1imgRage,t1imgMonk,bulletImg, 970, 520);
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        this.lf.getJf().addKeyListener(tc1);

        t2 = new Tank(4250, 2350, 0, 0, (short) 180, t2img, t2imgRage, t2imgMonk, bulletImg, 4250, 2350);
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SHIFT);
        this.lf.getJf().addKeyListener(tc2);

        MapLoader mapLoader = new MapLoader();
        obstacles = mapLoader.loadMap("/mapC.csv");
        powerUps = mapLoader.loadPowers("/mapC.csv");

        InitializeSounds();
    }

    public static void endGame()  {
        bgSound.stop();
        endSound.play();
        if (t1.isWinner) {
            try {
                winImg = ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("P1Win.png"),
                            "Could not find P1Win.png")
                    );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                winImg = ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("P2Win.png"),
                            "Could not find P2Win.png")
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        stopGame();
        Launcher.getInstance().setEndGamePanel();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw the background and objects on the buffer
        Graphics2D buffer = world.createGraphics();
        buffer.drawImage(background, 0, 0, GameConstants.GAME_WORLD_WIDTH, GameConstants.GAME_WORLD_HEIGHT, null);

        for (Obstacle obstacle : obstacles) {
            obstacle.drawImage(buffer);
        }
        for (PowerUp powerUp : powerUps) {
            powerUp.drawImage(buffer);
        }

        t1.drawImage(buffer);
        t2.drawImage(buffer);

        //spell animation
        Lightning.draw(buffer);
        Rage.draw(buffer);
        buffer.dispose();

        // Zoom out by shrinking the subimage
        float zoomFactorX = 0.7f;
        float zoomFactorY = 0.78f;
        int width = (int) ((float) GameConstants.GAME_SCREEN_WIDTH / 2 / zoomFactorX);
        int height = (int) (GameConstants.GAME_SCREEN_HEIGHT / zoomFactorY);

        try {
            int t1X = Math.max(0, Math.min((int) t1.getX() - width / 2, GameConstants.GAME_WORLD_WIDTH - width));
            int t1Y = Math.max(0, Math.min((int) t1.getY() - height / 2, GameConstants.GAME_WORLD_HEIGHT - height));
            int t2X = Math.max(0, Math.min((int) t2.getX() - width / 2, GameConstants.GAME_WORLD_WIDTH - width));
            int t2Y = Math.max(0, Math.min((int) t2.getY() - height / 2, GameConstants.GAME_WORLD_HEIGHT - height));

            // Create larger subimages from the world
            BufferedImage leftScreen = world.getSubimage(t1X, t1Y, width, height);
            BufferedImage rightScreen = world.getSubimage(t2X, t2Y, width, height);

            // Draw the zoomed out split screens
            g2.drawImage(leftScreen, 0, 0, getWidth() / 2, getHeight()-GameConstants.GAME_BOTTOM_MARGIN, null);
            g2.drawImage(rightScreen, getWidth() / 2, 0, getWidth() / 2, getHeight()-GameConstants.GAME_BOTTOM_MARGIN, null);
        } catch (RuntimeException e) {
            System.out.println("Error: Subimage out of bounds");
        }

        // Draw borders
        g2.setColor(Color.BLACK);
        g2.drawRect(0, 0, getWidth() / 2, getHeight()-GameConstants.GAME_BOTTOM_MARGIN);
        g2.drawRect(getWidth() / 2, 0, getWidth() / 2, getHeight()-GameConstants.GAME_BOTTOM_MARGIN);

        // Draw the wood background in the margin area
        int marginY = getHeight() - GameConstants.GAME_BOTTOM_MARGIN;
        g2.drawImage(woodImg, 0, marginY, getWidth(), GameConstants.GAME_BOTTOM_MARGIN, null);

        // Draw the mini-map
        float miniMapWidth = getWidth() / 4.4f;
        float miniMapHeight = getHeight() / 4.4f;
        float xPosition = (getWidth() - miniMapWidth) / 2.0f;
        float yPosition = getHeight() - miniMapHeight;

        BufferedImage miniMap = world.getSubimage(0, 0, GameConstants.GAME_WORLD_WIDTH, GameConstants.GAME_WORLD_HEIGHT);

        // Apply transformation for scaling and positioning the mini-map
        AffineTransform at = new AffineTransform();
        at.translate(xPosition, yPosition);
        at.scale(miniMapWidth / miniMap.getWidth(), miniMapHeight / miniMap.getHeight());
        g2.drawImage(miniMap, at, null);

        // tank squares
        int tankSize = 10;
        int miniMapX = (int) xPosition;
        int miniMapY = (int) yPosition;
        int miniMapW = (int) miniMapWidth;
        int miniMapH = (int) miniMapHeight;

        //position of tanks on the mini-map
        int t1MinimapX = (int) (miniMapX + (t1.getX() / (double) GameConstants.GAME_WORLD_WIDTH) * miniMapW) - tankSize / 2;
        int t1MinimapY = (int) (miniMapY + (t1.getY() / (double) GameConstants.GAME_WORLD_HEIGHT) * miniMapH) - tankSize / 2;
        int t2MinimapX = (int) (miniMapX + (t2.getX() / (double) GameConstants.GAME_WORLD_WIDTH) * miniMapW) - tankSize / 2;
        int t2MinimapY = (int) (miniMapY + (t2.getY() / (double) GameConstants.GAME_WORLD_HEIGHT) * miniMapH) - tankSize / 2;

        g2.setColor(Color.RED);
        g2.fillRect(t1MinimapX, t1MinimapY, tankSize, tankSize);
        g2.setColor(Color.BLUE);
        g2.fillRect(t2MinimapX, t2MinimapY, tankSize, tankSize);

        // Draw stat boxes
        drawStatBox(g2, t1, 20, getHeight() - 110, true);
        drawStatBox(g2, t2, getWidth() - 120, getHeight() - 110, false);
    }

    private void drawStatBox(Graphics2D g2, Tank tank, int x, int y, boolean isLeft) {
        int livesWidth = liveImg.getWidth();
        int livesHeight = liveImg.getHeight();
        int healthWidth = healthImg.getWidth();
        int healthHeight = healthImg.getHeight();
        int hbContainerWidth = hbContainerImg.getWidth();
        int hbContainerHeight = hbContainerImg.getHeight();
        // Draw lives
        for (int i = 0; i < tank.getLives(); i++) {
            int livesX = isLeft ? x + i * (livesWidth + 5) : x - i * (livesWidth + 5) - livesWidth+100;
            g2.drawImage(liveImg, livesX, y + healthImg.getHeight() + 20, livesWidth+5, livesHeight+5, null);
        }

        // Draw health bar container
        int hbContainerX = isLeft ? x : x - hbContainerWidth + 110;
        g2.drawImage(hbContainerImg, hbContainerX, y, hbContainerWidth, hbContainerHeight, null);

        // Draw health bar
        int healthBarWidth = healthWidth * tank.getHealth() / 100;
        int healthBarX = isLeft ? x+6 : x -healthBarWidth+102;
        g2.drawImage( healthImg, healthBarX, y+3, healthBarWidth, healthHeight, null);
    }
}