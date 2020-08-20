package Collision.Shapes;

import LinearAlgebra.Vectors.Vector3D;

/**
 * This class represents a collision shape of type capsule.
 */
public class Capsule implements Hitbox {
    public Vector3D a, b;
    public double radius;

    public Capsule(Vector3D a, Vector3D b, double radius) {
        this.a = a;
        this.b = b;
        this.radius = radius;
    }
}
