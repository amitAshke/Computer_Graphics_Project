package LinearAlgebra.Vectors;

import LinearAlgebra.Matrices.RotationMatrix3D;

/**
 * General vector in 3D space. It's first point at the origin.
 */
public class Vector3D {
    private double x, y, z;

    public Vector3D() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public final double getX() {
        return this.x;
    }

    public final double getY() {
        return this.y;
    }

    public final double getZ() {
        return this.z;
    }

    public void setX(double x) { this.x = x; }

    public void setY(double y) { this.y = y; }

    public void setZ(double z) { this.z = z; }

    public Vector3D clone() {
        return new Vector3D(x, y, z);
    }

    public double distance(double x1, double y1, double z1) {
        double a = this.getX() - x1;
        double b = this.getY() - y1;
        double c = this.getZ() - z1;
        return Math.sqrt(a * a + b * b + c * c);
    }

    public Vector3D neg() {
        return new Vector3D(-1 * x, -1 * y, -1 * z);
    }

    public double distance(Vector3D point) {
        return this.distance(point.getX(), point.getY(), point.getZ());
    }

    public Vector3D add(double x, double y, double z) {
        return new Vector3D(this.x + x, this.y + y, this.z + z);
    }

    public Vector3D add(Vector3D vector) {
        return this.add(vector.getX(), vector.getY(), vector.getZ());
    }

    public Vector3D subtract(double x, double y, double z) {
        return new Vector3D(this.getX() - x, this.getY() - y, this.getZ() - z);
    }

    public Vector3D subtract(Vector3D vector) {
        return this.subtract(vector.getX(), vector.getY(), vector.getZ());
    }

    public Vector3D scale(double factor) {
        return new Vector3D(this.x * factor, this.y * factor, this.z * factor);
    }

    public Vector3D scaleAdd(double factor, Vector3D vector3D) {
        return this.add(vector3D.scale(factor));
    }

    public Vector3D normalize() {
        double mag = this.magnitude();
        return mag == 0.0D ? new Vector3D(0.0D, 0.0D, 0.0D) :
                new Vector3D(this.getX() / mag, this.getY() / mag, this.getZ() / mag);
    }

    public Vector3D midpoint(double x, double y, double z) {
        return new Vector3D(x + (this.getX() - x) / 2.0D, y + (this.getY() - y) / 2.0D, z + (this.getZ() - z) / 2.0D);
    }

    public Vector3D midpoint(Vector3D point) {
        return this.midpoint(point.getX(), point.getY(), point.getZ());
    }

    public double angle(double x, double y, double z) {
        double ax = this.getX();
        double ay = this.getY();
        double az = this.getZ();
        double delta = (ax * x + ay * y + az * z) / Math.sqrt((ax * ax + ay * ay + az * az) * (x * x + y * y + z * z));
        if (delta > 1.0D) {
            return 0.0D;
        } else {
            return delta < -1.0D ? 180.0D : Math.toDegrees(Math.acos(delta));
        }
    }

    public double angle(Vector3D point) {
        return this.angle(point.getX(), point.getY(), point.getZ());
    }

    public double angle(Vector3D p1, Vector3D p2) {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        double ax = p1.getX() - x;
        double ay = p1.getY() - y;
        double az = p1.getZ() - z;
        double bx = p2.getX() - x;
        double by = p2.getY() - y;
        double bz = p2.getZ() - z;
        double delta = (ax * bx + ay * by + az * bz) / Math.sqrt((ax * ax + ay * ay + az * az) * (bx * bx + by * by + bz * bz));
        if (delta > 1.0D) {
            return 0.0D;
        } else {
            return delta < -1.0D ? 180.0D : Math.toDegrees(Math.acos(delta));
        }
    }

    public double magnitude() {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double dotProduct(double x, double y, double z) {
        return this.getX() * x + this.getY() * y + this.getZ() * z;
    }

    public double dotProduct(Vector3D vector) {
        return this.dotProduct(vector.getX(), vector.getY(), vector.getZ());
    }

    public Vector3D crossProduct(double x, double y, double z) {
        double ax = this.getX();
        double ay = this.getY();
        double az = this.getZ();
        return new Vector3D(ay * z - az * y, az * x - ax * z, ax * y - ay * x);
    }

    public Vector3D crossProduct(Vector3D vector) {
        return this.crossProduct(vector.getX(), vector.getY(), vector.getZ());
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof Vector3D)) {
            return false;
        } else {
            Vector3D other = (Vector3D)obj;
            return this.getX() == other.getX() && this.getY() == other.getY() && this.getZ() == other.getZ();
        }
    }

    public Vector3D rotate(double angle, char axis) {
        RotationMatrix3D rotationMatrix = new RotationMatrix3D(angle, axis);
        return rotationMatrix.transform(this);
    }

    public Vector3D projectToXZ() {
        return new Vector3D(x, 0 , z);
    }

    public String toString() {
        return "[x = " + this.getX() + ", y = " + this.getY() + ", z = " + this.getZ() + "]";
    }
}
