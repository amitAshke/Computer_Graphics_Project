import javax.media.opengl.glu.GLU;
import java.awt.event.KeyEvent;

public class Player {

    private Camera camera;
    private InputHandler inputHandler;
    private double straightSpeed = 0.05;
    private double diagonalSpeed = Math.sqrt(2) / 40;

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    public Camera getCamera() {
        return camera;
    }

    public Player() {
        inputHandler = new InputHandler();
        camera = new Camera(new Vector3D(0.5, 1, 0.5),
                new Vector3D(0, 0, 1),
                new Vector3D(0, 1, 0));
    }

    public void tick() {
        if (inputHandler.key.get(KeyEvent.VK_W) && inputHandler.key.get(KeyEvent.VK_D)) {
            camera.move_w(diagonalSpeed);
            camera.move_u(diagonalSpeed);
        } else if (inputHandler.key.get(KeyEvent.VK_W) && inputHandler.key.get(KeyEvent.VK_A)) {
            camera.move_w(diagonalSpeed);
            camera.move_u(-1 * diagonalSpeed);
        } else if (inputHandler.key.get(KeyEvent.VK_S) && inputHandler.key.get(KeyEvent.VK_D)) {
            camera.move_w(-1 * diagonalSpeed);
            camera.move_u(diagonalSpeed);
        } else if (inputHandler.key.get(KeyEvent.VK_S) && inputHandler.key.get(KeyEvent.VK_A)) {
            camera.move_w(-1 * diagonalSpeed);
            camera.move_u(-1 * diagonalSpeed);
        } else {
            if (inputHandler.key.get(KeyEvent.VK_W)) { camera.move_w(straightSpeed); }
            if (inputHandler.key.get(KeyEvent.VK_S)) { camera.move_w(-1 * straightSpeed); }
            if (inputHandler.key.get(KeyEvent.VK_D)) { camera.move_u(straightSpeed); }
            if (inputHandler.key.get(KeyEvent.VK_A)) { camera.move_u(-1 * straightSpeed); }
        }
    }

    public void setView(GLU glu) {
        camera.setView(glu);
    }
}
