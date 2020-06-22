package Collision;

import LinearAlgebra.Vectors.Vector3D;

public class Capsule implements Hitbox {
    public Vector3D a, b;
    public double radius;

    public Capsule(Vector3D a, Vector3D b, double radius) {
        this.a = a;
        this.b = b;
        this.radius = radius;
    }
}
