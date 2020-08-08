package Player;

import Collision.*;
import LinearAlgebra.Vectors.Vector3D;

import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 * This functions represents the controller that update certain things in the Player object based on the inputs.
 */
public class Controller {

    private double straightSpeed = 0.05, diagonalSpeed = straightSpeed * Math.sqrt(2) / 2;
    private double horizontalSensitivity = 0.03, verticalSensitivity = 0.03;
    private long lastTimeFired = System.nanoTime();
    private boolean altActive = false;

    public static InputHandler inputHandler;
    public static SoundPlayer soundPlayer;

    public Controller() {
        inputHandler = new InputHandler();
        this.soundPlayer = new SoundPlayer();
    }

    public void setAltActive(boolean altActive) {
        this.altActive = altActive;
    }

    /**
     * This functions handles the camera position based on the input.
     */
    public void handleMovement() {
        Camera camera = Player.camera;
        HashMap<Integer, Boolean> key = inputHandler.key;

        Vector3D oldPosition = camera.position, newPosition = oldPosition, u_Projection = camera.u_Vector, w_Projection;

        // Make projection of the forward vector of the camera to the XZ plane.
        if (camera.v_Vector.getY() > 0) {
             w_Projection = camera.w_Vector.projectToXZ().normalize();
        } else {
            if (camera.w_Vector.getY() == 1) {
                w_Projection = camera.v_Vector.projectToXZ().neg().normalize();
            } else {
                w_Projection = camera.v_Vector.projectToXZ().normalize();
            }
        }

        // If more than one movement button is pressed.
        if (inputHandler.getMovementKeysPressed() > 1) {
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
        }
        // If a single movement button is pressed.
        else {
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

        // Sets the new position of the camera.
        camera.position = newPosition;
    }

    /**
     * This functions handles the camera rotation based on the input.
     */
    public void handleRotation() {
        Camera camera = Player.camera;
        double leftRightAngle = inputHandler.mouseDistance.getX() * horizontalSensitivity;
        double upDownAngle = inputHandler.mouseDistance.getY() * verticalSensitivity;

        if (leftRightAngle != 0 || upDownAngle != 0) {
            camera.rotateUpDown(upDownAngle);
            camera.rotateLeftRight(leftRightAngle);
            camera.fixOrthogonality();
        }
        inputHandler.resetMouseDistance();
    }

    /**
     * This functions projectiles that the player fires based on the input.
     */
    public void handleFire() {
        Camera camera = Player.camera;

        // If the player clicked on the left mouse button.
        if (inputHandler.key.get(1)) {
            // If the player has not reached his projectile limit and the special attack is not active.
            if (Player.projectiles.size() < Player.projectileLimit && !altActive) {
                long currentTimeFire = System.nanoTime(), passedTime = currentTimeFire - lastTimeFired;
                // If the time that passed since the last shot is greater than the cooldown.
                if (passedTime / 1000000000.0 > Player.COOLDOWN) {
                    lastTimeFired = currentTimeFire;

                    Player.projectiles.add(new StandardProjectile("src\\resources\\models\\Dagger.obj",
                            "src\\resources\\models\\textures\\Dagger_1K_Diffuse.png",
                            "",
                            camera.position, camera.w_Vector, camera.v_Vector));
                    soundPlayer.playStandard();
                }
            }
            inputHandler.resetMouseButtons();
        }
        // If the player clicked on the right button.
        else if (inputHandler.key.get(3)) {
            if (!altActive && Player.projectiles.size() == 0) {
                Player.altProjectile = new AltProjectile("src\\resources\\models\\Dagger.obj",
                        "src\\resources\\models\\textures\\Dagger_1K_Diffuse.png",
                        "",
                        camera.position, new Vector3D(0, 0, 1));
                inputHandler.resetMouseButtons();
                altActive = true;
                soundPlayer.playSpecial();
            } else if (altActive) {
                Player.altProjectile.detachProjectiles();
                Player.projectiles = Player.altProjectile.getProjectiles();
                altActive = false;
                inputHandler.resetMouseButtons();
                Player.altProjectile = null;
            }
        }

        // Reset the mouse buttons.
        inputHandler.resetMouseButtons();
    }
}
