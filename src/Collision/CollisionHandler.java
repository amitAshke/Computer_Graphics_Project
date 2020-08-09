package Collision;

import LinearAlgebra.Vectors.Vector3D;

import java.util.List;

/**
 * This class represents an entity that handles collisions between different object in the game.
 * It has a "CollisionDetector" object as a property in order to know when a collision occurs.
 */
public class CollisionHandler {
    private CollisionDetector detector;

    public CollisionHandler() {
        detector = new CollisionDetector();
    }

    /**
     * This function checks for collision between a player and other collidables in the game using the CollisionDetector
       class and when a collision is detected it calls the collidable's function that handles the collision.
     * @param capsule the player's collision shape.
     * @param newPosition the players new position.
     * @param collidables a list of collidable objects.
     * @return the new position of the player. Same as received if no collision was detected, and a new fixed position
       in case a collision occured.
     */
    public Vector3D handleCollisionWithPlayer(Capsule capsule, Vector3D oldPosition, Vector3D newPosition, List<Collidable> collidables) {
        for (Collidable collidable : collidables) {
            Hitbox hitbox = collidable.getPlayerCollisionShape();
            if (hitbox instanceof AABB) {
                if(detector.detectCollision(capsule, (AABB) hitbox)) {
                    newPosition = collidable.handlePlayerCollision(newPosition);
                }
            } else if (hitbox instanceof Capsule) {
                if (detector.detectCollision(capsule, (Capsule) hitbox)) {
                    collidable.handlePlayerCollision(newPosition);
                    newPosition = oldPosition;
                }
            }
        }
        return newPosition;
    }

    /**
     * This function checks for collision between a projectile and other collidables in the game using the
       CollisionDetector class and when a collision is detected it saves the collidable in a local variable and returns
       it at the end of the function.
     * @param capsule the projectile's collision shape.
     * @param collidables a list of collidable objects.
     * @return the collidable that the projectile collided with and null if no collision occured.
     */
    public Collidable handleCollisionWithProjectile(Capsule capsule, List<Collidable> collidables) {
        Collidable collided = null;
        for (Collidable collidable : collidables) {
            Hitbox hitbox = collidable.getProjectileCollisionShape();
            if (hitbox instanceof AABB) {
                if(detector.detectCollision(capsule, (AABB) hitbox)) {
                    collided = collidable;
                    break;
                }
            } else if (hitbox instanceof Capsule) {
                if (detector.detectCollision(capsule, (Capsule) hitbox)) {
                    collided = collidable;
                    break;
                }
            }
        }
        if (collided != null) {
            collided.projectileCollisionEffect();
        }
        return collided;
    }
}
