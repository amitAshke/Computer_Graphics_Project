package Main;

import com.jogamp.opengl.util.Animator;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;

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

        // Really Nice Perspective Calculations
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

        render3D = new Render3D(gl);

        world = new World("src\\map.txt", render3D);

        canvas.addKeyListener(world.getPlayer().getController().getInputHandler());
        canvas.addMouseListener(world.getPlayer().getController().getInputHandler());
        canvas.addMouseMotionListener(world.getPlayer().getController().getInputHandler());
        canvas.addFocusListener(world.getPlayer().getController().getInputHandler());
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

    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {}
}
