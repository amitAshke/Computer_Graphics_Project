package Player;

import Collision.*;
import LinearAlgebra.Vectors.Vector3D;
import Main.Display;

import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 * This functions represents the controller that update certain things in the Player object based on the inputs.
 */
public class Controller {

    private double straightSpeed = 0.05, diagonalSpeed = straightSpeed * Math.sqrt(2) / 2;
    private double horizontalSensitivity = 0.03, verticalSensitivity = 0.03;
    private boolean specialActive = false;

    public static InputHandler inputHandler;

    public Controller() {
        inputHandler = new InputHandler();
    }

    public void setSpecialActive(boolean specialActive) {
        this.specialActive = specialActive;
    }

    /**
     * This functions handles the camera position based on the input.
     */
    public void handleMovement() {

        // If more than one movement button is pressed.
        if (inputHandler.getMovementKeysPressed() > 1) {
            handleDiagonalMovement();
        }
        // If a single movement button is pressed.
        else {
            handleStraightMovement();
        }
    }

    /**
     * This functions determines the diagonal direction according to the player's input and returns the new player location.
     */
    private void handleDiagonalMovement() {
        HashMap<Integer, Boolean> key = inputHandler.key;
        Camera camera = Player.camera;

        if (key.get(KeyEvent.VK_W) && key.get(KeyEvent.VK_D)) {
            camera.moveForwardOrBackward(diagonalSpeed);
            camera.moveRightOrLeft(diagonalSpeed);
        }
        if (key.get(KeyEvent.VK_W) && key.get(KeyEvent.VK_A)) {
            camera.moveForwardOrBackward(diagonalSpeed);
            camera.moveRightOrLeft(-1 * diagonalSpeed);
        }
        if (key.get(KeyEvent.VK_S) && key.get(KeyEvent.VK_D)) {
            camera.moveForwardOrBackward(-1 * diagonalSpeed);
            camera.moveRightOrLeft(diagonalSpeed);
        }
        if (key.get(KeyEvent.VK_S) && key.get(KeyEvent.VK_A)) {
            camera.moveForwardOrBackward(-1 * diagonalSpeed);
            camera.moveRightOrLeft(-1 * diagonalSpeed);
        }
    }

    /**
     * This functions determines the straight direction according to the player's input and returns the new player location.
     */
    private void handleStraightMovement() {
        HashMap<Integer, Boolean> key = inputHandler.key;
        Camera camera = Player.camera;

        if (key.get(KeyEvent.VK_W)) {
            camera.moveForwardOrBackward(straightSpeed);
        }
        if (key.get(KeyEvent.VK_S)) {
            camera.moveForwardOrBackward(-1 * straightSpeed);
        }
        if (key.get(KeyEvent.VK_D)) {
            camera.moveRightOrLeft(straightSpeed);
        }
        if (key.get(KeyEvent.VK_A)) {
            camera.moveRightOrLeft(-1 * straightSpeed);
        }
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
            handleStandardProjectileActivation();
        }
        // If the player clicked on the right button.
        else if (inputHandler.key.get(3)) {
            handleSpecialProjectileActivation();
        }

        // Reset the mouse buttons.
        inputHandler.resetMouseButtons();
    }

    private void handleStandardProjectileActivation() {
        Camera camera = Player.camera;

        // If the player has not reached his projectile limit with or without the special attack.
        if ((!specialActive && Player.projectiles.size() < Player.projectileLimit) ||
                (specialActive && Player.projectiles.size() + Player.specialProjectile.getProjectiles().size() < Player.projectileLimit)) {

            if (Player.projectileFireCooldown.canActivate()) {

                Player.projectiles.add(new StandardProjectile(camera.position, camera.w_Vector, camera.v_Vector, -1));
                Player.projectileFireCooldown.activated();
                Display.soundPlayer.playStandard();
            }
        }
        inputHandler.resetMouseButtons();
    }

    private void handleSpecialProjectileActivation() {
        Camera camera = Player.camera;

        if (!specialActive && Player.projectiles.size() == 0) {
            Player.specialProjectile = new SpecialProjectile(camera.position, new Vector3D(0, 0, 1));
            inputHandler.resetMouseButtons();
            specialActive = true;
            Display.soundPlayer.playSpecial();
        } else if (specialActive) {
            Player.specialProjectile.detachProjectiles();
            Player.projectiles = Player.specialProjectile.getProjectiles();
            specialActive = false;
            inputHandler.resetMouseButtons();
            Player.specialProjectile = null;
            Display.soundPlayer.playSpecial();
        }
    }

        public void resetValues() {
        specialActive = false;
    }
}
