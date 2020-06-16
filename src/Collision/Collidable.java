package Collision;

import LinearAlgebra.Vectors.Vector3D;

public interface Collidable {

    public float getX1();

    public float getX2();

    public float getZ1();

    public float getZ2();

    public boolean isCollidingWithPlayer(Vector3D playerPosition);

    public Vector3D handlePlayerCollision(Vector3D newPosition);

    public boolean isCollidingWithProjectile(Vector3D position, double length, double radius, Vector3D direction);
}
