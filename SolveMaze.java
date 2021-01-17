/*
* This program takes a maze in a txt file and solves it.
*
* @author  Jacob Bonner
* @version 1.0
* @since   2021-01-16
*/

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner;  // Import the Scanner class

/**
 * This program takes a maze in a txt file and solves it.
 */
public class SolveMaze {
  // A global 2D array that stores information on a boolean version of the maze
  public static boolean[][] boolList;

  /**
   * This function finds the start and end points of a maze.
   */
  static int[] findBreakPoints(char[][] startMaze) {
    // Setting up a list for coordinates as well as coordinate variables
    int[] breakCoords = new int[4];
    int startX = 0;
    int startY = 0;
    int endX = 0;
    int endY = 0;

    // Searching the array for a starting point
    for (int start1 = 0; start1 < startMaze.length; start1++) {
      for (int start2 = 0; start2 < startMaze[start1].length; start2++) {
        if (startMaze[start1][start2] == 'S') {
          startX = start1;
          startY = start2;
          break;
        }
      }
    }

    // Searching the array for a starting point
    for (int end1 = 0; end1 < startMaze.length; end1++) {
      for (int end2 = 0; end2 < startMaze[end1].length; end2++) {
        if (startMaze[end1][end2] == 'G') {
          endX = end1;
          endY = end2;
          break;
        }
      }
    }

    // Adding the coordinates to an array
    breakCoords[0] = startX;
    breakCoords[1] = startY;
    breakCoords[2] = endX;
    breakCoords[3] = endY;

    // Returning the array with the coordinates
    return breakCoords;
  }

  /**
   * This function prints a maze from a 2D array to the user.
   */
  static void printMaze(char[][] mazeArray, int length, int width) {
    // Printing the columns containing the maze information
    for (int counterX = 0; counterX < width; counterX++) {
      for (int counterY = 0; counterY < length; counterY++) {
        char printValue = (char) (mazeArray[counterX][counterY]);
        System.out.print(printValue + " ");
      }
      System.out.println("");
    }
  }

  /**
   * This function takes elements from a txt file and adds them to a 2D array
   * to create a maze.
   */
  static char[][] createMaze(File mazeFile, int length, int width) {
    // Creating array for the function to return containing the maze info
    char[][] newMaze = new char[width][length];

    try {
      // Taking the elements of the txt file and adding them to a 2D array
      Scanner mazeReader = new Scanner(mazeFile);
      for (int row = 0; mazeReader.hasNextLine() && row < width; row++) {
        char[] tempArray = mazeReader.nextLine().toCharArray();
        for (int col = 0; col < length && col < tempArray.length; col++) {
          newMaze[row][col] = tempArray[col];
        }
      }

      // Catching and showing the user what error occurred
    } catch (FileNotFoundException e) {
      System.out.println("ERROR: Unable to read file");
      e.printStackTrace();
    } catch (Exception e) {
      System.out.println("ERROR: Unable to create maze");
    }

    // Returning the newly created maze
    return newMaze;
  }

  /**
   * This function finds whether or not there is a solution for the maze.
   */
  static char[][] exploreMaze(char[][] viewMaze, int[] startStop, int rowSize,
                              int columnSize) {
    // Creating a boolean version of the maze
    boolList = new boolean[columnSize][rowSize];
    boolean[][] mapBoolean = new boolean[columnSize][rowSize];
    boolean[][] firstBoolList = new boolean[columnSize][rowSize];
    boolean[][] secondBoolList = new boolean[columnSize][rowSize];

    // Creating a boolean equivalent to the maze
    for (int mapRow = 0; mapRow < viewMaze.length; mapRow++) {
      for (int mapColumn = 0; mapColumn < viewMaze[0].length; mapColumn++) {
        if (viewMaze[mapRow][mapColumn] == '#') {
          mapBoolean[mapRow][mapColumn] = true;
        } else {
          mapBoolean[mapRow][mapColumn] = false;
        }
      }
    }

    // Filling the other arrays with boolean values
    for (int booleanRow = 0; booleanRow < viewMaze.length; booleanRow++) {
      for (int booleanCol = 0; booleanCol < viewMaze[booleanRow].length;
           booleanCol++) {
        boolList[booleanRow][booleanCol] = false;
        firstBoolList[booleanRow][booleanCol] = false;
        secondBoolList[booleanRow][booleanCol] = false;
      }
    }

    // Extracting important coordinates from the passed in array
    int beginX = startStop[0];
    int beginY = startStop[1];
    int stopX = startStop[2];
    int stopY = startStop[3];

    // Finding a solution for the maze
    boolean solution = travelMaze(mapBoolean, firstBoolList, secondBoolList, 
                                  beginX, beginY, stopX, stopY);

    // Telling the user if a solution was found or not
    if (solution = true) {
      System.out.println("Solution Found");
    } else {
      System.out.println("No Solution Found");
    }

    // Initializing a character array containing the
    char[][] solvedMaze = new char[columnSize][rowSize];

    // Converting the boolean version of the maze back to normal
    for (int rows = 0; rows < columnSize; rows++) {
      for (int cols = 0; cols < rowSize; cols++) {
        if (boolList[rows][cols] == true) {
          solvedMaze[rows][cols] = '+';
        } else if (boolList[rows][cols] == false && viewMaze[rows][cols] == '.') {
          solvedMaze[rows][cols] = '.';
        } else {
          solvedMaze[rows][cols] = '#';
        }
      }
    }

    // Redefining the start and end coordinates
    solvedMaze[beginX][beginY] = 'S';
    solvedMaze[stopX][stopY] = 'G';

    // Returning the newly solved maze
    return solvedMaze;
  }

  /**
   * This function uses recursion to find a solution to a boolean version of the
   * maze.
   */
  static boolean travelMaze(boolean[][] boolMaze, boolean[][] pathTraveled, 
                            boolean[][] finishedMaze, int coordX, int coordY, 
                            int goalX, int goalY) {

    // Checking if the user has reached the end of the maze
    if (coordX == goalX && coordY == goalY) {
      return true;
    }

    // Checking if the program is on a wall or has already visited the space
    if (boolMaze[coordX][coordY] || pathTraveled[coordX][coordY]) {
      return false;
    }

    // Marking the spot the program is on as been traveled through before
    pathTraveled[coordX][coordY] = true;

    // Checking if the program is on the left edge of the maze
    if (coordX != 0) {
      if (travelMaze(boolMaze, pathTraveled, finishedMaze, coordX - 1, 
                     coordY, goalX, goalY)) {
        boolList[coordX][coordY] = true;
        return true;
      }
    }

    // Checking if the program is on the right edge of the maze
    if (coordX != boolMaze.length - 1) {
      if (travelMaze(boolMaze, pathTraveled, finishedMaze, coordX + 1, 
                     coordY, goalX, goalY)) {
        boolList[coordX][coordY] = true;
        return true;
      }
    }

    // Checking if the program is on the top edge of the maze
    if (coordY != 0) {
      if (travelMaze(boolMaze, pathTraveled, finishedMaze, coordX, 
                     coordY - 1, goalX, goalY)) {
        boolList[coordX][coordY] = true;
        return true;
      }
    }

    // Checking if the program is on the bottom edge of the maze
    if (coordY != boolMaze[0].length - 1) {
      if (travelMaze(boolMaze, pathTraveled, finishedMaze, coordX, 
                     coordY + 1, goalX, goalY)) {
        boolList[coordX][coordY] = true;
        return true;
      }
    }
    
    // Returning false should none of the above conditions be met
    return false;
  }

  /**
   * This function takes three arrays containing mazes and then shows their
   * path from the start to the end of the maze to the user.
   */
  public static void main(String[] args) {
    // Creating the first maze
    File firstMazeFile = new File("Maze1.txt");
    char[][] firstMaze = createMaze(firstMazeFile, 6, 6);

    // Printing the first maze at its starting point
    System.out.println("Maze 1:");
    printMaze(firstMaze, 6, 6);
    System.out.println("");

    // Finding the start and stop points of the first maze
    int[] firstBreakPoints = findBreakPoints(firstMaze);

    // Finding a solution to the first maze and printing it out
    char[][] firstSolved = exploreMaze(firstMaze, firstBreakPoints, 6, 6);
    printMaze(firstSolved, 6, 6);
    System.out.println("");
    System.out.println("");

    // Creating the second maze
    File secondMazeFile = new File("Maze2.txt");
    char[][] secondMaze = createMaze(secondMazeFile, 6, 12);

    // Printing the second maze at its starting point
    System.out.println("Maze 2:");
    printMaze(secondMaze, 6, 12);
    System.out.println("");

    // Finding the start and stop points of the second maze
    int[] secondBreakPoints = findBreakPoints(secondMaze);

    // Finding a solution to the second maze and printing it out
    char[][] secondSolved = exploreMaze(secondMaze, secondBreakPoints, 6, 12);
    printMaze(secondSolved, 6, 12);
    System.out.println("");
    System.out.println("");

    // Creating the third maze
    File thirdMazeFile = new File("Maze3.txt");
    char[][] thirdMaze = createMaze(thirdMazeFile, 19, 9);

    // Printing the third maze at its starting point
    System.out.println("Maze 3:");
    printMaze(thirdMaze, 19, 9);
    System.out.println("");

    // Finding the start and stop points of the third maze
    int[] thirdBreakPoints = findBreakPoints(thirdMaze);

    // Finding a solution to the third maze and printing it out
    char[][] thirdSolved = exploreMaze(thirdMaze, thirdBreakPoints, 19, 9);
    printMaze(thirdSolved, 19, 9);
  }
}
