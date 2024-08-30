package tankrotationexample;

import tankrotationexample.game.GameWorld;
import tankrotationexample.menus.EndGamePanel;
import tankrotationexample.menus.StartMenuPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class Launcher {

    public static Thread gameThread;
    private static Launcher instance;
    private JPanel mainPanel;
    public GameWorld gamePanel;
    private final JFrame jf;
    private CardLayout cl;

    public Launcher() {
        instance = this; // Set the instance to this object
        this.jf = new JFrame();
        this.jf.setTitle("Tank Wars Game");
        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static Launcher getInstance() {
        return instance;
    }

    private void initUIComponents() {
        this.mainPanel = new JPanel();
        JPanel startPanel = new StartMenuPanel(this);
        this.gamePanel = new GameWorld(this);
        this.gamePanel.InitializeGame();
        JPanel endPanel = new EndGamePanel(this, this.gamePanel);
        cl = new CardLayout();
        this.mainPanel.setLayout(cl);
        this.mainPanel.add(startPanel, "start");
        this.mainPanel.add(gamePanel, "game");
        this.mainPanel.add(endPanel, "end");
        this.jf.add(mainPanel);
        this.jf.setResizable(true);
        this.setFrame("start");
    }

    public void setFrame(String type) {
        this.jf.setVisible(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        switch (type) {
            case "start" -> {
                this.jf.setSize(GameConstants.START_MENU_SCREEN_WIDTH, GameConstants.START_MENU_SCREEN_HEIGHT);
                centerWindow(screenSize, GameConstants.START_MENU_SCREEN_WIDTH, GameConstants.START_MENU_SCREEN_HEIGHT);
            }
            case "game" -> {
                this.jf.setSize(GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
                centerWindow(screenSize, GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
                gameThread = new Thread(this.gamePanel);
                gameThread.start();
            }
            case "end" -> {
                this.jf.setSize(GameConstants.END_MENU_SCREEN_WIDTH, GameConstants.END_MENU_SCREEN_HEIGHT);
                centerWindow(screenSize, GameConstants.END_MENU_SCREEN_WIDTH, GameConstants.END_MENU_SCREEN_HEIGHT);
            }
        }
        this.cl.show(mainPanel, type);
        this.jf.setVisible(true);
    }

    private void centerWindow(Dimension screenSize, int windowWidth, int windowHeight) {
        int x = (screenSize.width - windowWidth) / 2;
        int y = (screenSize.height - windowHeight) / 2;
        this.jf.setLocation(x, y);
    }

    public JFrame getJf() {
        return jf;
    }

    public void closeGame() {
        this.jf.dispatchEvent(new WindowEvent(this.jf, WindowEvent.WINDOW_CLOSING));
    }

    public void setEndGamePanel() {
        setFrame("end");
    }

    public static void main(String[] args) {
        (new Launcher()).initUIComponents();
    }
}
