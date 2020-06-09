package Player;

import Collision.Collidable;
import Collision.CollisionHandler;
import LinearAlgebra.Vectors.Vector3D;

import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.HashMap;

public class Controller {

    private InputHandler inputHandler;
    private CollisionHandler collisionHandler;

    private double straightSpeed = 0.05, diagonalSpeed = straightSpeed * Math.sqrt(2) / 2;
    private double horizontalSensitivity = 0.03, verticalSensitivity = 0.03;

    public Controller() {
        this.inputHandler = new InputHandler();
        this.collisionHandler = new CollisionHandler();
    }

    public InputHandler getInputHandler() { return inputHandler; }

    public void handleMovement(Camera camera) {
        HashMap<Integer, Boolean> key = inputHandler.key;
        Vector3D oldPosition = camera.position, newPosition = oldPosition, w_Projection = camera.projectWtoXZ(),
                u_Projection = camera.u_Vector;

        if ( Collections.frequency(key.values(), true) > 1) {
            if (key.get(KeyEvent.VK_W) && key.get(KeyEvent.VK_D)) {
                newPosition = newPosition.scaleAdd(diagonalSpeed, w_Projection);
                newPosition = newPosition.scaleAdd(diagonalSpeed, u_Projection);

            }
            if (key.get(KeyEvent.VK_W) && key.get(KeyEvent.VK_A)) {
                newPosition = newPosition.scaleAdd(diagonalSpeed, w_Projection);
                newPosition = newPosition.scaleAdd(-1 * diagonalSpeed, u_Projection);

            }
            if (key.get(KeyEvent.VK_S) && key.get(KeyEvent.VK_D)) {
                newPosition = newPosition.scaleAdd(-1 * diagonalSpeed, w_Projection);
                newPosition = newPosition.scaleAdd(diagonalSpeed, u_Projection);

            }
            if (key.get(KeyEvent.VK_S) && key.get(KeyEvent.VK_A)) {
                newPosition = newPosition.scaleAdd(-1 * diagonalSpeed, w_Projection);
                newPosition = newPosition.scaleAdd(-1 * diagonalSpeed, u_Projection);

            }
        } else {
            if (key.get(KeyEvent.VK_W)) {
                newPosition = newPosition.scaleAdd(straightSpeed, w_Projection);
            }
            if (key.get(KeyEvent.VK_S)) {
                newPosition = newPosition.scaleAdd(-1 * straightSpeed, w_Projection);
            }
            if (key.get(KeyEvent.VK_D)) {
                newPosition = newPosition.scaleAdd(straightSpeed, u_Projection);
            }
            if (key.get(KeyEvent.VK_A)) {
                newPosition = newPosition.scaleAdd(-1 * straightSpeed, u_Projection);
            }
        }
        newPosition = collisionHandler.handleCollisionWithPlayer(newPosition);

        camera.position = newPosition;
    }

    public void handleRotation(Camera camera) {
        double leftRightAngle = inputHandler.mouseDistance.getX() * horizontalSensitivity;
        double upDownAngle = inputHandler.mouseDistance.getY() * verticalSensitivity;

        if (leftRightAngle != 0 || upDownAngle != 0) {
            camera.rotateUpDown(upDownAngle);
            if (camera.v_Vector.getY() == 0) {
                camera.rotateSideways(leftRightAngle);
            } else {
                camera.rotateLeftRight(leftRightAngle);
            }
            camera.fixOrthogonality();
        }
        inputHandler.resetMouseDistance();
    }
}
