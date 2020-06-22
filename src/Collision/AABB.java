package Collision;

public class AABB implements Hitbox {
    private double x1, x2, y1, y2, z1, z2;

    public AABB(double x1, double x2, double y1, double y2, double z1, double z2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.z1 = z1;
        this.z2 = z2;
    }

    public double minX() {
        return Math.min(x1, x2);
    }

    public double maxX() {
        return Math.max(x1, x2);
    }

    public double minY() {
        return Math.min(y1, y2);
    }

    public double maxY() {
        return Math.max(y1, y2);
    }

    public double minZ() {
        return Math.min(z1, z2);
    }

    public double maxZ() {
        return Math.max(z1, z2);
    }
}
