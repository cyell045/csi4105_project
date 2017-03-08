/**
 * Created by celineyelle on 2017-03-04.
 */
public class Solution {

    private long executionTime;
    private int unsolvedBoard [][]; //puzzle
    private int solvedBoard [][]; //solution to algorithm
    private int solutionBoard [][]; //solution

    public Solution(int [][] unsolved, int [][] solved, int [][] solution, long time) {
        unsolvedBoard = unsolved;
        solvedBoard = solved;
        solutionBoard = solution;
        executionTime = time;
    }

    public Solution(int[][] solution) {
        solutionBoard = solution;
    }

    public void printAlgoSolution() {
        System.out.println();
        System.out.println();
        System.out.println("Unsolved Board");
        printBoard(unsolvedBoard);

        System.out.println("Solved Board");
        printBoard(solvedBoard);

        System.out.println("Solution Board");
        printBoard(solutionBoard);

        System.out.println("Execution Time : " + executionTime + "s");
        System.out.println();

}

    private void printBoard(int[][] board){
        System.out.println();
        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board.length; j++)
                System.out.print(board[i][j] + " ");
            System.out.println();
        }
        System.out.println();
    }

    public int [][] getUnsolvedBoard()
    {
        return unsolvedBoard;
    }

    public int [][] getSolvedBoard()
    {
        return solvedBoard;
    }

    public int [][] getSolutionBoard()
    {
        return solutionBoard;
    }
}
