package Collision;

import LinearAlgebra.Vectors.Vector3D;

import java.util.List;

public class CollisionHandler {
    public Vector3D handleCollisionWithPlayer(Vector3D newPosition, List<Collidable> collidables) {
        for (Collidable collidable : collidables) {
            if (collidable.isCollidingWithPlayer(newPosition)) {
                newPosition = collidable.handlePlayerCollision(newPosition);
            }
        }
        return newPosition;
    }

    public Collidable handleCollisionWithProjectile(Vector3D position, double length, double radius, Vector3D direction, List<Collidable> collidables) {
        Collidable collided = null;
        for (Collidable collidable : collidables) {
            if (collidable.isCollidingWithProjectile(position, length, radius, direction)) {
                System.out.println("collision");
                collided = collidable;
            }
        }
        return collided;
    }
}
