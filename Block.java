/***************************************************************
* file: Block.java
* author: L. Chua, A. Sun, M. Yang
* class: CS 4450 – Computer Graphics
*
* assignment: final program
* date last modified: 10/20/20
*
* purpose: This program creates and stores the blocks for the game.
*
****************************************************************/
package finalprogram;

public class Block {
    private boolean IsActive;
    private BlockType Type;
    private float x,y,z;
    
    public enum BlockType {
        BlockType_Grass(0),
        BlockType_Sand(1),
        BlockType_Water(2),
        BlockType_Dirt(3),
        BlockType_Stone(4),
        BlockType_Bedrock(5),
        BlockType_Default(6); // Added Default block since Chunk.java needed it. Line 151
        
        private int BlockID;
        
        BlockType(int i) {
        BlockID = i;
        }
        
        //method: GetID
        //purpose: Getter for ID
        public int GetID(){
            return BlockID;
        }
        
        //method: SetID
        //purpose: Setter for ID
        public void SetID(int i){
            BlockID = i;
        }
    }

    //method: Block
    //purpose: Constructor for block
    public Block(BlockType type){
        Type= type;
    }
    
    //method: setCoords
    //purpose: Sets the coorodinates for blocks
    public void setCoords(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    //method: IsActive
    //purpose: Getter for IsActive    
    public boolean IsActive() {
        return IsActive;
    }

    //method: SetActive
    //purpose: Setter for IsActive    
    public void SetActive(boolean active){
        IsActive = active;
    }

    //method: GetID
    //purpose: Getter for ID    
    public int GetID(){
        return Type.GetID();
    }
}