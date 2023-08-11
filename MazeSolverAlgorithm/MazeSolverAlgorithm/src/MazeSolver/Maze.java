package MazeSolver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Maze {
    private int lineCount = 0; // Takes the line count of the maze
    private int columnCount = 0; // Takes the column count of the maze

    private char[][] MazeArray; // Takes the character of each visiting cells

    boolean implementStat = true; //This checks whether it is possible to implement the maze

    static MazePositionFinder mazePositionArray = new MazePositionFinder();

    public void MazeArrayReader() {
        //This Buffered reader is used to count the size of maze
        try (BufferedReader reader = new BufferedReader(new FileReader("MazeText.txt"))) {
            String readline;
            // This reads all the lines until it finds a line with spaces
            if ((readline = reader.readLine()) != null) {
                lineCount++;
                // This will take the column count by considering the length
                columnCount = readline.length();
            }
            // This is used to find out the line count of the maze
            while (reader.readLine() != null) {
                lineCount++;
            }

            reader.close();
            //  This initializes the size of the char Array that stores symbols of the maze
            MazeArray = new char[lineCount][columnCount];

            /* This buffered reader is used to read and insert the maze path characters into the maze array
                Eg: S, V, . , #, / */
            BufferedReader readerInput = new BufferedReader(new FileReader("MazeText.txt"));
            int tempRowCount = 0;

            while ((readline = readerInput.readLine()) != null) {
                for (int index = 0; index < readline.length(); index++) {
                    // This will insert the maze characters into the Maze Array
                    MazeArray[tempRowCount][index] = readline.charAt(index);
                }
                tempRowCount++;
            }

            readerInput.close();

        } catch (IOException e) {
            // print this message if it fails to read the file
            System.out.println("Can't read file !!!");
        }
    }

    // This method is used to find the starting index
    public void findStartIndex() {
        for (int j = 0; j < MazeArray.length; j++) {
            for (int i = 0; i < columnCount; i++) {
                if (MazeArray[j][i] == 'S') {
                    // This inserts the Starting position of the maze into the Stack
                    mazePositionArray.pushMazePosition(new int[]{j, i});
                    return;
                }
            }
        }
    }

    // This method is used to find the path of the maze
    public void pathFinder() {
        MazeArrayReader();
        findStartIndex();

        int RowCount;
        int ColCount;

        // This initializes the column priority direction arrays of the maze
        int[] ColDirectionArray = {-1, +1, 0, 0};

        // This initializes the row priority direction arrays of the maze
        int[] RowDirectionArray = {0, 0, -1, +1};

        while (true) {
            try {
                // This checks whether the maze stack is empty
                if (mazePositionArray.checkIsEmpty()) {
                    // If it is empty, it will direct to the starting position
                    findStartIndex();
                }

                // This takes the current row and column of the maze stack
                RowCount = mazePositionArray.MazeCurrentPosition()[0];
                ColCount = mazePositionArray.MazeCurrentPosition()[1];

                // If the current character equals to E, it will break this loop
                if (MazeArray[RowCount][ColCount] == 'E') {
                    break;
                }

                // This is used to check whether there are blocked paths from the current position
                boolean WhiteSpaceFoundStatus = false;

                // This rest of the code will apply only if the current position character is S or V
                if (MazeArray[RowCount][ColCount] == 'S' || MazeArray[RowCount][ColCount] == 'V') {
                    for (int index = 0; index < ColDirectionArray.length; index++) {
                        int tempRow = RowCount + RowDirectionArray[index];
                        int tempCol = ColCount + ColDirectionArray[index];

                        /* This checks whether the next cell is not a wall, blocked path cell,
                        already visited cell, and a starting cell */
                        if ((tempRow < lineCount) && (tempCol < columnCount) &&
                                (MazeArray[tempRow][tempCol] != '#') && (MazeArray[tempRow][tempCol] != 'V') &&
                                (MazeArray[tempRow][tempCol] != '/') && (MazeArray[tempRow][tempCol] != 'S')) {
                            mazePositionArray.pushMazePosition(new int[]{tempRow, tempCol});

                            // If that cell is not the starting cell or ending cell, marking it as visited
                            if (MazeArray[tempRow][tempCol] != 'S' && MazeArray[tempRow][tempCol] != 'E') {
                                MazeArray[tempRow][tempCol] = 'V';
                            }

                            // If the path is blocked, priority direction will be changed
                            if (MazeArray[tempRow][tempCol] == 'S') {
                                MazeArray[RowCount][ColCount] = '/';
                                mazePositionArray.popMazePosition();
                                ColDirectionArray = new int[]{0, 0, -1, +1};
                                RowDirectionArray = new int[]{-1, +1, 0, 0};
                            }

                            WhiteSpaceFoundStatus = true;
                            break;
                        }
                    }
                }

                // if it finds a blocked path mark it as blocked and pop that position from the maze array
                if (!WhiteSpaceFoundStatus) {
                    MazeArray[RowCount][ColCount] = '/';
                    mazePositionArray.popMazePosition();
                }

            } catch (ArrayIndexOutOfBoundsException AIB) {
                // If the maze cannot be implemented, display an error message
                System.out.println("Maze Cannot be implemented !!!");
                implementStat = false;
                break;
            }

        }

        printMaze();

    }

    // This method will print the maze with the visited cells
    public void printMaze() {
        if (implementStat) {
            for (char[] chars : MazeArray) {
                for (int y = 0; y < columnCount; y++) {
                    // If it is a blocked cell, print it as a '.'
                    if (chars[y] == '/') {
                        chars[y] = '.';
                    }
                    // If it is not, print its given character
                    System.out.print(chars[y] + " ");
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {

        Maze maze = new Maze();
        long startTime = System.nanoTime();

        maze.pathFinder();

        /*The below code is used to take the time to implement the maze and calculate
        the time complexity of the maze. */
        long endTime = System.nanoTime();
        long runtime = endTime - startTime;

        System.out.println("\n\nRuntime for the maze solver algorithm: " + runtime + " nanoseconds");
    }

}

/*
The main data structure that is used to implement this algorithm is Stack with LinkedLists.
The main purpose of using the stack is to store the positions of the visited cells in the given path of the maze.
Since the stack uses the First in Last Out algorithm concept, this data structure is the most efficient data structure
for backtracking, but here this algorithm uses several conditions to stop the recursion that usually occurs in mazes.
When implementing this algorithm, it has given a priority direction to visit the cells in the path. (Mainly two directions
are chosen from horizontal and vertical directions). If one of the selected priority directions meets a recursion,
the priority will be given to the opposite direction. The main stack is implemented using linked lists to reduce the
memory allocation to store positions of the cells.
 */
