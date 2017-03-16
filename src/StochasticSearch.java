/**
 * Created by celineyelle on 2017-03-04.
 */
import java.math.BigDecimal;
import java.util.*;


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

        Sudoku tempBoard = getOptimizedBoard(problem);


        long lEndTime = new Date().getTime();
        BigDecimal time = BigDecimal.valueOf(lEndTime - lStartTime).divide(BigDecimal.valueOf(1000000));
        return new Solution(problem, time);
    }

    /** This method takes the initial board and solves the 'obvious' cells to obtain a better partially filled board. **/
    public Sudoku getOptimizedBoard(Sudoku sudoku){

        List<Integer> numList = new ArrayList<>() ;
        for(int i = 0; i<N; i++){
            numList.add(i+1);
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                //See if the cell is empty
                if(sudoku.getNumber(i,j) == 0) {
                    List<Integer> tempNumList = numList;
                    int[] sBox = new int[]{0, 0};

                    //Find box the cell is in
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

                    //Find possible values
                    for (int k = 0; k < N; k++) {
                        if (sudoku.isUsedInRow(j, i, tempNumList.get(k)) ||
                                sudoku.isUsedInCol(i, j, tempNumList.get(k)) ||
                                sudoku.isUsedInBox(i, sBox[0], j, sBox[1], tempNumList.get(k))) {
                            tempNumList.remove(k);
                        }
                    }

                    //If only one value is possible, assign this value to the cell and restart the outer
                    // loop.
                    if (tempNumList.size() == 1) {
                        sudoku.setNumber(i, j, tempNumList.get(0));
                        i = 0;
                        break;
                    }
                }
            }
        }

        return sudoku;
    }

    public boolean isSolution(Sudoku sudoku) {
        return true;

    }
}
