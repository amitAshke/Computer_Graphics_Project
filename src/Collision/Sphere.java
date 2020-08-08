package Collision;

import LinearAlgebra.Vectors.Vector3D;

/**
 * This class represents a collision shape of type sphere.
 */
public class Sphere {
    public Vector3D center;
    public double radius;

    public Sphere(Vector3D center, double radius) {
        this.center = center;
        this.radius = radius;
    }
}
