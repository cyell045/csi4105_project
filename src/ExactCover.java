/*
 * Created by celineyelle on 2017-03-09.
 *
 * Based off of Algorithm X by Donald Knuth
 * Based off of the Algorithm X adaptation to Sudoku by Anish Ratnawat
 * Based off of the Algorithm X adaptation to Sudoku by Jonathan Chu
 */

import java.util.Stack;


public class ExactCover {
    DancingCell h;
    DancingCell colHeaders[];
    int n;
    int N;
    int numCol;
    int rowCount = 0;
    Stack<SudokuCell> possibleSolution = null;



    public ExactCover(int n) {
        h = new DancingCell(null,null,-1,-1);
        this.n = n;
        N = n*n;
        numCol = 4*N*N;
        colHeaders = new DancingCell[numCol];
        for(int i = 0; i < numCol; i++) {
            colHeaders[i] = new DancingCell(h,null, -1, i);
        }
        possibleSolution = new Stack<SudokuCell>();
    }


    public void transformSudokuToExactCover(SudokuCell puzzle[][]) {
        SudokuCell cell;
        for(int row = 0; row < (N); row++) {
            for(int col = 0; col < (N); col++) {
                if(puzzle[row][col].cellValue == 0) {
                    for(int k = 0; k < (N); k++){
                        if(puzzle[row][col].arr[k] == 0) {
                            cell = new SudokuCell(row, col,k+1);
                            addCell(row, col, k+1, rowCount, cell);
                            rowCount++;
                        }
                    }
                } else {
                    int value = puzzle[row][col].cellValue;
                    cell = new SudokuCell(row, col, value);
                    addCell(row, col, value, rowCount, cell);
                    rowCount++;
                }
            }
        }
    }



    public void addCell(int r, int c, int value, int rowCount, SudokuCell cell) {
        int cellValue = getCellHeaderNumber(r, c);
        int rowValue = getRowHeaderNumber(r) + value;
        int colValue = getColHeaderNumber(c) + value;
        int boxValue = getBoxHeaderNumber(r, c) + value;

        colHeaders[cellValue].count++;
        colHeaders[rowValue].count++;
        colHeaders[colValue].count++;
        colHeaders[boxValue].count++;

        DancingCell cellReference = null;
        DancingCell newCell;

        cellReference = new DancingCell(cellReference, colHeaders[cellValue], rowCount, cellValue);
        cellReference.cell = cell;
        newCell = new DancingCell(cellReference, colHeaders[rowValue], rowCount, rowValue);
        newCell.cell = cell;
        newCell = new DancingCell(cellReference, colHeaders[colValue], rowCount, colValue);
        newCell.cell = cell;
        newCell = new DancingCell(cellReference, colHeaders[boxValue], rowCount, boxValue);
        newCell.cell = cell;
    }


    public int getRowHeaderNumber(int row) {
        int rowHeaderNumber = (N*N) - 1 + (row*N);
        return rowHeaderNumber;
    }


    public int getColHeaderNumber(int col){
        int rowColumnHeader = (2*N*N) - 1 + (col*N);
        return rowColumnHeader;
    }


    public int getCellHeaderNumber(int row, int col){
        int CellHeaderNumber = (row*N) + col;
        return CellHeaderNumber;
    }


    public int getBoxHeaderNumber(int row, int col){
        int k = ((row/n)*n+(col/n))*(N);
        int boxHeaderNumber =  (3*N*N) - 1 + k;
        return boxHeaderNumber;
    }



    public boolean search() {
        if(h.R == h) {
            return true;
        }

        int min = Integer.MAX_VALUE;

        DancingCell minHeader = null;
        for(DancingCell x = h.R; x != h; x = x.R){
            if(x.count < min){
                minHeader = x;
                min = x.count;
            }
        }

        cover(minHeader.j);

        for (DancingCell y = minHeader.D; y != minHeader; y = y.D) {
            possibleSolution.push(y.cell);
            for (DancingCell z = y.R; z != y; z = z.R) {
                cover(z.j);
            }

            if (search()) {
                return true;
            }

            // backtrack
            for (DancingCell z = y.L; z != y; z = z.L) {
                uncover(z.j);
            }

            possibleSolution.pop();
        }
        uncover(minHeader.j);
        return false;

    }


    void cover(int j) {
        DancingCell col = colHeaders[j];
        col.unlinkHorizontalCell();

        for (DancingCell row = col.D; row != col; row = row.D) {
            for (DancingCell rightDancingCell = row.R; rightDancingCell != row; rightDancingCell = rightDancingCell.R) {
                rightDancingCell.unlinkVerticalCell();
                colHeaders[rightDancingCell.j].count--;
            }
        }
    }


    void uncover(int j) {
        DancingCell col = colHeaders[j];

        for (DancingCell row = col.U; row != col; row = row.U)
            for (DancingCell rightDancingCell = row.L; rightDancingCell != row; rightDancingCell = rightDancingCell.L) {
                colHeaders[rightDancingCell.j].count++;
                rightDancingCell.linkVerticalCell();
            }
        col.linkHorizontalCell();
    }
}
