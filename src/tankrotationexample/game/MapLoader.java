package tankrotationexample.game;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;

public class MapLoader {
    private BufferedImage UnbreakableWallImage;
    private BufferedImage breakableWallImage;
    private BufferedImage hitBreakableWallImage;
    private BufferedImage waterImageV;
    private BufferedImage waterImageH;
    private BufferedImage waterImageTR;
    private BufferedImage waterImageBR;
    private BufferedImage waterImageBL;
    private BufferedImage waterImageTL;
    private BufferedImage waterImageS;


    public MapLoader() {
        try {
            UnbreakableWallImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("uwall.png")));
            breakableWallImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("bwall1.png")));
            hitBreakableWallImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("bwall2.png")));
            waterImageV = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("waterV.png")));
            waterImageH = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("waterH.png")));
            waterImageTR = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("waterTR.png")));
            waterImageBR = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("waterBR.png")));
            waterImageBL = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("waterBL.png")));
            waterImageTL = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("waterTL.png")));
            waterImageS = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("waterS.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Obstacle> loadMap(String mapPath) {
        List<Obstacle> obstacles = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(mapPath))))) {
            String line;
            int y = 0;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                for (int x = 0; x < values.length; x++) {
                    switch (values[x]) {
                        case "9":
                            obstacles.add(new UnbreakableWall(x * 60, y * 60, UnbreakableWallImage));
                            break;
                        case "4":
                            obstacles.add(new BreakableWall(x * 60, y * 60, breakableWallImage, hitBreakableWallImage));
                            break;
                        //water types:
                        case "8":
                            obstacles.add(new Water(x * 60, y * 60, waterImageV));
                            break;
                        case "7":
                            obstacles.add(new Water(x * 60, y * 60, waterImageH));
                            break;
                        case "6":
                            obstacles.add(new Water(x * 60, y * 60, waterImageTR));
                            break;
                        case "5":
                            obstacles.add(new Water(x * 60, y * 60, waterImageBR));
                            break;
                        case "3":
                            obstacles.add(new Water(x * 60, y * 60, waterImageBL));
                            break;
                        case "2":
                            obstacles.add(new Water(x * 60, y * 60, waterImageTL));
                            break;
                        case "1":
                            obstacles.add(new Water(x * 60, y * 60, waterImageS));
                            break;
                    }
                }
                y++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obstacles;
    }

    public List<PowerUp> loadPowers(String mapPath) {
        List<PowerUp> PowerUps = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(mapPath))))) {
            String line;
            int y = 0;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                for (int x = 0; x < values.length; x++) {
                    switch (values[x]) {
                        case "R":
                            PowerUps.add(new Rage(x * 60, y * 60));
                            break;
                        case "H":
                            PowerUps.add(new Heal(x * 60, y * 60));
                            break;
                        case "S":
                            PowerUps.add(new Haste(x * 60, y * 60));
                            break;
                        case "L":
                            PowerUps.add(new Life(x * 60, y * 60));
                            break;
                        case "T":
                            PowerUps.add(new Lightning(x * 60, y * 60));
                            break;
                        case "M":
                            PowerUps.add(new Monk(x * 60, y * 60));
                            break;

                    }
                }
                y++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return PowerUps;
    }

}
