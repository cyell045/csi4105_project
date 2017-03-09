/**
 * Created by celineyelle on 2017-03-04.
 */
public class Solution {

    private long executionTime;
    private Sudoku solution;

    public Solution(Sudoku sudoku, long time){
        this.solution = sudoku;
        executionTime = time;
    }

    public void printAllBoards() {
        System.out.println();
        System.out.println();
        System.out.println("UNSOLVED BOARD");
        solution.printBoard(0);

        System.out.println();
        System.out.println();
        System.out.println("SOLVED BOARD");
        solution.printBoard(1);

        System.out.println();
        System.out.println();
        System.out.println("SOLUTION BOARD");
        solution.printBoard(2);

        System.out.println();
        System.out.println("Execution Time : " + executionTime + "s");
        System.out.println();
    }

    public int [][] getUnsolvedBoard()
    {
        return solution.getCopyOfBoard(0);
    }

    public int [][] getSolvedBoard()
    {
        return solution.getCopyOfBoard(1);
    }

    public int [][] getSolutionBoard()
    {
        return solution.getCopyOfBoard(2);
    }
}
