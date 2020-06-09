package Player;

import LinearAlgebra.Vectors.Vector3D;

import javax.media.opengl.glu.GLU;

public class Player {

    private Controller controller;
    private Camera camera;

    public static final double HIT_RADIUS = 0.3;

    public Player() {

        Vector3D position = new Vector3D(1.3, 1.2, 3),
//                w_Vector = new Vector3D(Math.sin(Math.toRadians(-30)), 0, Math.cos(Math.toRadians(-30))),
                w_Vector = new Vector3D(0, 0, 1),
                v_Vector = new Vector3D(0, 1, 0);
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
