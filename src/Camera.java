import javax.media.opengl.glu.GLU;

public class Camera {
    private Vector3D position;
    private Vector3D w_Vector;
    private Vector3D u_Vector;
    private Vector3D v_Vector;

    public Camera(Vector3D position, Vector3D look, Vector3D up) {
        this.position = position;
        this.w_Vector = look.normalize();
        this.v_Vector = up.normalize();
        this.u_Vector = w_Vector.crossProduct(v_Vector).normalize();
    }

    public Vector3D getPosition() { return position; }

    public void move_w(double linearSpeed) {
        position = position.scaleAdd(linearSpeed, w_Vector);
    }

    public void move_u(double linearSpeed) {
        position = position.scaleAdd(linearSpeed, u_Vector);
    }

    public void move_v(double linearSpeed) {
        position = position.scaleAdd(linearSpeed, v_Vector);
    }

    public void setView(GLU glu) {
        glu.gluLookAt(	position.getX(), position.getY(), position.getZ(),
                position.getX() + w_Vector.getX(), position.getY() + w_Vector.getY(), position.getZ() + w_Vector.getZ(),
                v_Vector.getX(), v_Vector.getY(), v_Vector.getZ());
    }
}
