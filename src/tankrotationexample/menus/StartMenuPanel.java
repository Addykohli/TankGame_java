package tankrotationexample.menus;
import tankrotationexample.Launcher;
import tankrotationexample.game.GameWorld;
import tankrotationexample.game.Sound;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class StartMenuPanel extends JPanel {

    private BufferedImage menuBackground;
    private final Launcher lf;
    private BufferedImage startButtonImg;
    private BufferedImage exitButtonImg;

    public StartMenuPanel(Launcher lf) {
        this.lf = lf;
        try {
            startButtonImg = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("button1.png")));
            exitButtonImg = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("button2.png")));
            menuBackground = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("title.png")));
        } catch (IOException e) {
            System.out.println("Error cant read menu background");
            e.printStackTrace();
            System.exit(-3);
        }
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        JButton start = createButton("START", startButtonImg);
        start.setSize(new Dimension(131, 100));
        start.setBounds(115, 100, 131, 59);
        start.setForeground(Color.CYAN);
        start.addActionListener(actionEvent -> this.lf.setFrame("game"));
        start.addActionListener(actionEvent -> GameWorld.bgSound.loop());

        JButton exit = createButton("EXIT", exitButtonImg);
        exit.setSize(new Dimension(131, 100));
        exit.setBounds(585, 100, 131, 65);
        exit.setForeground(Color.YELLOW);
        exit.addActionListener((actionEvent -> this.lf.closeGame()));

        this.add(start);
        this.add(exit);
        Sound startSound = new Sound("startup.wav");
        startSound.play();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.menuBackground, 0, 0, null);
    }

    private JButton createButton(String text, BufferedImage buttonImg) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 24));
        button.setIcon(new ImageIcon(buttonImg));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.getColorModel();
        button.setFocusPainted(true);
        return button;
    }
}
