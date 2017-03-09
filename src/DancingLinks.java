import java.util.Date;

/**
 * Created by celineyelle on 2017-03-04.
 */
public class DancingLinks {

    private int n;
    private int N;
    private int numberOfCell;
    private Sudoku problem;
    private int [][] board;
    private int [][] algoSolution;
    private int [][] solution;

    public DancingLinks (Sudoku sudoku, Solution sol) {
        this.problem = sudoku;
        this.n = sudoku.getN();
        this.N = sudoku.getNSquared();
        this.numberOfCell = sudoku.getNumberOfCell();
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
