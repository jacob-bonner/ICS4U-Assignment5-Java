/*
* This program is a tic tac toe game against the computer.
*
* @author  Jacob Bonner
* @version 1.0
* @since   2021-01-13
*/

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.ArrayList;  // import the ArrayList class
import java.util.Scanner;  // Import the Scanner class

public class Maze {
  public static void main(String[] args) {
    try {
      File firstMaze = new File("Maze1.txt");
      Scanner mazeReader = new Scanner(firstMaze);
      while (mazeReader.hasNextLine()) {
        String mazeInfo = mazeReader.nextLine();
        System.out.println(mazeInfo);
      }
      mazeReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }
}
