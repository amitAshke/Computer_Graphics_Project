package Main;

import Player.Player;
import Renderables.WavefrontObject;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import static Player.Controller.InputHandler;

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

    static GLCanvas canvas;
    static Frame frame = new Frame();
    static Animator animator;
    private static LevelManager levelManager;
    private World world;
    private Render3D render3D;

    private int framesRendered = 0;
    private double unprocessedSeconds = 0;
    private long previousTime = System.nanoTime();


    public static void main(String[] args) {
        // create the cursor
        Toolkit t = Toolkit.getDefaultToolkit();
        Image i = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        MONITOR_WIDTH = (int)t.getScreenSize().getWidth();
        MONITOR_HEIGHT = (int)t.getScreenSize().getHeight();
        Cursor noCursor = t.createCustomCursor(i, new Point(0, 0), "none");

        canvas = createCanvas();
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
        animator = new Animator(canvas);
        animator.start();
        canvas.requestFocus();
    }

    public static GLCanvas createCanvas() {
        // create a profile, in this case OpenGL 2 or later
        GLProfile profile = GLProfile.get(GLProfile.GL2);

        // configure context
        GLCapabilities capabilities = new GLCapabilities(profile);

        // setup z-buffer
        capabilities.setDepthBits(16);

        // for anti-aliasing
        capabilities.setSampleBuffers(true);
        capabilities.setNumSamples(2);

        // initialize a GLDrawable of your choice
        GLCanvas canvas = new GLCanvas(capabilities);
        return canvas;
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

        levelManager = new LevelManager(render3D);

        world = levelManager.getLevel();

//        world = new World("src\\map1.txt", render3D);

        canvas.addKeyListener(InputHandler);
        canvas.addMouseListener(InputHandler);
        canvas.addMouseMotionListener(InputHandler);
        canvas.addFocusListener(InputHandler);

        projectileModel = WavefrontObject.loadWavefrontObjectAsDisplayList(gl, "src\\resources\\models\\Dagger.obj");
        dummyModel = WavefrontObject.loadWavefrontObjectAsDisplayList(gl, "src\\resources\\models\\18489_Knight_V1_.obj");
        lampBaseModel = WavefrontObject.loadWavefrontObjectAsDisplayList(gl, "src\\resources\\models\\lampobj.obj");
        sphereModel = WavefrontObject.loadWavefrontObjectAsDisplayList(gl, "src\\resources\\models\\sphere.obj");
    }

    public void dispose(GLAutoDrawable glAutoDrawable) {}

    public void display(GLAutoDrawable glAutoDrawable) {
        if (world == null) { return; }
        GL2 gl = glAutoDrawable.getGL().getGL2();
        GLUT glut = new GLUT();
        long currentTime = System.nanoTime(), passedTime = currentTime - previousTime;
        double secondsPerTick = 1 / 60.0;
        boolean isPaused = InputHandler.key.get(KeyEvent.VK_F1);

        previousTime = currentTime;
        unprocessedSeconds += passedTime / 1000000000.0;

        if (!isPaused) {
            if (unprocessedSeconds > secondsPerTick) {
                unprocessedSeconds %= secondsPerTick;
                levelManager.player.tick();
                world.tick();
            }
        }
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();

        render3D.renderPlayer(gl, levelManager.player);
        world.render(gl);
        ++framesRendered;

        gl.glDisable( GL.GL_TEXTURE_2D );
        gl.glPopMatrix();


        if (isPaused) {
            renderInstructions(gl, glut);
        } else {
            gl.glColor3f( 1.0f, 1.0f, 1.0f );
            gl.glWindowPos2d( 20, WINDOW_HEIGHT - 60 );
            glut.glutBitmapString( GLUT.BITMAP_HELVETICA_12, (int) (1 / unprocessedSeconds) + " FPS");
            gl.glRasterPos2d( 0, 0 );
            gl.glColor3f( 1.0f, 1.0f, 1.0f );
        }

        if (world.dummies.isEmpty() || InputHandler.key.get(KeyEvent.VK_F2)) {
            System.out.println("New Level");
//            renderLoading(gl, glut);
            world = levelManager.getLevel();
        }
    }

    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        Render3D.glu.gluPerspective(50, Display.WINDOW_WIDTH / Display.WINDOW_HEIGHT, Player.HIT_RADIUS - 0.2,1000);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    private void renderInstructions(GL2 gl, GLUT glut) {
        gl.glDisable( GL.GL_TEXTURE_2D );
        gl.glColor3f( 1.0f, 1.0f, 1.0f );

        gl.glWindowPos2d( 20, WINDOW_HEIGHT - 100 );
        glut.glutBitmapString( GLUT.BITMAP_HELVETICA_12, "Objective: shoot the knight status.");
        gl.glWindowPos2d( 20, WINDOW_HEIGHT - 140 );
        glut.glutBitmapString( GLUT.BITMAP_HELVETICA_12, "Controls:");
        gl.glWindowPos2d( 20, WINDOW_HEIGHT - 160 );
        glut.glutBitmapString( GLUT.BITMAP_HELVETICA_12, "W - move forward");
        gl.glWindowPos2d( 20, WINDOW_HEIGHT - 180 );
        glut.glutBitmapString( GLUT.BITMAP_HELVETICA_12, "A - move left:");
        gl.glWindowPos2d( 20, WINDOW_HEIGHT - 200 );
        glut.glutBitmapString( GLUT.BITMAP_HELVETICA_12, "S - move back:");
        gl.glWindowPos2d( 20, WINDOW_HEIGHT - 220 );
        glut.glutBitmapString( GLUT.BITMAP_HELVETICA_12, "D - move right:");
        gl.glWindowPos2d( 20, WINDOW_HEIGHT - 240 );
        glut.glutBitmapString( GLUT.BITMAP_HELVETICA_12, "Left mouse button - shoot");
        gl.glWindowPos2d( 20, WINDOW_HEIGHT - 260 );
        glut.glutBitmapString( GLUT.BITMAP_HELVETICA_12, "right mouse button - activate special ability");
        gl.glWindowPos2d( 20, WINDOW_HEIGHT - 280 );
        glut.glutBitmapString( GLUT.BITMAP_HELVETICA_12, "right mouse button while special ability is active - shoot all projectiles");

        gl.glRasterPos2d( 0, 0 );
        gl.glColor3f( 1.0f, 1.0f, 1.0f );
    }

//    private void renderLoading(GL2 gl, GLUT glut) {
//        gl.glDisable( GL.GL_TEXTURE_2D );
//        gl.glColor3f( 1.0f, 1.0f, 1.0f );
//
//        gl.glWindowPos2d( WINDOW_WIDTH / 2 - 50, WINDOW_HEIGHT / 2 );
//        glut.glutBitmapString( GLUT.BITMAP_HELVETICA_12, "Loading...");
//
//        gl.glRasterPos2d( 0, 0 );
//        gl.glColor3f( 1.0f, 1.0f, 1.0f );
//    }

//    private void renderYouWin()
}
