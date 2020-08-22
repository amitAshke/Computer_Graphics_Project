package Player;

import Main.Display;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.HashMap;

/**
 * This class is the game's input handler that keeps track of the players input through the keyboard and mouse.
 */
public class InputHandler implements KeyListener, MouseListener, MouseMotionListener, FocusListener {

    private static InputHandler singleInstance = null;

    // A Robot object to to recenter the mouse after each movement to the center of the screen.
    private Robot robot;

    private final Point2D windowCenter = new Point2D.Double(Display.WINDOW_WIDTH / 2.0, Display.WINDOW_HEIGHT / 2.0);

    // A hashmap that hold  all of the information regarding which key is pressed (mouse and keyboard).
    public HashMap<Integer, Boolean> key = new HashMap<>();
    public Point2D mouseDistance = new Point2D.Double(0, 0);

    public InputHandler() {

        key.put(KeyEvent.VK_W, false);
        key.put(KeyEvent.VK_A, false);
        key.put(KeyEvent.VK_S, false);
        key.put(KeyEvent.VK_D, false);
        key.put(KeyEvent.VK_R, false);
        key.put(KeyEvent.VK_F1, false);
        key.put(KeyEvent.VK_F2, false);
        key.put(KeyEvent.VK_ESCAPE, false);
        key.put(1, false);
        key.put(3, false);

        try {
            robot = new Robot();
            robot.mouseMove(Display.MONITOR_WIDTH / 2, Display.MONITOR_HEIGHT / 2);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static InputHandler getInstance() {
        if (singleInstance == null)
            singleInstance = new InputHandler();

        return singleInstance;
    }

    /**
     * This functions returns the number of movement keys that are pressed.
     */
    public int getMovementKeysPressed() {
        int counter = 0;
        if (key.get(KeyEvent.VK_W)) { ++counter; }
        if (key.get(KeyEvent.VK_A)) { ++counter; }
        if (key.get(KeyEvent.VK_S)) { ++counter; }
        if (key.get(KeyEvent.VK_D)) { ++counter; }
        return counter;
    }

    public void resetMouseDistance() {
        mouseDistance.setLocation(0, 0);
    }

    /**
     * This functions reset the mouse, pause and skip keys.
     */
    public void resetMouseButtons() {
        key.replace(1, false);
        key.replace(3, false);
        key.replace(KeyEvent.VK_F1, false);
        key.replace(KeyEvent.VK_F2, false);
        key.replace(KeyEvent.VK_R, false);
    }

    /**
     * This functions recenter the mouse to the center of the screen.
     */
    private void recenterMouse() {
        robot.mouseMove(Display.MONITOR_WIDTH / 2 + 8, Display.MONITOR_HEIGHT / 2 + 10);
    }

    @Override
    public void focusGained(FocusEvent focusEvent) {

    }

    @Override
    public void focusLost(FocusEvent focusEvent) {
        key.replaceAll((k, v)->v=false);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (keyCode != KeyEvent.VK_F1 && keyCode != KeyEvent.VK_F2) {
            key.replace(keyCode, true);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (keyCode != KeyEvent.VK_F1 && keyCode != KeyEvent.VK_F2 && keyCode != KeyEvent.VK_R) {
            key.replace(keyCode, false);
        } else {
            key.replace(keyCode, !key.get(keyCode));
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        key.replace(mouseEvent.getButton(), true);
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        mouseMoved(mouseEvent);
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        Point2D newMousePosition = mouseEvent.getPoint();
        mouseDistance = new Point2D.Double(newMousePosition.getX() - windowCenter.getX(),
                newMousePosition.getY() - windowCenter.getY());
        if (Math.abs(mouseDistance.getX()) > 2 || Math.abs(mouseDistance.getY()) > 2) {
            recenterMouse();
        }
    }
}
