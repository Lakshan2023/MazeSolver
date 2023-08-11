package MazeSolver;

// This is the main Stack that is used to store the position of the visited cells.
public class MazePositionFinder {

    MazePositionNode head;

    /*Push mazePosition method is used to insert the position
    values of the maze to the stack*/
    public void pushMazePosition(int[] position) {
        // firstly, it makes a new node to store the position
        MazePositionNode new_node = new MazePositionNode(position);

        /*Checks whether the head node is null,
        if it is null, it assigns the head node */
        if (head == null) {
            head = new_node;
        } else {
            /* if not it assign the head as a temporary node,
             * and then it assigns the new node as the head, after that
             *  it connects the temporary node value as its next value*/
            MazePositionNode tempNode = head;
            head = new_node;
            new_node.next = tempNode;
        }
    }

    /*This popMazePosition is used to delete the positions
     from the  Stack*/
    public int[] popMazePosition() {
        /* Here it checks whether the stack is empty,
        before deleting the node */
        if (checkIsEmpty()) {
            return new int[]{-1, -1};
        } else {
            /* If the stack is not empty, head will be assigned as the
             next node that is connected with the current node*/
            MazePositionNode poppedNode = head;
            head = head.next;

            return poppedNode.position;
        }
    }

    /* This method checks whether stack is empty*/
    public boolean checkIsEmpty() {
        return head == null;
    }

    /*This retrieves the current position of the Maze stack*/
    public int[] MazeCurrentPosition() {
        /*First it checks whether the head is null,
         if it is null that means the stack is empty*/
        if (head == null) {
            return new int[]{-1, -1};
        } else {
            return head.position;
        }
    }
}


