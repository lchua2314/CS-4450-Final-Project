/***************************************************************
* file: FinalProgram.java
* author: L. Chua, A. Sun, M. Yang
* class: CS 4450 – Computer Graphics
*
* assignment: final program
* date last modified: 10/25/20
*
* purpose: This program contains the main method to run the program.
* It also controls the initialization of the window, its width and 
* height in pixels and display.
*
****************************************************************/
package finalprogram;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

public class FinalProgram {
    
    private FPCameraController fp = null;
    private DisplayMode displayMode;
    private FloatBuffer lightPosition;
    private FloatBuffer whiteLight;
    
    // method: start
    // purpose: Tries to create the window, initialize GL, and render the image through FPCameraController.
    // If an error is found, the stack will catch and return it, terminating the program.    
    public void start() {
    
        try {
                createWindow();
                initGL();
                fp = new FPCameraController(0f,0f,0f);
                fp.gameLoop();//render();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    // method: createWindow
    // purpose: Sets fullscreen to false, sets window to be 640x480 pixels 
    // and sets the title of the window to "Final Program"    
    private void createWindow() throws Exception{
        Display.setFullscreen(false);
        DisplayMode d[] =
        Display.getAvailableDisplayModes();
        for (int i = 0; i < d.length; i++) {
            if (d[i].getWidth() == 640
            && d[i].getHeight() == 480
            && d[i].getBitsPerPixel() == 32) {
            displayMode = d[i];
            break;
            }
        }
        Display.setDisplayMode(displayMode);
        Display.setTitle("Final Program");
        Display.create();
    }
    
    // method: initGL
    // purpose: Selects the background color, Alpha, 
    // loads camera and Identity matrix, sets up orthographic matrix,
    // Model view and some rendering hints
    private void initGL() {
        glEnable(GL_TEXTURE_2D);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnable(GL_DEPTH_TEST);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(100.0f, (float)displayMode.getWidth()/(float)
        displayMode.getHeight(), 0.1f, 300.0f);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        initLightArrays();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition); //sets our light’s position
        glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);//sets our specular light
        glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);//sets our diffuse light
        glLight(GL_LIGHT0, GL_AMBIENT, whiteLight);//sets our ambient light
        glEnable(GL_LIGHTING);//enables our lighting
        glEnable(GL_LIGHT0);//enables light0
    }

    // method: initLightArrays
    // purpose: Initializes and places lightPosition and whiteLight
    // with the use of buffers in memory.
    private void initLightArrays() {
        lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(50.0f).put(0.0f).put(0.0f).put(1.0f).flip();
        whiteLight = BufferUtils.createFloatBuffer(4);
        whiteLight.put(1.0f).put(1.0f).put(1.0f).put(0.0f).flip();
    }
   
    // method: main
    // purpose: Creates an instance of the FinalProgram class. 
    // This is so we don't have to make all the classes static.
    // Then the start method is called.
    public static void main(String[] args) {
            FinalProgram fp1 = new FinalProgram();
            fp1.start();
    }
}
