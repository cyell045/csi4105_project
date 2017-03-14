/**
 * Created by celineyelle on 2017-03-04.
 */
import java.math.BigDecimal;


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
        long lStartTime = System.nanoTime();




        //algorithm magic!


        long lEndTime = System.nanoTime();
        BigDecimal time = BigDecimal.valueOf(lEndTime - lStartTime).divide(BigDecimal.valueOf(1000000));
        return new Solution(problem, time);
    }
}
