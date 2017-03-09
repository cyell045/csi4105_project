/**
 * Created by celineyelle on 2017-03-04.
 */
import java.util.Date;


public class StochasticSearch {

    private int n;
    private int N;
    private int numberOfCell;
    private Sudoku problem;


    public StochasticSearch (Sudoku sudoku) {
        this.problem = sudoku;
        this.n = sudoku.getN();
        this.N = sudoku.getNSquared();
        this.numberOfCell = sudoku.getNumberOfCell();
    }


    public Solution solve () {
        long lStartTime = new Date().getTime();




        //algorithm magic!


        long lEndTime = new Date().getTime();
        long time = lEndTime - lStartTime;
        return new Solution(problem, time);
    }
}
