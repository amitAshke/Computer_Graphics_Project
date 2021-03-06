package Collision;

import Collision.Shapes.AABB;
import Collision.Shapes.Capsule;
import Collision.Shapes.Sphere;
import LinearAlgebra.Vectors.Vector3D;

/**
 * This class represents an entity that uses algorithms to detection between different shapes.
 */
public class CollisionDetector {

    /**
     * This function runs an algorithms that detects a collision between a capsule and an AABB and returns the result.
     */
    public boolean detectCollision(Capsule capsule, AABB aabb) {
        Vector3D a = capsule.a, b = capsule.b;
        double r = capsule.radius, maxX = aabb.maxX(), minX = aabb.minX(), maxY = aabb.maxY(), minY = aabb.minY(), maxZ = aabb.maxZ(), minZ = aabb.minZ();

        if (b.getY() < minY || a.getY() > maxY) {
            return (Math.abs(a.getX() - maxX) < r || Math.abs(a.getX() - minX) < r ||
                    Math.abs(a.getZ() - maxZ) < r || Math.abs(a.getZ() - minZ) < r) &&
                    Math.abs(a.getY() - maxY) < r || Math.abs(a.getY() - minY) < r;
        } else {
            return detectCollision(new Sphere(a, r), aabb) || detectCollision(new Sphere(b, r), aabb);
        }
    }

    /**
     * This function runs an algorithms that detects a collision between a sphere and an AABB and returns the result.
     */
    public boolean detectCollision(Sphere sphere, AABB aabb) {
        Vector3D c = sphere.center;
        double r = sphere.radius, maxX = aabb.maxX(), minX = aabb.minX(), maxY = aabb.maxY(), minY = aabb.minY(), maxZ = aabb.maxZ(), minZ = aabb.minZ();

        double x = Math.max(minX, Math.min(c.getX(), maxX));
        double y = Math.max(minY, Math.min(c.getY(), maxY));
        double z = Math.max(minZ, Math.min(c.getZ(), maxZ));

        double distance = Math.sqrt((x - c.getX()) * (x - c.getX()) +
                (y - c.getY()) * (y - c.getY()) +
                (z - c.getZ()) * (z - c.getZ()));

        return distance < r;
    }

    /**
     * This function runs an algorithms that detects a collision between two capsules and returns the result.
     */
    public boolean detectCollision(Capsule capsule1, Capsule capsule2) {
        Vector3D a1 = capsule1.a, b1 = capsule1.b, a2 = capsule2.a, b2 = capsule2.b;
        double r1 = capsule1.radius, r2 = capsule2.radius, distance;

        if (a1.getY() > b2.getY() || b1.getY() < a2.getY()) {
            double da = a1.distance(a2), db = b1.distance(b2);
            distance = Math.min(da, db);
        } else {
            distance = Math.min(Math.sqrt(Math.pow(a1.getX() - a2.getX(), 2) + Math.pow(a1.getZ() - a2.getZ(), 2)),
                    Math.sqrt(Math.pow(b1.getX() - b2.getX(), 2) + Math.pow(b1.getZ() - b2.getZ(), 2)));
        }

        return distance < r1 + r2;
    }
}
