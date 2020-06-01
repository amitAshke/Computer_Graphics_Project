import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.HashMap;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener, FocusListener {

    Robot robot;
    private boolean robotEvent = false;
    private final Point2D windowCenter = new Point2D.Double(Display.WINDOW_WIDTH / 2.0, Display.WINDOW_HEIGHT / 2.0);

    public HashMap<Integer, Boolean> key = new HashMap<>();
    public Point2D mouseDistance = new Point2D.Double(0, 0);

    public InputHandler() {

        key.put(KeyEvent.VK_W, false);
        key.put(KeyEvent.VK_A, false);
        key.put(KeyEvent.VK_S, false);
        key.put(KeyEvent.VK_D, false);

        try {
            robot = new Robot();
            robot.mouseMove(Display.MONITOR_WIDTH / 2, Display.MONITOR_HEIGHT / 2);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void resetMouseDistance() {
        mouseDistance.setLocation(0, 0);
    }

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
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        key.replace(keyCode, true);
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        key.replace(keyCode, false);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

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

    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        Point2D newMousePosition = mouseEvent.getPoint();
        mouseDistance = new Point2D.Double(newMousePosition.getX() - windowCenter.getX(),
                newMousePosition.getY() - windowCenter.getY());
        if (mouseDistance.getX() != 0 || mouseDistance.getY() != 0) {
            recenterMouse();
        }
    }
}
