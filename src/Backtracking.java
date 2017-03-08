import java.util.Date;

/**
 * Created by celineyelle on 2017-03-04.
 */
public class Backtracking {

    private int n;
    private int nsquared;
    private int numberOfCells;
    private int [][] board;
    private int [][] algoSolution;
    private int [][] solution;

    public Backtracking (Sudoku sudoku, Solution sol) {
        this.n = sudoku.getN();
        this.nsquared = sudoku.getNSquared();
        this.numberOfCells = sudoku.getTotalNumberOfCells();
        this.board = sudoku.getCopyOfBoard();
        this.solution = sol.getSolutionBoard();
    }


    public Solution solve () {
        long lStartTime = new Date().getTime();





        //algorithm magic!




        //for now set the solution equal to board (remove this)
        algoSolution = board;

        long lEndTime = new Date().getTime();
        long time = lEndTime - lStartTime;
        return new Solution(board, algoSolution, solution, time);
    }
}
