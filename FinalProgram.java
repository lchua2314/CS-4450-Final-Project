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

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;

public class FinalProgram {

    // method: start
    // purpose: Tries to create the window, initialize GL, and render the image.
    // If an error is found, the stack will catch and return it, terminating the program.
    public void start() {
        try {
            createWindow();
            initGL();
            render();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // method: createWindow
    // purpose: Sets fullscreen to false, sets window to be 640x480 pixels 
    // and sets the title of the window to "Final Program"
    private void createWindow() throws Exception{
        Display.setFullscreen(false);
        Display.setDisplayMode(new DisplayMode(640, 480));
        Display.setTitle("Final Program");
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
        // glOrtho(0, 640, 0, 480, 1, -1); // First Quadrant only
        glOrtho(-320, 320, -240, 240, 1, -1); // Centered
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT,
        GL_NICEST);
    }
    
    // method: render
    // purpose: Draw on the screen.
    // Pressing Esc - Exits the window and program.
    private void render() {
        
        while (!Display.isCloseRequested()) {
            try{
                    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                    glLoadIdentity();
                    glPointSize(1);
                    glColor3f(0f, 1f, 0f); // Green
                    glTranslatef(2f, 2f, 4f); // Translate (2,2,4)
                    glTranslatef(2f, 2f, 2f); // Translate (2,2,2) Since cube is centered here.
                    glScalef(1f, 2f, 3f); // Scale (1,2,3)
                    glRotatef(90f, 0f, 0f, 1f); // Rotate 90 around z-axis
                    glRotatef(60f, 0f, 1f, 0f); // Rotate 60 around y-axis
                    glRotatef(30f, 1f, 0f, 0f); // Rotate 30 around x-axis
                    glTranslatef(-2f, -2f, -2f); // Translate (2,2,2) Since cube is centered here, need to translate back.
                    glBegin(GL_QUADS);
                    // Draw
                    glEnd();
                    Display.update();
                    
                    // Esacpe key - Exits the window and program.            
                    if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
                    {
                        System.exit(0);
                    }

                    Display.sync(60);
            }
            catch(Exception e){
            }
        }
        
        Display.destroy();
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
