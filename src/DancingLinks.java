import java.util.Date;

/**
 * Created by celineyelle on 2017-03-04.
 */
public class DancingLinks {

    private Sudoku problem;
    private int n;
    private int N;
    private int numberOfCell;


    public DancingLinks (Sudoku sudoku) {
        this.problem = sudoku;
        this.n = sudoku.getN();
        this.N = sudoku.getNSquared();
        this.numberOfCell = sudoku.getNumberOfCell();
        problem.printBoard(0);
    }


    public Solution solve () {
        long lStartTime = new Date().getTime();


        //algorithm magic!


        long lEndTime = new Date().getTime();
        long time = lEndTime - lStartTime;
        return new Solution(problem, time);
    }
}
