package tankrotationexample.menus;

import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import tankrotationexample.game.GameWorld;

public class EndGamePanel extends JPanel {

    private BufferedImage menuBackground;
    private final Launcher lf;
    private final GameWorld gameWorld;
    private BufferedImage restartButtonImg;
    private BufferedImage exitButtonImg;

    public EndGamePanel(Launcher lf, GameWorld gameWorld) {
        this.lf = lf;
        this.gameWorld = gameWorld;
        try {
            menuBackground = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("endBg.png")));
            restartButtonImg = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("button1.png")));
            exitButtonImg = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("button2.png")));
        } catch (IOException e) {
            System.out.println("Error can't read menu background or button images");
            e.printStackTrace();
            System.exit(-3);
        }
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        JButton Restart = createButton("RESTART", 403, 380, 59, restartButtonImg);
        Restart.setForeground(Color.CYAN);
        Restart.addActionListener((actionEvent -> this.gameWorld.resetGame()));
        Restart.addActionListener((actionEvent -> this.lf.setFrame("game")));
        Restart.addActionListener(actionEvent -> GameWorld.endSound.stop());
        Restart.addActionListener(actionEvent -> GameWorld.bgSound.loop());

        JButton exit = createButton("EXIT", 403, 460, 65, exitButtonImg);
        exit.setForeground(Color.YELLOW);
        exit.addActionListener((actionEvent -> this.lf.closeGame()));

        this.add(Restart);
        this.add(exit);
    }

    private JButton createButton(String text, int x, int y, int height, BufferedImage buttonImg) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 20));
        button.setBounds(x, y, 200, height);
        button.setIcon(new ImageIcon(buttonImg));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(true);
        return button;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.menuBackground, 0, 0, null);

        if (GameWorld.winImg != null) {
            int imgX = (this.getWidth() - GameWorld.winImg.getWidth()+34) / 2;
            int imgY = 60;
            g2.drawImage(GameWorld.winImg, imgX, imgY, null);
        }
    }
}