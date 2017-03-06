/**
 * Created by celineyelle on 2017-03-04.
 */
public class Solution {

    private long executionTime;
    private int unsolvedBoard [][];
    private int solvedBoard [][];

    public Solution(int [][] unsolved, int [][] solved, long time) {
        unsolvedBoard = unsolved;
        solvedBoard = solved;
        executionTime = time;
    }

    public void printAlgoSolution() {
        System.out.println();
        System.out.println();
        System.out.println("Unsolved Board");
        printBoard(unsolvedBoard);

        System.out.println("Solved Board");
        printBoard(solvedBoard);

        System.out.println("Execution Time :");
        System.out.println(executionTime);

        System.out.println();
        System.out.println("------------------------------------------------");
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
}
