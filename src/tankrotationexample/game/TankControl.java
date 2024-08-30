package tankrotationexample.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class TankControl implements KeyListener {
    private final Tank tank;
    private final int up;
    private final int down;
    private final int right;
    private final int left;
    private final int shoot;
    private final Set<Integer> pressedKeys = new HashSet<>();

    public TankControl(Tank tank, int up, int down, int left, int right, int shoot) {
        this.tank = tank;
        this.up = up;
        this.down = down;
        this.right = right;
        this.left = left;
        this.shoot = shoot;
    }

    @Override
    public void keyTyped(KeyEvent ke) {}

    @Override
    public void keyPressed(KeyEvent ke) {
        int keyPressed = ke.getKeyCode();
        pressedKeys.add(keyPressed);

        if (pressedKeys.contains(up)) {
            this.tank.toggleUpPressed();
        }
        if (pressedKeys.contains(down)) {
            this.tank.toggleDownPressed();
        }
        if (pressedKeys.contains(left)) {
            this.tank.toggleLeftPressed();
        }
        if (pressedKeys.contains(right)) {
            this.tank.toggleRightPressed();
        }
        if (pressedKeys.contains(shoot)) {
            this.tank.shoot();
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        int keyReleased = ke.getKeyCode();
        pressedKeys.remove(keyReleased);

        if (!pressedKeys.contains(up)) {
            this.tank.unToggleUpPressed();
        }
        if (!pressedKeys.contains(down)) {
            this.tank.unToggleDownPressed();
        }
        if (!pressedKeys.contains(left)) {
            this.tank.unToggleLeftPressed();
        }
        if (!pressedKeys.contains(right)) {
            this.tank.unToggleRightPressed();
        }
    }
}
