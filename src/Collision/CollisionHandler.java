package Collision;

import LinearAlgebra.Vectors.Vector3D;
import Main.World;

import java.util.List;

public class CollisionHandler {
    public Vector3D handleCollisionWithPlayer(Vector3D newPosition) {
        List<Collidable> collidables = World.playerCollidables;
        for (Collidable collidable : collidables) {
            if (collidable.isCollidingWithPlayer(newPosition)) {
                newPosition = collidable.handlePlayerCollision(newPosition);
            }
        }
        return newPosition;
    }
}
