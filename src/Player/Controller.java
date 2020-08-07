package Player;

import Collision.*;
import LinearAlgebra.Vectors.Vector3D;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;

public class Controller {

    private SoundPlayer soundPlayer;
    private double straightSpeed = 0.05, diagonalSpeed = straightSpeed * Math.sqrt(2) / 2;
    private double horizontalSensitivity = 0.03, verticalSensitivity = 0.03;
    private long lastTimeFired = System.nanoTime();
    private boolean altActive = false;
    File soundFile = new File("src\\resources\\sfx\\StandardActivation1.wav");

    public static InputHandler InputHandler;

    public Controller() {
        InputHandler = new InputHandler();
        this.soundPlayer = new SoundPlayer();
    }

    public void setAltActive(boolean altActive) {
        this.altActive = altActive;
    }

    public void handleMovement() {
        Camera camera = Player.camera;
        HashMap<Integer, Boolean> key = InputHandler.key;

        Vector3D oldPosition = camera.position, newPosition = oldPosition, u_Projection = camera.u_Vector, w_Projection;

        if (camera.v_Vector.getY() > 0) {
             w_Projection = camera.w_Vector.projectToXZ().normalize();
        } else {
            if (camera.w_Vector.getY() == 1) {
                w_Projection = camera.v_Vector.projectToXZ().neg().normalize();
            } else {
                w_Projection = camera.v_Vector.projectToXZ().normalize();
            }
        }

        if (InputHandler.getMovementKeysPressed() > 1) {
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

        camera.position = newPosition;
    }

    public void handleRotation() {
        Camera camera = Player.camera;
        double leftRightAngle = InputHandler.mouseDistance.getX() * horizontalSensitivity;
        double upDownAngle = InputHandler.mouseDistance.getY() * verticalSensitivity;

        if (leftRightAngle != 0 || upDownAngle != 0) {
            camera.rotateUpDown(upDownAngle);
            camera.rotateLeftRight(leftRightAngle);
            camera.fixOrthogonality();
        }
        InputHandler.resetMouseDistance();
    }

    public void handleFire() {
        Camera camera = Player.camera;

        if (InputHandler.key.get(1)) {
            if (Player.projectiles.size() < Player.projectileLimit && !altActive) {
                long currentTimeFire = System.nanoTime(), passedTime = currentTimeFire - lastTimeFired;
                if (passedTime / 1000000000.0 > Player.COOLDOWN) {
                    lastTimeFired = currentTimeFire;

                    Player.projectiles.add(new StandardProjectile("src\\resources\\models\\Dagger.obj",
                            "src\\resources\\models\\textures\\Dagger_1K_Diffuse.png",
                            "",
                            camera.position, camera.w_Vector, camera.v_Vector));
                    soundPlayer.playStandard();
                }
            }
            InputHandler.resetMouseButtons();
        } else if (InputHandler.key.get(3)) {
            if (!altActive && Player.projectiles.size() == 0) {
                Player.altProjectile = new AltProjectile("src\\resources\\models\\Dagger.obj",
                        "src\\resources\\models\\textures\\Dagger_1K_Diffuse.png",
                        "",
                        camera.position, new Vector3D(0, 0, 1));
                InputHandler.resetMouseButtons();
                altActive = true;
                soundPlayer.playSpecial();
            } else if (altActive) {
                Player.altProjectile.detachProjectiles();
                Player.projectiles = Player.altProjectile.getProjectiles();
                altActive = false;
                InputHandler.resetMouseButtons();
                Player.altProjectile = null;
            }
        }
        InputHandler.resetMouseButtons();
    }
}
