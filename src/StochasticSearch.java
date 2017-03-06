/**
 * Created by celineyelle on 2017-03-04.
 */
import java.util.Date;


public class StochasticSearch {
    private int n;
    private int [][] board;
    private int [][] solution;

    public StochasticSearch (int n, Sudoku sudoku) {
        this.n = n;
        board = sudoku.getCopyOfBoard();
    }


    public Solution solve () {
        long lStartTime = new Date().getTime();





        //algorithm magic!




        //for now set the solution equal to board
        solution = board;

        long lEndTime = new Date().getTime();
        long time = lEndTime - lStartTime;
        return new Solution(board, solution, time);
    }


}
