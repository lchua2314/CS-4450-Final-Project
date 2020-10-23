/***************************************************************
* file: Chunk.java
* author: L. Chua, A. Sun, M. Yang
* class: CS 4450 – Computer Graphics
*
* assignment: final program
* date last modified: 10/25/20
*
* purpose: This program creates and renders random blocks for the game.
*
****************************************************************/
package finalprogram;

import java.nio.FloatBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Chunk {
    static final int CHUNK_SIZE = 30;
    static final int CUBE_LENGTH = 2;
    private Block[][][] Blocks;
    private int VBOVertexHandle;
    private int VBOColorHandle;
    private int StartX, StartY, StartZ;
    private Random r;
    private int VBOTextureHandle;
    private Texture texture;
    
    // method: render
    // purpose: Renders by binding buffers, textures, 
    // creating pointers and draw arrays.
    public void render(){
        glPushMatrix();
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBindTexture(GL_TEXTURE_2D, 1);
        glTexCoordPointer(2,GL_FLOAT,0,0L);
        glBindBuffer(GL_ARRAY_BUFFER,
            VBOVertexHandle);
        glVertexPointer(3, GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER,
            VBOColorHandle);
        glColorPointer(3,GL_FLOAT, 0, 0L);
        glDrawArrays(GL_QUADS, 0,
            CHUNK_SIZE *CHUNK_SIZE*
            CHUNK_SIZE * 24);
        glPopMatrix();
    }

    // method: rebuildMesh
    // purpose: Inputs three floats: startX, startY and startZ,
    // creates float buffers, creates textures for cubes, and binds buffers.
    public void rebuildMesh(
        float startX, float startY, float startZ) {
        VBOTextureHandle = glGenBuffers(); // placed among the other VBOs
        //the following among our other Float Buffers before our for loops
        FloatBuffer VertexTextureData =
            BufferUtils.createFloatBuffer((CHUNK_SIZE*
                CHUNK_SIZE *CHUNK_SIZE)* 6 * 12);
                VBOColorHandle = glGenBuffers();
                VBOVertexHandle = glGenBuffers();
        FloatBuffer VertexPositionData =
            BufferUtils.createFloatBuffer(
            (CHUNK_SIZE * CHUNK_SIZE *
            CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexColorData =
            BufferUtils.createFloatBuffer(
            (CHUNK_SIZE* CHUNK_SIZE *
            CHUNK_SIZE) * 6 * 12);
        for (float x = 0; x < CHUNK_SIZE; x += 1) {
            for (float z = 0; z < CHUNK_SIZE; z += 1) {
                for(float y = 0; y < CHUNK_SIZE; y++){
                    VertexTextureData.put(createTexCube((float) 0, (float)
                        0,Blocks[(int)(x)][(int) (y)][(int) (z)]));
                    VertexPositionData.put(
                        createCube((float) (startX + x
                        * CUBE_LENGTH),
                        (float)(y*CUBE_LENGTH+
                        (int)(CHUNK_SIZE*.8)),
                        (float) (startZ + z *
                        CUBE_LENGTH)));
                         VertexColorData.put(
                            createCubeVertexCol(
                            getCubeColor(
                            Blocks[(int) x]
                            [(int) y]
                            [(int) z])));
                }
            }
        }
        VertexTextureData.flip();
        VertexColorData.flip();
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexTextureData,
            GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        VertexPositionData.flip();
        glBindBuffer(GL_ARRAY_BUFFER,
            VBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER,
            VertexPositionData,
            GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER,
            VBOColorHandle);
        glBufferData(GL_ARRAY_BUFFER,
            VertexColorData,
            GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    // method: createCubeVertexCol
    // purpose: Takes in input for CubeColorArray,
    // initializes cubeColors on the heap, assigns CubeColorArray's data to
    // cubeColors and returns cubeColors as a float.
    private float[] createCubeVertexCol(float[] CubeColorArray) {
        float[] cubeColors = new float[CubeColorArray.length * 4 * 6];
        for (int i = 0; i < cubeColors.length; i++) {
            cubeColors[i] = CubeColorArray[i %
            CubeColorArray.length];
        }
        return cubeColors;
    }
    
    // method: createCube
    // purpose: Takes inputs of coordinates in floats: x, y, and z.
    // Returns a new float array with coordinates of where the
    // cube is located with offsets.
    public static float[] createCube(float x, float y, float z) {
        int offset = CUBE_LENGTH / 2;
        return new float[] {
            // TOP QUAD
            x + offset, y + offset, z,
            x - offset, y + offset, z,
            x - offset, y + offset, z - CUBE_LENGTH,
            x + offset, y + offset, z - CUBE_LENGTH,
            // BOTTOM QUAD
            x + offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z,
            x + offset, y - offset, z,
            // FRONT QUAD
            x + offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            // BACK QUAD
            x + offset, y - offset, z,
            x - offset, y - offset, z,
            x - offset, y + offset, z,
            x + offset, y + offset, z,
            // LEFT QUAD
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z,
            x - offset, y - offset, z,
            x - offset, y - offset, z - CUBE_LENGTH,
            // RIGHT QUAD
            x + offset, y + offset, z,
            x + offset, y + offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z };
    }
    
    // method: getCubeColor
    // purpose: Returns new float array 
    // with three 1's since the program 
    // now uses textures for the cubes.
    private float[] getCubeColor(Block block) {
        /*
        // Old as of lecture 14
        switch (block.GetID()) {
            case 1:
                return new float[] { 0, 1, 0 };
            case 2:
                return new float[] { 1, 0.5f, 0 };
            case 3:
                return new float[] { 0, 0f, 1f };
        }*/
        return new float[] { 1, 1, 1 };
    }

    // method: Chunk
    // purpose: Constructor that takes in three integer inputs: startX, startY, and startZ, 
    // tries to use "terrain.png" image file as textures for the cubes 
    // (if it is not found in project folder, catch the exception and prompt the user), 
    // use a random number to determine block types, creates the blocks within the chunk,
    // creates gen buffers, assigns StartX, StartY, and StartZ and lastly, calls rebuildMesh()
    // with integer inputs: startX, startY, and startZ.
    public Chunk(int startX, int startY, int startZ) {
        try{texture = TextureLoader.getTexture("PNG",
            ResourceLoader.getResourceAsStream("terrain.png"));
        }
        catch(Exception e)
        {
            System.out.print("ER-ROAR!");
        }
        r = new Random();
        Blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    if(r.nextFloat()>0.7f){
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Grass);
                    }else if(r.nextFloat()>0.4f){
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Dirt);
                    }else if(r.nextFloat()>0.2f){
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Water);
                    }else if(r.nextFloat()>0.2f){
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Stone);
                    }else if(r.nextFloat()>0.1f){
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Bedrock);
                    }else if(r.nextFloat()>0.0f){
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Sand);
                    }else{
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Default);
                    }
                }
            }
        }
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers(); //along with our other VBOs
        StartX = startX;
        StartY = startY;
        StartZ = startZ;
        rebuildMesh(startX, startY, startZ);
    }
    
    // method: createTexCube
    // purpose: Returns the texture based on the cube's type
    // with adding the offsets mulptipled by a number to
    // determine which coordnates are used to find the textures
    // to map from "terrain.png".
    public static float[] createTexCube(float x, float y, Block block) {
        float offset = (1024f/16)/1024f;
        switch (block.GetID()) {
            case 0: //grass
                return new float[] {
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset*3, y + offset*10, //right, bottom
                    x + offset*2, y + offset*10, //left, bottom
                    x + offset*2, y + offset*9, //left, top
                    x + offset*3, y + offset*9, //right, top
                    // TOP!
                    x + offset*3, y + offset*1,
                    x + offset*2, y + offset*1,
                    x + offset*2, y + offset*0,
                    x + offset*3, y + offset*0,
                    // FRONT QUAD
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1,
                    // BACK QUAD
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1,
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    // LEFT QUAD
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1,
                    // RIGHT QUAD
                    x + offset*3, y + offset*0,
                    x + offset*4, y + offset*0,
                    x + offset*4, y + offset*1,
                    x + offset*3, y + offset*1};
            case 1: //sand
                return new float[] {
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset*3, y + offset*2, //right, bottom
                    x + offset*2, y + offset*2, //left, bottom
                    x + offset*2, y + offset*1, //left, top
                    x + offset*3, y + offset*1, //right, top
                    // TOP!
                    x + offset*3, y + offset*2, 
                    x + offset*2, y + offset*2, 
                    x + offset*2, y + offset*1, 
                    x + offset*3, y + offset*1,
                    // FRONT QUAD
                    x + offset*3, y + offset*2, 
                    x + offset*2, y + offset*2, 
                    x + offset*2, y + offset*1, 
                    x + offset*3, y + offset*1,
                    // BACK QUAD
                    x + offset*3, y + offset*2, 
                    x + offset*2, y + offset*2, 
                    x + offset*2, y + offset*1, 
                    x + offset*3, y + offset*1,
                    // LEFT QUAD
                    x + offset*3, y + offset*2, 
                    x + offset*2, y + offset*2, 
                    x + offset*2, y + offset*1, 
                    x + offset*3, y + offset*1,
                    // RIGHT QUAD
                    x + offset*3, y + offset*2, 
                    x + offset*2, y + offset*2, 
                    x + offset*2, y + offset*1, 
                    x + offset*3, y + offset*1,};
            case 2: //water
                return new float[] {
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset*14, y + offset*13, //right, bottom
                    x + offset*13, y + offset*13, //left, bottom
                    x + offset*13, y + offset*12, //left, top
                    x + offset*14, y + offset*12, //right, top
                    // TOP!
                    x + offset*14, y + offset*13, 
                    x + offset*13, y + offset*13, 
                    x + offset*13, y + offset*12, 
                    x + offset*14, y + offset*12,
                    // FRONT QUAD
                    x + offset*14, y + offset*13, 
                    x + offset*13, y + offset*13, 
                    x + offset*13, y + offset*12, 
                    x + offset*14, y + offset*12,
                    // BACK QUAD
                    x + offset*14, y + offset*13, 
                    x + offset*13, y + offset*13, 
                    x + offset*13, y + offset*12, 
                    x + offset*14, y + offset*12,
                    // LEFT QUAD
                    x + offset*14, y + offset*13, 
                    x + offset*13, y + offset*13, 
                    x + offset*13, y + offset*12, 
                    x + offset*14, y + offset*12,
                    // RIGHT QUAD
                    x + offset*14, y + offset*13, 
                    x + offset*13, y + offset*13, 
                    x + offset*13, y + offset*12, 
                    x + offset*14, y + offset*13,};
            case 3: //dirt
                return new float[] {
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset*3, y + offset*1, //right, bottom
                    x + offset*2, y + offset*1, //left, bottom
                    x + offset*2, y + offset*0, //left, top
                    x + offset*3, y + offset*0, //right, top
                    // TOP!
                    x + offset*3, y + offset*1, 
                    x + offset*2, y + offset*1, 
                    x + offset*2, y + offset*0, 
                    x + offset*3, y + offset*0,
                    // FRONT QUAD
                    x + offset*3, y + offset*1, 
                    x + offset*2, y + offset*1, 
                    x + offset*2, y + offset*0, 
                    x + offset*3, y + offset*0,
                    // BACK QUAD
                    x + offset*3, y + offset*1, 
                    x + offset*2, y + offset*1, 
                    x + offset*2, y + offset*0, 
                    x + offset*3, y + offset*0,
                    // LEFT QUAD
                    x + offset*3, y + offset*1, 
                    x + offset*2, y + offset*1, 
                    x + offset*2, y + offset*0, 
                    x + offset*3, y + offset*0,
                    // RIGHT QUAD
                    x + offset*3, y + offset*1, 
                    x + offset*2, y + offset*1, 
                    x + offset*2, y + offset*0, 
                    x + offset*3, y + offset*0,};
            case 4: //stone
                return new float[] {
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset*2, y + offset*1, //right, bottom
                    x + offset*1, y + offset*1, //left, bottom
                    x + offset*1, y + offset*0, //left, top
                    x + offset*2, y + offset*0, //right, top
                    // TOP!
                    x + offset*2, y + offset*1, 
                    x + offset*1, y + offset*1, 
                    x + offset*1, y + offset*0, 
                    x + offset*2, y + offset*0,
                    // FRONT QUAD
                    x + offset*2, y + offset*1, 
                    x + offset*1, y + offset*1, 
                    x + offset*1, y + offset*0, 
                    x + offset*2, y + offset*0,
                    // BACK QUAD
                    x + offset*2, y + offset*1, 
                    x + offset*1, y + offset*1, 
                    x + offset*1, y + offset*0, 
                    x + offset*2, y + offset*0,
                    // LEFT QUAD
                    x + offset*2, y + offset*1, 
                    x + offset*1, y + offset*1, 
                    x + offset*1, y + offset*0, 
                    x + offset*2, y + offset*0,
                    // RIGHT QUAD
                    x + offset*2, y + offset*1, 
                    x + offset*1, y + offset*1, 
                    x + offset*1, y + offset*0, 
                    x + offset*2, y + offset*0,};
            case 5: //bedrock
                return new float[] {
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset*2, y + offset*2, //right, bottom
                    x + offset*1, y + offset*2, //left, bottom
                    x + offset*1, y + offset*1, //left, top
                    x + offset*2, y + offset*1, //right, top
                    // TOP!
                    x + offset*2, y + offset*2, 
                    x + offset*1, y + offset*2, 
                    x + offset*1, y + offset*1, 
                    x + offset*2, y + offset*1,
                    // FRONT QUAD
                    x + offset*2, y + offset*2, 
                    x + offset*1, y + offset*2, 
                    x + offset*1, y + offset*1, 
                    x + offset*2, y + offset*1,
                    // BACK QUAD
                    x + offset*2, y + offset*2, 
                    x + offset*1, y + offset*2, 
                    x + offset*1, y + offset*1, 
                    x + offset*2, y + offset*1,
                    // LEFT QUAD
                    x + offset*2, y + offset*2, 
                    x + offset*1, y + offset*2, 
                    x + offset*1, y + offset*1, 
                    x + offset*2, y + offset*1,
                    // RIGHT QUAD
                    x + offset*2, y + offset*2, 
                    x + offset*1, y + offset*2, 
                    x + offset*1, y + offset*1, 
                    x + offset*2, y + offset*1,};
            default: //purple box
                return new float[] {
                    // BOTTOM QUAD(DOWN=+Y)
                    x + offset*4, y + offset*14, //right, bottom
                    x + offset*3, y + offset*14, //left, bottom
                    x + offset*3, y + offset*13, //left, top
                    x + offset*4, y + offset*13, //right, top
                    // TOP!
                    x + offset*4, y + offset*14,
                    x + offset*3, y + offset*14,
                    x + offset*3, y + offset*13,
                    x + offset*4, y + offset*13,
                    // FRONT QUAD
                    x + offset*4, y + offset*14,
                    x + offset*3, y + offset*14,
                    x + offset*3, y + offset*13,
                    x + offset*4, y + offset*13,
                    // BACK QUAD
                    x + offset*4, y + offset*14,
                    x + offset*3, y + offset*14,
                    x + offset*3, y + offset*13,
                    x + offset*4, y + offset*13,
                    // LEFT QUAD
                    x + offset*4, y + offset*14,
                    x + offset*3, y + offset*14,
                    x + offset*3, y + offset*13,
                    x + offset*4, y + offset*13,
                    // RIGHT QUAD
                    x + offset*4, y + offset*14,
                    x + offset*3, y + offset*14,
                    x + offset*3, y + offset*13,
                    x + offset*4, y + offset*13};
        }
    }
}
