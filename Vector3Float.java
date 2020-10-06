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

//method: Vector3Float
//purpose: Constructor that hold user's camera's position
//in 3D space.
public class Vector3Float {
    
    public float x, y, z;
    
    public Vector3Float(int x, int y, int z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
}
