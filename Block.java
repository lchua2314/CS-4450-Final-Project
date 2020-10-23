/***************************************************************
* file: Block.java
* author: L. Chua, A. Sun, M. Yang
* class: CS 4450 – Computer Graphics
*
* assignment: final program
* date last modified: 10/25/20
*
* purpose: This program creates and stores the blocks for the game.
*
****************************************************************/
package finalprogram;

public class Block {
    private boolean isActive;
    private BlockType type;
    private float x,y,z;

    // method: BlockType
    // purpose: Special kind of "class"
    public enum BlockType {
        BlockType_Grass(0),
        BlockType_Sand(1),
        BlockType_Water(2),
        BlockType_Dirt(3),
        BlockType_Stone(4),
        BlockType_Bedrock(5),
        BlockType_Default(6); // Added Default block since Chunk.java needed it. Line 151

        private int blockID;

        // method: BlockType
        // purpose: Constructor
        BlockType(int i) {
            blockID = i;
        }

        // method: GetID
        // purpose: Getter for ID
        public int getID(){
            return blockID;
        }

        // method: SetID
        // purpose: Setter for ID
        public void setID(int i){
            blockID = i;
        }
    }
    
    // method: Block
    // purpose: Constructor for block
    public Block(BlockType type){
        this.type = type;
    }

    // method: setCoords
    // purpose: Sets the coorodinates for blocks
    public void setCoords(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    // method: IsActive
    // purpose: Getter for IsActive
    public boolean isActive() {
        return isActive;
    }
    
    // method: SetActive
    // purpose: Setter for IsActive
    public void setActive(boolean active){
        isActive = active;
    }
    
    // method: GetID
    // purpose: Getter for ID
    public int getID(){
        return type.getID();
    }
}
