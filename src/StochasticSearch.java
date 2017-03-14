/**
 * Created by celineyelle on 2017-03-04.
 */
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;


public class StochasticSearch {

    private int n;
    private int N;
    private int numberOfCell;
    private Sudoku problem;


    public StochasticSearch(Sudoku sudoku) {
        this.problem = sudoku;
        this.n = sudoku.getN();
        this.N = sudoku.getNSquared();
        this.numberOfCell = sudoku.getNumberOfCell();
    }

    public Solution solve() {


        long lStartTime = new Date().getTime();
        Sudoku tempBoard = problem;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int[] numArray = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
                int[] sBox = new int[]{0, 0};

                for (int k = n; k <= N; k += n) {
                    if (i < k && sBox[0] == 0) {
                        sBox[0] = k - n;
                    }
                    if (j < k && sBox[1] == 0) {
                        sBox[1] = k - n;
                    }
                    if (sBox[0] != 0 && sBox[1] != 0) {
                        break;
                    }
                }

                for (int k = 0; k < n; k++) {
                    if (problem.isUsedInRow(j, i, numArray[k]) ||
                            problem.isUsedInCol(i, j, numArray[k]) ||
                            problem.isUsedInBox(i, sBox[0], j, sBox[1], numArray[k])) {
                        numArray[k] = 0;
                        break;
                    }
                }
                int random = 0;
                while (random == 0) {
                    Random rand = new Random();
                    int k = rand.nextInt(11);
                    if (numArray[k] != 0) {
                        random = numArray[k];
                    }
                }

            }
        }


        long lEndTime = new Date().getTime();
        BigDecimal time = BigDecimal.valueOf(lEndTime - lStartTime).divide(BigDecimal.valueOf(1000000));
        return new Solution(problem, time);
    }

    public boolean isSolution(Sudoku sudoku) {
        return true;

    }
}
