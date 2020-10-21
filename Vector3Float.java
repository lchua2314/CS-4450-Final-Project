/***************************************************************
* file: Vector3Float.java
* author: L. Chua, A. Sun, M. Yang
* class: CS 4450 – Computer Graphics
*
* assignment: final program
* date last modified: 10/4/20
*
* purpose: This program creates and stores the camera's position
* in the 3D space.
*
****************************************************************/
package finalprogram;

public class Vector3Float {
    
    public float x, y, z;
    
    //method: Vector3Float
    //purpose: Constructor that initializes user's camera's position
    //in 3D space.
    public Vector3Float(int x, int y, int z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
}
