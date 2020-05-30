import java.awt.event.*;
import java.util.HashMap;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener, FocusListener {

    public HashMap<Integer, Boolean> key = new HashMap<>();

    public InputHandler() {

        key.put(KeyEvent.VK_W, false);
        key.put(KeyEvent.VK_A, false);
        key.put(KeyEvent.VK_S, false);
        key.put(KeyEvent.VK_D, false);
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

    }
}
