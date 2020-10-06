/***************************************************************
* file: FinalProgram.java
* author: L. Chua, A. Sun, M. Yang
* class: CS 4450 – Computer Graphics
*
* assignment: final program
* date last modified: 10/4/20
*
* purpose: This program creates a blockly game.
*
****************************************************************/
package finalprogram;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;

public class FinalProgram {
    
    private FPCameraController fp = new FPCameraController(0f,0f,0f);
    private DisplayMode displayMode;
    
    // method: start
    // purpose: Tries to create the window, initialize GL, and render the image.
    // If an error is found, the stack will catch and return it, terminating the program.    
    public void start() {
    
        try {
                createWindow();
                initGL();
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
        Display.setTitle("Hey Mom! I am using” + “OpenGL!!!");
        Display.create();
    }
    
    // method: initGL
    // purpose: Selects the background color, Alpha, 
    // loads camera and Identity matrix, sets up orthographic matrix,
    // Model view and some rendering hints
    private void initGL() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(100.0f, (float)displayMode.getWidth()/(float)
        displayMode.getHeight(), 0.1f, 300.0f);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
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
