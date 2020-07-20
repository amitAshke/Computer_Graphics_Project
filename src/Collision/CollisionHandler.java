package Collision;

import LinearAlgebra.Vectors.Vector3D;

import java.util.List;

public class CollisionHandler {
    private CollisionDetector detector;

    public CollisionHandler() {
        detector = new CollisionDetector();
    }

    public Vector3D handleCollisionWithPlayer(Capsule capsule, Vector3D newPosition, List<Collidable> collidables) {
        for (Collidable collidable : collidables) {
            Hitbox hitbox = collidable.getPlayerCollisionShape();
            if (hitbox instanceof AABB) {
                if(detector.detectCollision(capsule, (AABB) hitbox)) {
                    newPosition = collidable.handlePlayerCollision(newPosition);
                }
            } else if (hitbox instanceof Capsule) {
                if (detector.detectCollision(capsule, (Capsule) hitbox)) {
//                    System.out.println("Before collision fix: " + newPosition);
                    newPosition = collidable.handlePlayerCollision(newPosition);
//                    System.out.println("After collision fix: " + newPosition + '\n');
                }
            }
        }
        return newPosition;
    }

    public Collidable handleCollisionWithProjectile(Capsule capsule, List<Collidable> collidables) {
        Collidable collided = null;
        for (Collidable collidable : collidables) {
            Hitbox hitbox = collidable.getProjectileCollisionShape();
            if (hitbox instanceof AABB) {
                if(detector.detectCollision(capsule, (AABB) hitbox)) {
//                    System.out.println("projectile collision with aabb");
                    collided = collidable;
                }
            } else if (hitbox instanceof Capsule) {
                if (detector.detectCollision(capsule, (Capsule) hitbox)) {
//                    System.out.println("projectile collision + with capsule");
                    collided = collidable;
                }
            }
            if (collided != null) {
                collided.projectileCollisionEffect();
            }
        }
        return collided;
    }
}
