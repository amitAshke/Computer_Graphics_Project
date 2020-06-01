import javax.media.opengl.glu.GLU;
import java.awt.event.KeyEvent;

public class Player {

    private Controller controller;
    private Camera camera;

    public Player() {

        Vector3D position = new Vector3D(0.5, 1.2, 0.5), w_Vector = new Vector3D(0, 0, 1), v_Vector = new Vector3D(0, 1, 0);
        camera = new Camera(position, w_Vector, v_Vector);
        controller = new Controller();
    }

    public Camera getCamera() {
        return camera;
    }

    public Controller getController() { return controller; }

    public void tick() {
        controller.handleMovement(camera);

        controller.handleRotation(camera);
    }

    public void setView(GLU glu) {
        camera.setView(glu);
    }
}
