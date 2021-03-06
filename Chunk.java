/***************************************************************
* file: Chunk.java
* author: L. Chua, A. Sun, M. Yang
* class: CS 4450 – Computer Graphics
*
* assignment: final program
* date last modified: 11/2/20
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
    private int[][] height;
    private int[][] humidity;
    private int[][][] level2;
    
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
                for(float y = 0; y < height[(int)x][(int)z]; y++){
                    VertexTextureData.put(createTexCube((float) 0, (float)
                        0,Blocks[(int)(x)][(int) (y)][(int) (z)]));
                    VertexPositionData.put(createCube((float) (startX + x * CUBE_LENGTH),
                        (float)(y*CUBE_LENGTH+(int)(CHUNK_SIZE*.9)),(float) (startZ + z *CUBE_LENGTH)));
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
    // Uses Random library and SimplexNoise objects to generate height, humidity 
    // (spawns either grass or sand), and level2 (stone or dirt spawning below topmost level)
    // This is used for identifying which textures to use for each blocks.
    // Additional feature 1: worldType - The Chunk can be reloaded to include the create a 
    // world with the indicated type (for example, a world with only
    // pink blocks).
    // Additional feature 3: Rare spawns (use F1 to reload world to find rare spawns)
    // 1. 1/6 chance to spawn in a tundra biome with snow-dirt blocks and ice
    // 2. Rare chance to spawn a pumpkin in the world. Conditions to spawn: 
    //    (a) Normal world in non-tundra.
    //    (b) A 2D coordinate will be randomly chosen to spawn the pumpkin on.
    //        However, if it is located on sand or water, it will not spawn.
    //    The program will notify user if it spawns.
    // NOTE: If you are looking for our 2nd feature, it is the day/night cycle located in
    // FPCameraController.java
    public Chunk(int startX, int startY, int startZ, int worldType) {
        try{texture = TextureLoader.getTexture("PNG",
            ResourceLoader.getResourceAsStream("terrain.png"));
        }
        catch(Exception e)
        {
            System.out.print("ER-ROAR!");
        }
        
        // Generate height in height, humidity, and level2
        Random rand = new Random();
        SimplexNoise noise = new SimplexNoise(100,0.15,rand.nextInt());
        SimplexNoise noise2 = new SimplexNoise(200,0.15,rand.nextInt());
        SimplexNoise noise3 = new SimplexNoise(200,0.15,rand.nextInt());
        
        height = new int[CHUNK_SIZE][CHUNK_SIZE];
        humidity = new int[CHUNK_SIZE][CHUNK_SIZE];
        level2 = new int[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
        for (float x = 0; x < CHUNK_SIZE; x += 1) {
            for (float z = 0; z < CHUNK_SIZE; z += 1) {

                int i = (int)(x*((CHUNK_SIZE)/30));
                int j = (int)(z*((CHUNK_SIZE)/30));
                int k = (int)(z*((CHUNK_SIZE)/30));

                height[(int)x][(int)z] = CHUNK_SIZE - Math.abs(startY + (int)(100*noise.getNoise(i,j,k)) * CUBE_LENGTH)/4;
                i = (int)(x*((CHUNK_SIZE)/10));
                j = (int)(z*((CHUNK_SIZE)/10));
                k = (int)(z*((CHUNK_SIZE)/10));
                humidity[(int)x][(int)z] = Math.abs(startY + (int)(100*noise2.getNoise(i,j,k)) * CUBE_LENGTH)/4;
                for (float y = 0; y < CHUNK_SIZE; y += 1) {
                    i = (int)(x*((CHUNK_SIZE)/15));
                    j = (int)(y*((CHUNK_SIZE)/15));
                    k = (int)(z*((CHUNK_SIZE)/15));
                    level2[(int)x][(int)z][(int)y] = Math.abs(startY + (int)(100*noise3.getNoise(i,j,k)) * CUBE_LENGTH)/4;
                    //System.out.println(level2[(int)x][(int)z][(int)y]);
                }
            }
        }
        
        int spawnSnow = rand.nextInt(6); // If spawnSnow == 5, spawn in tundra (1/6 chance)
        int[][] spawnPumpkin = new int[1][2]; // Spawn a pumpkin in 1/30 spots only if it is not humid or too deep.
        spawnPumpkin[0][0] = rand.nextInt(30);
        spawnPumpkin[0][1] = rand.nextInt(30);
        Blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    //F2 World
                    if (worldType == -1) { 
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Default);}
                    //F3 World
                    else if(worldType == -2 && y == height[(int)x][(int)z] - 1 && height[(int)x][(int)z] - 1 <= CHUNK_SIZE - 5){ //F3 World
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Lava);
                    }else if(worldType == -2 && y > 0 && y < height[(int)x][(int)z] - 1 && level2[(int)x][(int)z][(int)y] <= 3){
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Obsidian);
                    }else if(worldType == -2 && y == height[(int)x][(int)z] - 1 && height[(int)x][(int)z] - 1 > 25 && humidity[(int)x][(int)z] > 4){
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Mycelium);
                    }else if(worldType == -2 && y == height[(int)x][(int)z] - 1 && height[(int)x][(int)z] - 1 > 25 && humidity[(int)x][(int)z] <= 4){
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Netherrack);
                    }else if(worldType == -2 && y > 0 && y < height[(int)x][(int)z] - 1 && level2[(int)x][(int)z][(int)y] > 3){ 
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Diamondore);}
                    //F1 World
                    else if (spawnSnow == 5 && y == height[(int)x][(int)z] - 1 && height[(int)x][(int)z] - 1 > 25) {
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Snow);    
                    }else if(spawnSnow == 5 && y == height[(int)x][(int)z] - 1 && height[(int)x][(int)z] - 1 <= CHUNK_SIZE - 5){ // - 5  sand 2 stack
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Ice);
                    }else if(spawnPumpkin[0][0] == x && spawnPumpkin[0][1] == z && y == height[(int)x][(int)z] - 1 && height[(int)x][(int)z] - 1 > 25 && humidity[(int)x][(int)z] <= 4){
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Pumpkin);
                        spawnPumpkin[0][0] = -1;
                        System.out.println("Pumpkin spawned!");
                    }else if(y == height[(int)x][(int)z] - 1 && height[(int)x][(int)z] - 1 > 25 && humidity[(int)x][(int)z] <= 4){
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Grass);
                    }else if(y > 0 && y < height[(int)x][(int)z] - 1 && level2[(int)x][(int)z][(int)y] > 3){ // y >= 15 stone stack, 10 - 5 = water 5 stack
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Dirt);
                    }else if(y == height[(int)x][(int)z] - 1 && height[(int)x][(int)z] - 1 <= CHUNK_SIZE - 5){ // - 5  sand 2 stack
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Water );
                    }else if(y > 0 && y < height[(int)x][(int)z] - 1 && level2[(int)x][(int)z][(int)y] <= 3){
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Stone);
                    }else if(y == 0){
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Bedrock);
                    }else if(y == height[(int)x][(int)z] - 1 && height[(int)x][(int)z] - 1 > 25 && humidity[(int)x][(int)z] > 4){
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
                    // TOP
                    x + offset*3, y + offset*10, //right, bottom
                    x + offset*2, y + offset*10, //left, bottom
                    x + offset*2, y + offset*9, //left, top
                    x + offset*3, y + offset*9, //right, top
                    // BOTTOM!
                    x + offset*3, y + offset*1, // right, bottom
                    x + offset*2, y + offset*1, // left, bottom
                    x + offset*2, y + offset*0, // left, top
                    x + offset*3, y + offset*0, // right, top
                    // FRONT QUAD
                    x + offset*3, y + offset*0, // Top left
                    x + offset*4, y + offset*0, // Top right
                    x + offset*4, y + offset*1, // Bottom right
                    x + offset*3, y + offset*1, // Bottom left
                    // BACK QUAD
                    x + offset*4, y + offset*1, // Right bottom
                    x + offset*3, y + offset*1, // left bottom
                    x + offset*3, y + offset*0, // left top
                    x + offset*4, y + offset*0, // right top
                    // LEFT QUAD
                    x + offset*3, y + offset*0, // left top
                    x + offset*4, y + offset*0, // right top
                    x + offset*4, y + offset*1, // right bottom
                    x + offset*3, y + offset*1, // left bottom
                    // RIGHT QUAD
                    x + offset*3, y + offset*0, // left top
                    x + offset*4, y + offset*0, // right top
                    x + offset*4, y + offset*1, // right bottom
                    x + offset*3, y + offset*1}; // left bottom
            case 1: //sand
                return new float[] {
                    // TOP
                    x + offset*3, y + offset*2, //right, bottom
                    x + offset*2, y + offset*2, //left, bottom
                    x + offset*2, y + offset*1, //left, top
                    x + offset*3, y + offset*1, //right, top
                    // BOTTOM
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
                    // TOP
                    x + offset*14, y + offset*13, //right, bottom
                    x + offset*13, y + offset*13, //left, bottom
                    x + offset*13, y + offset*12, //left, top
                    x + offset*14, y + offset*12, //right, top
                    // BOTTOM
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
                    // TOP
                    x + offset*3, y + offset*1, //right, bottom
                    x + offset*2, y + offset*1, //left, bottom
                    x + offset*2, y + offset*0, //left, top
                    x + offset*3, y + offset*0, //right, top
                    // BOTTOM
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
                    // TOP
                    x + offset*2, y + offset*1, //right, bottom
                    x + offset*1, y + offset*1, //left, bottom
                    x + offset*1, y + offset*0, //left, top
                    x + offset*2, y + offset*0, //right, top
                    // BOTTOM
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
                    // TOP
                    x + offset*2, y + offset*2, //right, bottom
                    x + offset*1, y + offset*2, //left, bottom
                    x + offset*1, y + offset*1, //left, top
                    x + offset*2, y + offset*1, //right, top
                    // BOTTOM
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
            case 6: //lava
                return new float[] {
                    // TOP
                    x + offset*14, y + offset*15, //right, bottom
                    x + offset*13, y + offset*15, //left, bottom
                    x + offset*13, y + offset*14, //left, top
                    x + offset*14, y + offset*14, //right, top
                    // BOTTOM
                    x + offset*14, y + offset*15, 
                    x + offset*13, y + offset*15, 
                    x + offset*13, y + offset*14, 
                    x + offset*14, y + offset*14,
                    // FRONT QUAD
                    x + offset*14, y + offset*15, 
                    x + offset*13, y + offset*15, 
                    x + offset*13, y + offset*14, 
                    x + offset*14, y + offset*14,
                    // BACK QUAD
                    x + offset*14, y + offset*15, 
                    x + offset*13, y + offset*15, 
                    x + offset*13, y + offset*14, 
                    x + offset*14, y + offset*14,
                    // LEFT QUAD
                    x + offset*14, y + offset*15, 
                    x + offset*13, y + offset*15, 
                    x + offset*13, y + offset*14, 
                    x + offset*14, y + offset*14,
                    // RIGHT QUAD
                    x + offset*14, y + offset*15, 
                    x + offset*13, y + offset*15, 
                    x + offset*13, y + offset*14, 
                    x + offset*14, y + offset*14,};
            case 7: //obsidian
                return new float[] {
                    // TOP
                    x + offset*6, y + offset*3, //right, bottom
                    x + offset*5, y + offset*3, //left, bottom
                    x + offset*5, y + offset*2, //left, top
                    x + offset*6, y + offset*2, //right, top
                    // BOTTOM
                    x + offset*6, y + offset*3,
                    x + offset*5, y + offset*3, 
                    x + offset*5, y + offset*2, 
                    x + offset*6, y + offset*2,
                    // FRONT QUAD
                    x + offset*6, y + offset*3,
                    x + offset*5, y + offset*3, 
                    x + offset*5, y + offset*2, 
                    x + offset*6, y + offset*2,
                    // BACK QUAD
                    x + offset*6, y + offset*3,
                    x + offset*5, y + offset*3, 
                    x + offset*5, y + offset*2, 
                    x + offset*6, y + offset*2,
                    // LEFT QUAD
                    x + offset*6, y + offset*3,
                    x + offset*5, y + offset*3, 
                    x + offset*5, y + offset*2, 
                    x + offset*6, y + offset*2,
                    // RIGHT QUAD
                    x + offset*6, y + offset*3,
                    x + offset*5, y + offset*3, 
                    x + offset*5, y + offset*2, 
                    x + offset*6, y + offset*2,};
            case 8: //nether rack
                return new float[] {
                    // TOP
                    x + offset*8, y + offset*7, //right, bottom
                    x + offset*7, y + offset*7, //left, bottom
                    x + offset*7, y + offset*6, //left, top
                    x + offset*8, y + offset*6, //right, top
                    // BOTTOM
                    x + offset*8, y + offset*7,
                    x + offset*7, y + offset*7, 
                    x + offset*7, y + offset*6, 
                    x + offset*8, y + offset*6,
                    // FRONT QUAD
                    x + offset*8, y + offset*7,
                    x + offset*7, y + offset*7, 
                    x + offset*7, y + offset*6, 
                    x + offset*8, y + offset*6,
                    // BACK QUAD
                    x + offset*8, y + offset*7,
                    x + offset*7, y + offset*7, 
                    x + offset*7, y + offset*6, 
                    x + offset*8, y + offset*6,
                    // LEFT QUAD
                    x + offset*8, y + offset*7,
                    x + offset*7, y + offset*7, 
                    x + offset*7, y + offset*6, 
                    x + offset*8, y + offset*6,
                    // RIGHT QUAD
                    x + offset*8, y + offset*7,
                    x + offset*7, y + offset*7, 
                    x + offset*7, y + offset*6, 
                    x + offset*8, y + offset*6,};
            case 9: //mycelium
                return new float[] {
                    // TOP
                    x + offset*15, y + offset*5, //right, bottom
                    x + offset*14, y + offset*5, //left, bottom
                    x + offset*14, y + offset*4, //left, top
                    x + offset*15, y + offset*4, //right, top
                    // BOTTOM
                    x + offset*4, y + offset*1,
                    x + offset*2, y + offset*1, 
                    x + offset*2, y + offset*0, 
                    x + offset*3, y + offset*0,
                    // FRONT QUAD
                    x + offset*14, y + offset*4,
                    x + offset*13, y + offset*4, 
                    x + offset*13, y + offset*5, 
                    x + offset*14, y + offset*5,
                    // BACK QUAD
                    x + offset*14, y + offset*5,
                    x + offset*13, y + offset*5, 
                    x + offset*13, y + offset*4, 
                    x + offset*14, y + offset*4,
                    // LEFT QUAD
                    x + offset*14, y + offset*4,
                    x + offset*13, y + offset*4, 
                    x + offset*13, y + offset*5, 
                    x + offset*14, y + offset*5,
                    // RIGHT QUAD
                    x + offset*14, y + offset*4,
                    x + offset*13, y + offset*4, 
                    x + offset*13, y + offset*5, 
                    x + offset*14, y + offset*5,};
            case 10: //diamond ore
                return new float[] {
                    // TOP
                    x + offset*3, y + offset*4, //right, bottom
                    x + offset*2, y + offset*4, //left, bottom
                    x + offset*2, y + offset*3, //left, top
                    x + offset*3, y + offset*3, //right, top
                    // BOTTOM
                    x + offset*3, y + offset*4,
                    x + offset*2, y + offset*4, 
                    x + offset*2, y + offset*3, 
                    x + offset*3, y + offset*3,
                    // FRONT QUAD
                    x + offset*3, y + offset*3,
                    x + offset*2, y + offset*3, 
                    x + offset*2, y + offset*4, 
                    x + offset*3, y + offset*4,
                    // BACK QUAD
                    x + offset*3, y + offset*4,
                    x + offset*2, y + offset*4, 
                    x + offset*2, y + offset*3, 
                    x + offset*3, y + offset*3,
                    // LEFT QUAD
                    x + offset*3, y + offset*3,
                    x + offset*2, y + offset*3, 
                    x + offset*2, y + offset*4, 
                    x + offset*3, y + offset*4,
                    // RIGHT QUAD
                    x + offset*3, y + offset*3,
                    x + offset*2, y + offset*3, 
                    x + offset*2, y + offset*4, 
                    x + offset*3, y + offset*4,};
            case 11: //snow
                return new float[] {
                    // TOP
                    x + offset*3, y + offset*5, //right, bottom
                    x + offset*2, y + offset*5, //left, bottom
                    x + offset*2, y + offset*4, //left, top
                    x + offset*3, y + offset*4, //right, top
                    // BOTTOM!
                    x + offset*3, y + offset*1, // right, bottom
                    x + offset*2, y + offset*1, // left, bottom
                    x + offset*2, y + offset*0, // left, top
                    x + offset*3, y + offset*0, // right, top
                    // FRONT QUAD
                    x + offset*4, y + offset*4, // Top left
                    x + offset*5, y + offset*4, // Top right
                    x + offset*5, y + offset*5, // Bottom right
                    x + offset*4, y + offset*5, // Bottom left
                    // BACK QUAD
                    x + offset*5, y + offset*5, // Right bottom
                    x + offset*4, y + offset*5, // left bottom
                    x + offset*4, y + offset*4, // left top
                    x + offset*5, y + offset*4, // right top
                    // LEFT QUAD
                    x + offset*4, y + offset*4, // left top
                    x + offset*5, y + offset*4, // right top
                    x + offset*5, y + offset*5, // right bottom
                    x + offset*4, y + offset*5, // left bottom
                    // RIGHT QUAD
                    x + offset*4, y + offset*4, // left top
                    x + offset*5, y + offset*4, // right top
                    x + offset*5, y + offset*5, // right bottom
                    x + offset*4, y + offset*5}; // left bottom
            case 12: //ice
                return new float[] {
                    // TOP
                    x + offset*3, y + offset*4, // Top left
                    x + offset*4, y + offset*4, // Top right
                    x + offset*4, y + offset*5, // Bottom right
                    x + offset*3, y + offset*5, // Bottom left
                    // BOTTOM!
                    x + offset*3, y + offset*4, // Top left
                    x + offset*4, y + offset*4, // Top right
                    x + offset*4, y + offset*5, // Bottom right
                    x + offset*3, y + offset*5, // Bottom left
                    // FRONT QUAD
                    x + offset*3, y + offset*4, // Top left
                    x + offset*4, y + offset*4, // Top right
                    x + offset*4, y + offset*5, // Bottom right
                    x + offset*3, y + offset*5, // Bottom left
                    // BACK QUAD
                    x + offset*4, y + offset*5, // Right bottom
                    x + offset*3, y + offset*5, // left bottom
                    x + offset*3, y + offset*4, // left top
                    x + offset*4, y + offset*4, // right top
                    // LEFT QUAD
                    x + offset*3, y + offset*4, // left top
                    x + offset*4, y + offset*4, // right top
                    x + offset*4, y + offset*5, // right bottom
                    x + offset*3, y + offset*5, // left bottom
                    // RIGHT QUAD
                    x + offset*3, y + offset*4, // left top
                    x + offset*4, y + offset*4, // right top
                    x + offset*4, y + offset*5, // right bottom
                    x + offset*3, y + offset*5}; // left bottom
            case 13: //pumpkin
                return new float[] {
                    // TOP
                    x + offset*6, y + offset*6, // Top left
                    x + offset*7, y + offset*6, // Top right
                    x + offset*7, y + offset*7, // Bottom right
                    x + offset*6, y + offset*7, // Bottom left
                    // BOTTOM!
                    x + offset*6, y + offset*6, // Top left
                    x + offset*7, y + offset*6, // Top right
                    x + offset*7, y + offset*7, // Bottom right
                    x + offset*6, y + offset*7, // Bottom left
                    // FRONT QUAD
                    x + offset*7, y + offset*7, // Top left
                    x + offset*8, y + offset*7, // Top right
                    x + offset*8, y + offset*8, // Bottom right
                    x + offset*7, y + offset*8, // Bottom left
                    // BACK QUAD
                    x + offset*7, y + offset*8, // Right bottom
                    x + offset*6, y + offset*8, // left bottom
                    x + offset*6, y + offset*7, // left top
                    x + offset*7, y + offset*7, // right top
                    // LEFT QUAD
                    x + offset*6, y + offset*7, // left top
                    x + offset*7, y + offset*7, // right top
                    x + offset*7, y + offset*8, // right bottom
                    x + offset*6, y + offset*8, // left bottom
                    // RIGHT QUAD
                    x + offset*6, y + offset*7, // left top
                    x + offset*7, y + offset*7, // right top
                    x + offset*7, y + offset*8, // right bottom
                    x + offset*6, y + offset*8}; // left bottom
            default: //purple box
                return new float[] {
                    // TOP
                    x + offset*4, y + offset*14, //right, bottom
                    x + offset*3, y + offset*14, //left, bottom
                    x + offset*3, y + offset*13, //left, top
                    x + offset*4, y + offset*13, //right, top
                    // BOTTOM
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
