package Main;

import Player.Player;
import Renderables.WavefrontObject;
import com.jogamp.opengl.util.Animator;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class Display implements GLEventListener {

    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static int MONITOR_WIDTH;
    public static int MONITOR_HEIGHT;
    public static final String TITLE = "CG_Project_Blue";
    public static int projectileModel;
    public static int dummyModel;
    public static int lampBaseModel;
    public static int sphereModel;

    static GLCanvas canvas = new GLCanvas();
    static Frame frame = new Frame();
    static Animator animator = new Animator(canvas);
    private World world;
    Render3D render3D;

    private int framesRendered = 0, tickCount = 0;
    private double unprocessedSeconds = 0, secondsPerTick = 1 / 60.0;
    private long previousTime = System.nanoTime();


    public static void main(String[] args) {
        // create the cursor
        Toolkit t = Toolkit.getDefaultToolkit();
        Image i = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        MONITOR_WIDTH = (int)t.getScreenSize().getWidth();
        MONITOR_HEIGHT = (int)t.getScreenSize().getHeight();
        Cursor noCursor = t.createCustomCursor(i, new Point(0, 0), "none");

        canvas.addGLEventListener(new Display());
        frame.add(canvas);
        frame.pack();
        frame.setTitle(TITLE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setCursor(noCursor);
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

        // Initialize lighting
        float	ambient[] = {0.1f,0.1f,0.1f,1.0f};
        float	diffuse0[] = {1f,1f,1f,1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse0, 0);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_LIGHTING);

        // Really Nice Perspective Calculations
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);

        render3D = new Render3D(gl);

        world = new World("src\\map.txt", render3D);

        canvas.addKeyListener(world.getPlayer().getController().getInputHandler());
        canvas.addMouseListener(world.getPlayer().getController().getInputHandler());
        canvas.addMouseMotionListener(world.getPlayer().getController().getInputHandler());
        canvas.addFocusListener(world.getPlayer().getController().getInputHandler());

        projectileModel = WavefrontObject.loadWavefrontObjectAsDisplayList(gl, "src\\resources\\models\\Dagger.obj");
        dummyModel = WavefrontObject.loadWavefrontObjectAsDisplayList(gl, "src\\resources\\models\\18489_Knight_V1_.obj");
        lampBaseModel = WavefrontObject.loadWavefrontObjectAsDisplayList(gl, "src\\resources\\models\\lampobj.obj");
        sphereModel = WavefrontObject.loadWavefrontObjectAsDisplayList(gl, "src\\resources\\models\\sphere.obj");
    }

    public void dispose(GLAutoDrawable glAutoDrawable) {}

    public void display(GLAutoDrawable glAutoDrawable) {
        if (world == null) { return; }
        long currentTime = System.nanoTime();
        long passedTime = currentTime - previousTime;
        previousTime = currentTime;
        unprocessedSeconds += passedTime / 1000000000.0;

        if (unprocessedSeconds > secondsPerTick) {
            unprocessedSeconds %= secondsPerTick;

            world.tick();
            ++tickCount;
            if (tickCount % 60 == 0) {
                System.out.println(framesRendered + " FPS");
                previousTime += 1000;
                framesRendered = 0;
            }
        }
        world.render(glAutoDrawable);
        ++framesRendered;
    }

    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        Render3D.glu.gluPerspective(50, Display.WINDOW_WIDTH / Display.WINDOW_HEIGHT, Player.HIT_RADIUS - 0.2,1000);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
}
