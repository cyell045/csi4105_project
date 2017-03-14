import java.math.BigDecimal;

/**
 * Created by celineyelle on 2017-03-09.
 *
 * Based off of the solution provided by Anish Ratnawat
 * which was based off of Algorithm X by Donald Knuth
 */

public class DancingLinks {

    private Sudoku sudoku;
    private int n;
    private int N;
    private SudokuCell [][] puzzle;

    public DancingLinks (Sudoku sudoku) {
        this.sudoku = sudoku;
        this.n = sudoku.getN();
        this.N = sudoku.getNSquared();
    }


    public Solution solve () {
        long lStartTime = System.nanoTime();

        //algorithm magic!
        ExactCover ec = new ExactCover(n);
        puzzle = new SudokuCell[N][N];
        initializeSudoku();
        findPossibleElements();

        ec.transformToExactCover(puzzle);

        if(ec.findSolution()) {
            for (SudokuCell c : ec.possibleSolution) {
                puzzle[c.row][c.col].cellValue = c.cellValue;
            }
        }

        returnSolutionToSudokuBoard();

        long lEndTime = System.nanoTime();
        BigDecimal time = BigDecimal.valueOf(lEndTime - lStartTime).divide(BigDecimal.valueOf(1000000));
        return new Solution(sudoku, time);
    }


    public void initializeSudoku() {

        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                puzzle[i][j] = new SudokuCell();
                puzzle[i][j].cellValue = sudoku.initial[i][j];

                if(puzzle[i][j].cellValue == 0){
                    puzzle[i][j].arr = new int[N];
                    puzzle[i][j].valueCount = N;
                }
            }
        }
    }

    public void returnSolutionToSudokuBoard() {
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                sudoku.current[i][j] = puzzle[i][j].cellValue;
            }
        }
    }

    public void findPossibleElements(){

        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                if(puzzle[i][j].cellValue==0) {
                    //rows possibility
                    for(int k = 0; k < N; k++) {
                        if(puzzle[i][k].cellValue != 0) {
                            if(puzzle[i][j].arr[puzzle[i][k].cellValue-1] == 0) {
                                puzzle[i][j].arr[puzzle[i][k].cellValue-1] = 1;
                                puzzle[i][j].valueCount--;
                            }
                        }

                    }
                    //columns possibility
                    for(int k = 0; k < N; k++) {
                        if(puzzle[k][j].cellValue!=0){
                            if(puzzle[i][j].arr[puzzle[k][j].cellValue-1] == 0) {
                                puzzle[i][j].arr[puzzle[k][j].cellValue-1] = 1;
                                puzzle[i][j].valueCount--;
                            }
                        }
                    }
                    //squares possibility
                    int h = i/n;
                    int g = j/n;
                    for(int k = (h*n); k < ((h*n)+n); k++) {
                        for(int l = (g*n); l < ((g*n)+n); l++) {
                            if(puzzle[k][l].cellValue != 0) {
                                if(puzzle[i][j].arr[puzzle[k][l].cellValue-1] == 0) {
                                    puzzle[i][j].arr[puzzle[k][l].cellValue-1] = 1;
                                    puzzle[i][j].valueCount--;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}