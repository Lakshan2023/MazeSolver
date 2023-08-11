package MazeSolver;

public class MazePositionNode {
    int[] position; // This stores the column and row index of the array
    MazePositionNode next; // This is used to connect the current node with its next node

    public MazePositionNode(int[] position){
        this.position = position;
        this.next = null;
    }
}
