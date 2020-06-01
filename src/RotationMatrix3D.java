import java.lang.Math;

public class RotationMatrix3D extends TransformationMatrix3D {

    public RotationMatrix3D(double theta, char axis) {
        this.matrix = switchAxis(super.zeroMatrix(), theta, axis);
    }

    public void buildMatrix(double theta, char axis) {
        double[][] r = super.zeroMatrix();
        r[3][3] = 1;
        r = switchAxis(r, theta, axis);
        this.matrix = r;
        double[][] t1 = new TranslateMatrix3D(-this.pos.getX(), -this.pos.getY(), -this.pos.getZ()).getMatrix();
        double[][] t2 = new TranslateMatrix3D(this.pos.getX(), this.pos.getY(), this.pos.getZ()).getMatrix();
        this.matrix = matrixMultiplication(t2, matrixMultiplication(r, t1));
    }

    public double[][] xCase(double[][] m, double theta) {
        m[0][0] = 1;
        m[1][1] = m[2][2] = Math.cos(theta);
        m[1][2] = -Math.sin(theta);
        m[2][1] = Math.sin(theta);
        return m;
    }

    public double[][] yCase(double[][] m, double theta) {
        m[1][1] = 1;
        m[0][0] = m[2][2] = Math.cos(theta);
        m[0][2] = -Math.sin(theta);
        m[2][0] = Math.sin(theta);
        return m;
    }

    public double[][] zCase(double[][] m, double theta) {
        m[2][2] = 1;
        m[0][0] = m[1][1] = Math.cos(theta);
        m[0][1] = -Math.sin(theta);
        m[1][0] = Math.sin(theta);
        return m;
    }

    public double[][] switchAxis(double[][] m, double theta, char axis) {
        axis = Character.toLowerCase(axis);
        switch (axis) {
            case 'x':
                return xCase(m, theta);
            case 'y':
                return yCase(m, theta);
            case 'z':
                return zCase(m, theta);
            default:
                return m;
        }
    }
}
