/**
 * Created by ChloeDiTomassoMasse on 2017-02-08.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Run {
    public static void main(String[] args) throws IOException {

        int n;
        int puzzleNum;
        boolean wrongNumber;
        String puzzlePath;
        String solutionPath;
        Scanner sc = new Scanner(System.in);

        boolean again = true;

        while(again){
            // Pick the size of the puzzle nxn
            System.out.println("Pick a size for your Sudoku puzzle (between 2 and 5).");
            do {
                n = sc.nextInt();
                if (n >= 2 && n <= 5) {
                    wrongNumber = false;
                }
                else {
                    System.out.println("Please pick a number between 2 and 5.");
                    wrongNumber = true;
                }
            }
            while(wrongNumber);


            puzzlePath = "puzzles/" + n + "x" + n;
            int max = (int)Files.list(Paths.get(puzzlePath)).count();


            // Pick which puzzle
            System.out.println("Pick the sudoku puzzle you wish to verify (between 1 and " + max + ").");
            do {
                puzzleNum = sc.nextInt();
                if (puzzleNum >= 1 && puzzleNum <= max) {
                    wrongNumber = false;
                }
                else {
                    System.out.println("Please pick a number between 1 and " + max + ".");
                    wrongNumber = true;
                }
            }
            while(wrongNumber);

            puzzlePath = "puzzles/" + n + "x" + n + "/p" + puzzleNum + ".txt";
            solutionPath = "solutions/" + n + "x" + n + "/s" + puzzleNum + ".txt";

            System.out.println("Select a number:");
            System.out.println("1 - verify that the puzzle board is correctly formed");
            System.out.println("2 - verify Backtracking Algorithm");
            System.out.println("3 - verify Stochastic Search Algorithm");
            System.out.println("4 - verify Dancing Links Algorithm");
            System.out.println("5 - verify all Algorithms");

            int i = sc.nextInt();

            // Verify that a specific board is correctly formed
            if (i == 1)
            {

                if (Verification.verifyPuzzle(readBoard(puzzlePath, n*n), readBoard(solutionPath, n*n)))
                {
                    System.out.println("The puzzle p" + puzzleNum + " is correctly formed.");
                }
                else
                {
                    System.out.println("The puzzle is incorrectly formed.  Please verify that puzzle p" + puzzleNum + " and s" + puzzleNum + " are the correct.");
                }
            }

            // Verify Algorithms work

            else if (i >= 2){

                Sudoku sudoku = new Sudoku(n, puzzlePath, solutionPath);

                System.out.println("------------------------------------------------");
                System.out.println();

                if (i == 2 || i == 5){
                    System.out.println("BACKTRACKING");

                    Solution s1 = new Backtracking(sudoku).solve();
                    s1.printAllBoards();
                    if (Verification.verifySolution(s1))
                    {
                        System.out.println("The puzzle p" + puzzleNum + " was correctly solved.");
                    }
                    else
                    {
                        System.out.println("The puzzle p" + puzzleNum + " was not correctly solved.  Please verify your algorithm.");
                    }

                    System.out.println();
                    System.out.println("------------------------------------------------");
                    System.out.println();

                }

                if (i == 3 || i == 5) {
                    System.out.println("STOCHASTIC SEARCH");

                    Solution s2 = new StochasticSearch(sudoku).solve();
                    s2.printAllBoards();
                    if (Verification.verifySolution(s2)) {
                        System.out.println("The puzzle p" + puzzleNum + " was correctly solved.");
                    } else {
                        System.out.println("The puzzle p" + puzzleNum + " was not correctly solved.  Please verify your algorithm.");
                    }
                    System.out.println();
                    System.out.println("------------------------------------------------");
                    System.out.println();            }

                if (i == 4 || i == 5) {
                    System.out.println("DANCING LINKS");

                    Solution s3 = new DancingLinks(sudoku).solve();
                    s3.printAllBoards();
                    Verification.verifySolution(s3);
                    if (Verification.verifySolution(s3)) {
                        System.out.println("The puzzle p" + puzzleNum + " was correctly solved.");
                    } else {
                        System.out.println("The puzzle p" + puzzleNum + " was not correctly solved.  Please verify your algorithm.");
                    }
                    System.out.println();
                    System.out.println("------------------------------------------------");
                    System.out.println();            }

            }

            System.out.println("Do you want to continue? (0 for no / 1 for yes)");
            do {
                int answer = sc.nextInt();
                if (answer >= 0 && answer <= 1) {
                    wrongNumber = false;
                    again = (answer != 0);
                }
                else {
                    System.out.println("Please pick 0 for no or 1 for yes.");
                    wrongNumber = true;
                }
            }
            while(wrongNumber);
        }
    }


    public static int [][] readBoard(String filename, int nsquared)
    {
        int [][] board = new int[nsquared][nsquared];

        try
        {
            FileReader fr = new FileReader(filename);
            BufferedReader stdin = new BufferedReader(fr);

            String [] lineArray;
            String line = "";

            for(int i = 0; i < nsquared; i++)
            {
                line = stdin.readLine();
                lineArray = line.split(",");
                for(int j = 0; j < nsquared; j++){
                    board[i][j] = Integer.parseInt(lineArray[j]);
                }

            }
        }
        catch(java.io.IOException e)
        {
            System.out.println(e);
        }

        return board;
    }

}
