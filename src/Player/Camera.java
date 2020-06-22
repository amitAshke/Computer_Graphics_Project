package Player;

import LinearAlgebra.Matrices.RotationMatrix3D;
import LinearAlgebra.Matrices.WorldViewMatrix3D;
import LinearAlgebra.Vectors.Vector3D;

import javax.media.opengl.glu.GLU;

public class Camera {
    public Vector3D position, w_Vector, u_Vector, v_Vector;

    public Camera(Vector3D position, Vector3D look, Vector3D up) {
        this.position = position;
        this.w_Vector = look.normalize();
        this.v_Vector = up.normalize();
        this.u_Vector = w_Vector.crossProduct(v_Vector).normalize();
    }

    public Vector3D getPosition() { return position; }

    public void move_w(double linearSpeed) { position = position.scaleAdd(linearSpeed, w_Vector); }

    public void move_u(double linearSpeed) {
        position = position.scaleAdd(linearSpeed, u_Vector);
    }

    public void move_v(double linearSpeed) {
        position = position.scaleAdd(linearSpeed, v_Vector);
    }

    public void fixOrthogonality() {
        if (w_Vector.dotProduct(u_Vector) > 0.01 || w_Vector.dotProduct(u_Vector) < -0.01) {
//            System.out.println(w_Vector.dotProduct(u_Vector) + " => w-u: not orthogonal");
//            System.out.println("angle:" + w_Vector.angle(u_Vector));
//            System.out.println("w_vector:  " + w_Vector);
//            System.out.println("u_vector:  " + u_Vector);
            u_Vector.setY(0);
            u_Vector = u_Vector.normalize();
            w_Vector = v_Vector.crossProduct(u_Vector);
//            System.out.println("new w_vector:  " + w_Vector);
//            System.out.println("");
        }
        if (v_Vector.dotProduct(u_Vector) > 0.01 || v_Vector.dotProduct(u_Vector) < -0.01) {
//            System.out.println(v_Vector.dotProduct(u_Vector) + " => v-u: not orthogonal");
//            System.out.println("angle:" + u_Vector.angle(v_Vector));
//            System.out.println("v_vector:  " + v_Vector);
//            System.out.println("u_vector:  " + u_Vector);
            u_Vector.setY(0);
            u_Vector = u_Vector.normalize();
            v_Vector = u_Vector.crossProduct(w_Vector);
//            System.out.println("new v_vector:  " + v_Vector);
//            System.out.println("");
        }
        if (w_Vector.dotProduct(v_Vector) > 0.01 || w_Vector.dotProduct(v_Vector) < -0.01) {
//            System.out.println(w_Vector.dotProduct(v_Vector) + " => w-v: not orthogonal");
//            System.out.println("angle:" + w_Vector.angle(v_Vector));
//            System.out.println("w_vector:  " + w_Vector);
//            System.out.println("v_vector:  " + v_Vector);
            u_Vector.setY(0);
            u_Vector = u_Vector.normalize();
            v_Vector = u_Vector.crossProduct(w_Vector);
//            System.out.println("new v_vector:  " + v_Vector);
//            System.out.println("");
        }
        return;
    }

    public void rotateUpDown(double angle) {

        if (angle == 0) { return; }

        Vector3D[] uvw = {u_Vector, v_Vector, w_Vector};

        WorldViewMatrix3D worldViewMatrix = new WorldViewMatrix3D(position, uvw);
        RotationMatrix3D rotate = new RotationMatrix3D(angle, 'x');
        WorldViewMatrix3D reversedWorldViewMatrix = worldViewMatrix.transpose();

//        LinearAlgebra.Matrices.TransformationMatrix3D transformation = reversedWorldViewMatrix.matrixMultiplication(rotate.matrixMultiplication(worldViewMatrix));
//        w_Vector = transformation.transform(w_Vector);
//        v_Vector = transformation.transform(v_Vector);

        w_Vector = worldViewMatrix.transform(w_Vector).normalize();
        w_Vector = rotate.transform(w_Vector).normalize();
        w_Vector = reversedWorldViewMatrix.transform(w_Vector).normalize();

        v_Vector = worldViewMatrix.transform(v_Vector).normalize();
        v_Vector = rotate.transform(v_Vector).normalize();
        v_Vector = reversedWorldViewMatrix.transform(v_Vector).normalize();

        if (v_Vector.getY() < 0) {
            v_Vector.setY(0);
            v_Vector = v_Vector.normalize();
            w_Vector = v_Vector.crossProduct(u_Vector);
        }
    }

    public void rotateLeftRight(double angle) {

        if (angle == 0) { return; }

//        LinearAlgebra.Vectors.Vector3D[] uvw = {u_Vector, v_Vector, w_Vector};

//        LinearAlgebra.Matrices.WorldViewMatrix3D worldViewMatrix = new LinearAlgebra.Matrices.WorldViewMatrix3D(position, uvw);
        RotationMatrix3D rotate = new RotationMatrix3D(angle, 'y');
//        LinearAlgebra.Matrices.WorldViewMatrix3D reversedWorldViewMatrix = worldViewMatrix.transpose();

//        LinearAlgebra.Matrices.TransformationMatrix3D transformation = reversedWorldViewMatrix.matrixMultiplication(rotate.matrixMultiplication(worldViewMatrix));
//        w_Vector = transformation.transform(w_Vector);
//        u_Vector = transformation.transform(u_Vector);

//        w_Vector = worldViewMatrix.transform(w_Vector).normalize();
        w_Vector = rotate.transform(w_Vector).normalize();
//        w_Vector = reversedWorldViewMatrix.transform(w_Vector).normalize();

//        u_Vector = worldViewMatrix.transform(u_Vector).normalize();
        u_Vector = rotate.transform(u_Vector).normalize();
//        u_Vector = reversedWorldViewMatrix.transform(u_Vector).normalize();
    }

    public Vector3D projectWtoXZ() {
        return w_Vector.projectToXZ();
    }

    public void setView(GLU glu) {
        glu.gluLookAt(	position.getX(), position.getY(), position.getZ(),
                position.getX() + w_Vector.getX(), position.getY() + w_Vector.getY(), position.getZ() + w_Vector.getZ(),
                v_Vector.getX(), v_Vector.getY(), v_Vector.getZ());
    }

    public double getUpDownAngle() {
        double angle = Math.toDegrees(Math.acos(this.projectWtoXZ().dotProduct(w_Vector)));

        if (w_Vector.getY() > 0) {
            angle *= -1;
        }

        return angle;
    }

    public double getLeftRightAngle() {
        double angle = 0;

        if (Player.camera.w_Vector.getX() < 0) {
            angle = Math.toDegrees(Math.acos(Player.camera.projectWtoXZ().dotProduct(new Vector3D(0, 0, 1)))) * -1;
        } else if (Player.camera.w_Vector.getX() > 0) {
            angle = Math.toDegrees(Math.acos(Player.camera.projectWtoXZ().dotProduct(new Vector3D(0, 0, 1))));
        } else {
            if (Player.camera.v_Vector.getX() < 0) {
                angle = Math.toDegrees(Math.acos(Player.camera.v_Vector.dotProduct(new Vector3D(0, 0, 1)))) * -1;
            } else if (Player.camera.v_Vector.getX() > 0) {
                angle = Math.toDegrees(Math.acos(Player.camera.v_Vector.dotProduct(new Vector3D(0, 0, 1))));
            }
        }

        return angle;
    }
}
