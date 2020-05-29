import com.jogamp.newt.Window;
import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.awt.AWTKeyAdapter;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class Display implements GLEventListener {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final String TITLE = "CG_Project_Blue";

    static GLU glu = new GLU();
    static GLCanvas canvas = new GLCanvas();
    static Frame frame = new Frame();
    static Animator animator = new Animator(canvas);
    static Render3D render3D;

    private int framesRendered = 0, tickCount = 0;
    private double unprocessedSeconds = 0, secondsPerTick = 1 / 60.0;
    private long previousTime = System.nanoTime();
    private boolean ticked = false;


    public static void main(String[] args) {
        canvas.addGLEventListener(new Display());
        frame.add(canvas);
        frame.pack();
        frame.setTitle(TITLE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        animator.start();
        canvas.requestFocus();
    }

    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH); // Enable Smooth Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Black Background
        gl.glClearDepth(1.0f); // Depth Buffer Setup
        gl.glEnable(GL2.GL_DEPTH_TEST); // Enables Depth Testing
        gl.glDepthFunc(GL2.GL_LEQUAL); // The Type Of Depth Testing To Do

        // Really Nice Perspective Calculations
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

        render3D = new Render3D(gl);
    }

    public void dispose(GLAutoDrawable glAutoDrawable) {}

    public void display(GLAutoDrawable glAutoDrawable) {
        long currentTime = System.nanoTime();
        long passedTime = currentTime - previousTime;
        previousTime = currentTime;
        unprocessedSeconds += passedTime / 1000000000.0;

        while (unprocessedSeconds > secondsPerTick) {
            //tick();
            unprocessedSeconds -= secondsPerTick;
            ticked = true;
            ++tickCount;
            if (tickCount % 60 == 0) {
                System.out.println(framesRendered + " FPS");
                previousTime += 1000;
                framesRendered = 0;
            }
        }

        if (ticked) {
            //render();
            ++framesRendered;
        }
        //render();
        ++framesRendered;

        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();
        glu.gluLookAt(0.5, 1, 0.5, 0.5, 1, 1.5, 0, 1, 0);

        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(50,WIDTH/HEIGHT,1,1000);

        render3D.floor(gl, 0, 0, 10);

    }

    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {}
}
