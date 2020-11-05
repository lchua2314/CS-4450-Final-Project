/***************************************************************
* file: Block.java
* author: L. Chua, A. Sun, M. Yang
* class: CS 4450 – Computer Graphics
*
* assignment: final program
* date last modified: 10/25/20
*
* purpose: This program stores the block type, coordinates, and
* if it is active or not. Data can be changed and/or retrieved
* by other classes.
*
****************************************************************/
package finalprogram;

public class Block {
    private boolean IsActive;
    private BlockType Type;
    private float x,y,z;
    
    // method: BlockType
    // purpose: Special kind of "class" that holds
    // BlockType data and determines which integer is for
    // which texture. It also has a mutator method 
    // and an accessor method for changing the BlockType (with BlockID).
    public enum BlockType {
        BlockType_Grass(0),
        BlockType_Sand(1),
        BlockType_Water(2),
        BlockType_Dirt(3),
        BlockType_Stone(4),
        BlockType_Bedrock(5),
        BlockType_Lava(6),
        BlockType_Obsidian(7),
        BlockType_Netherrack(8),
        BlockType_Mycelium(9),
        BlockType_Diamondore(10),
        BlockType_Default(11);// Added Default block since Chunk.java needed it. Line 151

        private int BlockID;
        
        // method: BlockType
        // purpose: Constructor that initalizes
        // the BlockID with input value integer i.
        BlockType(int i) {
            BlockID = i;
        }

        // method: GetID
        // purpose: Accessor for BlockID to change the BlockType.
        // Returns integer BlockID.
        public int GetID(){
            return BlockID;
        }

        // method: SetID
        // purpose: Mutator for BlockID to retrieve the BlockType.
        // Inputs integer i to set as BlockID.
        public void SetID(int i){
            BlockID = i;
        }
    }
    
    // method: Block
    // purpose: Constructor for block
    // that sets the Type of the Block
    public Block(BlockType type){
        Type = type;
    }

    // method: setCoords
    // purpose: Mutator for the coordinates of blocks
    // using floats: x, y, and z.
    public void setCoords(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    // method: IsActive
    // purpose: Accessor for IsActive.
    // Returns boolean IsActive
    public boolean IsActive() {
        return IsActive;
    }
    
    // method: SetActive
    // purpose: Mutator for IsActive.
    // Uses input boolean "active" to
    // set IsActive.
    public void SetActive(boolean active){
        IsActive = active;
    }
    
    // method: GetID
    // purpose: Accessor for ID.
    // Calls Type's BlockID and returns
    // that integer.
    public int GetID(){
        return Type.GetID();
    }
}
