/***************************************************************
* file: FPCameraController.java
* author: L. Chua, A. Sun, M. Yang
* class: CS 4450 – Computer Graphics
*
* assignment: final program
* date last modified: 11/2/20
*
* purpose: This program creates a cube and controls how the camera
* works.
*
****************************************************************/
package finalprogram;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.Sys;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

public class FPCameraController {
    //3d vector to store the camera's position in
    private Vector3f position = null;
    private Vector3f lPosition = null;
    //the rotation around the Y axis of the camera
    private float yaw = 0.0f;
    //the rotation around the X axis of the camera
    private float pitch = 0.0f;
    private Vector3Float me;
    private boolean isDayTime;
    private int clockCounter;
    
    // New Chunk Object
    Chunk chunkPosition = null;
    
    // method: FPCameraController
    // purpose: Constructor that hold user's camera's view
    // in 3D space.
    public FPCameraController(float x, float y, float z)
    {
        //instantiate position Vector3f to the x y z params.
        position = new Vector3f(x, y, z);
        lPosition = new Vector3f(x,y,z);
        lPosition.x = 40f;
        lPosition.y = 60f;
        lPosition.z = 100f;
        isDayTime = true;
        clockCounter = 0;
        // New Chuck object
        chunkPosition = new Chunk((int)x, (int)y, (int)z, 0);
    }
    
    // method: yaw
    // purpose: Increment the camera's current yaw rotation (y-axis)(left and right)
    public void yaw(float amount)
    {
        //increment the yaw by the amount param
        yaw += amount;
    }
    
    // method: pitch
    // purpose: Increment the camera's current pitch rotation (x-axis)(up and down)
    public void pitch(float amount)
    {
        //increment the pitch by the amount param
        pitch -= amount;
    }
    
    // method: walkForward
    // purpose: Moves the camera forward relative to its current rotation (yaw)
    public void walkForward(float distance)
    {
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        position.x -= xOffset;
        position.z += zOffset;
        /*
        lPosition.x+=xOffset;
        lPosition.z-=zOffset;
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x).put(
        lPosition.y).put(lPosition.z).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);*/
    }
    
    // method: walkBackwards
    // purpose: moves the camera backward relative to its current rotation (yaw)
    public void walkBackwards(float distance)
    {
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        position.x += xOffset;
        position.z -= zOffset;
        /*
        lPosition.x-=xOffset;
        lPosition.z+=zOffset;
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x).put(
        lPosition.y).put(lPosition.z).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);*/
    }
    
    // method: strafeLeft
    // purpose: Strafes the camera left relative to its current rotation (yaw)
    public void strafeLeft(float distance)
    {
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw-90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw-90));
        position.x -= xOffset;
        position.z += zOffset;/*
        lPosition.x+=xOffset;
        lPosition.z-=zOffset;
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x).put(
        lPosition.y).put(lPosition.z).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);*/
    }
    
    // method: strafeRight
    // purpose: Strafes the camera right relative to its current rotation (yaw)
    public void strafeRight(float distance)
    {
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw+90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw+90));
        position.x -= xOffset;
        position.z += zOffset;
        /*
        //System.out.println(position.x + " " + position.y + " " + position.z);
        System.out.println(xOffset + " " + zOffset);
        lPosition.x-=xOffset;
        lPosition.z+=zOffset;
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x).put(
        lPosition.y).put(lPosition.z).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
        */
        
    }

    // method: moveLight (This is our 2nd addtional feature)
    // purpose: Moves the light source up to transition into night time.
    // After the clockCounter hits 525, lower the light source
    // to transition into day time and reset the clockCounter.
    public void moveLight() {
        if (clockCounter == 525) {
            isDayTime = !isDayTime;
            clockCounter = 0;
        }
        
        if (isDayTime) {
            //lPosition.x-=1; // -3.1999633 83.19934 reaches max sunlight
            //lPosition.z+=1; // 85.29931 -5.2999616 reachs max darkness
            lPosition.y+=0.5;
            clockCounter++;
        } else if (!isDayTime) {
            //lPosition.x+=1; // -3.1999633 83.19934 reaches max sunlight
            //lPosition.z-=1; // 85.29931 -5.2999616 reachs max darkness
            lPosition.y-=0.5;   
            clockCounter++;
        }
        //System.out.println(lPosition.x + " " + lPosition.y + " " + lPosition.z);
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x).put(
        lPosition.y).put(lPosition.z).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }
    
    // method: moveUp
    // purpose: moves the camera up relative to its current rotation (yaw)
    public void moveUp(float distance)
    {
        position.y -= distance;
    }
    
    // method: moveDown
    // purpose: moves the camera down relative to its current rotation (yaw)
    public void moveDown(float distance)
    {
        position.y += distance;
    }
    
    // method: lookThrough
    // purpose: Translates and rotate the matrix so that it looks through the camera
    // this does basically what gluLookAt() does. Lighting also gets adjusted.
    public void lookThrough()
    {
        //rotate the pitch around the X axis
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //rotate the yaw around the Y axis
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //translate to the position vector's location
        glTranslatef(position.x, position.y, position.z);
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x).put(
        lPosition.y).put(lPosition.z).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }
    
    // method: gameLoop
    // purpose: Processes controls and graphics every frame
    // Press F1 - Reload the Chunk to create a new world!
    // Press F2 - Reload the Chunk to create a missing textured world!
    // Press F3 - Reload the Chunk to create a hellish world!
    public void gameLoop()
    {
        FPCameraController camera = new FPCameraController(-35, -90, -35);
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f; //length of frame
        float lastTime = 0.0f; // when the last frame was
        long time = 0;
        float mouseSensitivity = 0.09f;
        float movementSpeed = 2f; //.35f;
        //hide the mouse
        Mouse.setGrabbed(true);
        
        // keep looping till the display window is closed the ESC key is down
        while (!Display.isCloseRequested() &&
        !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
        {
            time = Sys.getTime();
            lastTime = time;
            camera.moveLight();
            //distance in mouse movement
            //from the last getDX() call.
            dx = Mouse.getDX();
            //distance in mouse movement
            //from the last getDY() call.
            dy = Mouse.getDY();
            //controll camera yaw from x movement fromt the mouse
            camera.yaw(dx * mouseSensitivity);
            //controll camera pitch from y movement fromt the mouse
            camera.pitch(dy * mouseSensitivity);
            //when passing in the distance to move
            //we times the movementSpeed with dt this is a time scale
            //so if its a slow frame u move more then a fast frame
            //so on a slow computer you move just as fast as on a fast computer
            if (Keyboard.isKeyDown(Keyboard.KEY_W))//move forward
            {
                camera.walkForward(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S))//move backwards
            {
                camera.walkBackwards(movementSpeed);
            }

            //strafe left 
            if (Keyboard.isKeyDown(Keyboard.KEY_A)){
                camera.strafeLeft(movementSpeed);
            }
            //strafe right 
            if (Keyboard.isKeyDown(Keyboard.KEY_D)){
                camera.strafeRight(movementSpeed);
            }
            //move up
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                camera.moveUp(movementSpeed);
            }
            //move down
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                camera.moveDown(movementSpeed);
            }
            
            // Reload the Chunk to a new default world.
            if (Keyboard.isKeyDown(Keyboard.KEY_F1)){
                chunkPosition = new Chunk(0,0,0,0);
            }
            
            // Reload the Chunk to a pink block world.
            if (Keyboard.isKeyDown(Keyboard.KEY_F2)){
                chunkPosition = new Chunk(0,0,0,-1);
            }
            
            // Reload the Chunk to a hellish world.
            if (Keyboard.isKeyDown(Keyboard.KEY_F3)){
                chunkPosition = new Chunk(0,0,0,-2);
            }
            
            
            //set the modelview matrix back to the identity
            glLoadIdentity();
            //look through the camera before you draw anything
            camera.lookThrough();
            
            //Enable depth buffer to prevent overlapping
            glEnable(GL_DEPTH_TEST); 
            
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            //you would draw your scene here.
            // render(); // No need for render() for lecture 13.
            chunkPosition.render();
            //draw the buffer to the screen
            Display.update();
            Display.sync(60);
        }
        Display.destroy();
    }
    
    /*
    // No need for this since lecture 13.
    // method: render
    // purpose: renders a cube 
    private void render() {
        try {
            glBegin(GL_QUADS);
            
            // Draw six sides of a cube with different colors.
            //Top
            glColor3f(0.0f,0.0f,1.0f);
            glVertex3f( 1.0f, 1.0f,-1.0f);
            glVertex3f(-1.0f, 1.0f,-1.0f);
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f( 1.0f, 1.0f, 1.0f);
            //Bottom
            glColor3f(0.0f,1.0f,1.0f);
            glVertex3f( 1.0f,-1.0f, 1.0f);
            glVertex3f(-1.0f,-1.0f, 1.0f);
            glVertex3f(-1.0f,-1.0f,-1.0f);
            glVertex3f( 1.0f,-1.0f,-1.0f);
            //Front
            glColor3f(1.0f,0.0f,1.0f);
            glVertex3f( 1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f,-1.0f, 1.0f);
            glVertex3f( 1.0f,-1.0f, 1.0f);
            //Back
            glColor3f(0.4f,0.0f,1.0f);
            glVertex3f( 1.0f,-1.0f,-1.0f);
            glVertex3f(-1.0f,-1.0f,-1.0f);
            glVertex3f(-1.0f, 1.0f,-1.0f);
            glVertex3f( 1.0f, 1.0f,-1.0f);
            //Left
            glColor3f(0.0f,0.5f,1.0f);
            glVertex3f(-1.0f, 1.0f,1.0f);
            glVertex3f(-1.0f, 1.0f,-1.0f);
            glVertex3f(-1.0f,-1.0f,-1.0f);
            glVertex3f(-1.0f,-1.0f, 1.0f);
            //Right
            glColor3f(0.7f,0.5f,1.0f);
            glVertex3f( 1.0f, 1.0f,-1.0f);
            glVertex3f( 1.0f, 1.0f, 1.0f);
            glVertex3f( 1.0f,-1.0f, 1.0f);
            glVertex3f( 1.0f,-1.0f,-1.0f);
            glEnd();
            
            
            // Draw outlines of each side of the cube.
            glBegin(GL_LINE_LOOP);
            //Top
            glColor3f(0.0f,0.0f,0.0f);
            glVertex3f( 1.0f, 1.0f,-1.0f);
            glVertex3f(-1.0f, 1.0f,-1.0f);
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f( 1.0f, 1.0f, 1.0f);
            glEnd();
            glBegin(GL_LINE_LOOP);
            //Bottom
            glVertex3f( 1.0f,-1.0f, 1.0f);
            glVertex3f(-1.0f,-1.0f, 1.0f);
            glVertex3f(-1.0f,-1.0f,-1.0f);
            glVertex3f( 1.0f,-1.0f,-1.0f);
            glEnd();
            glBegin(GL_LINE_LOOP);
            //Front
            glVertex3f( 1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f,-1.0f, 1.0f);
            glVertex3f( 1.0f,-1.0f, 1.0f);
            glEnd();
            glBegin(GL_LINE_LOOP);
            //Back
            glVertex3f( 1.0f,-1.0f,-1.0f);
            glVertex3f(-1.0f,-1.0f,-1.0f);
            glVertex3f(-1.0f, 1.0f,-1.0f);
            glVertex3f( 1.0f, 1.0f,-1.0f);
            glEnd();
            glBegin(GL_LINE_LOOP);
            //Left
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f, 1.0f,-1.0f);
            glVertex3f(-1.0f,-1.0f,-1.0f);
            glVertex3f(-1.0f,-1.0f, 1.0f);
            glEnd();
            glBegin(GL_LINE_LOOP);
            //Right
            glVertex3f( 1.0f, 1.0f,-1.0f);
            glVertex3f( 1.0f, 1.0f, 1.0f);
            glVertex3f( 1.0f,-1.0f, 1.0f);
            glVertex3f( 1.0f,-1.0f,-1.0f);
            glEnd();
        }catch(Exception e){
        }
    }
*/
}
