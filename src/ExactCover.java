/**
 * Created by celineyelle on 2017-03-09.
 *
 * Based off of the solution provided by Anish Ratnawat
 * which was based off of Algorithm X by Donald Knuth
 */

import java.util.Stack;

public class ExactCover {
    DancingCell h, ch[];
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
        ch = new DancingCell[numCol];
        for(int i = 0; i < numCol; i++) {
            ch[i] = new DancingCell(h,null, -1, i);
        }
        possibleSolution = new Stack<SudokuCell>();
    }


    public void transformToExactCover(SudokuCell puzzle[][]) {
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
        int cellV = getCellHeaderNo(r, c);
        int rowV = getRowHeaderNo(r) + value;
        int colV = getColHeaderNo(c) + value;
        int boxV = getBoxHeaderNo(r, c) + value;

        ch[cellV].count++;
        ch[rowV].count++;
        ch[colV].count++;
        ch[boxV].count++;

        DancingCell cellReference = null;
        DancingCell newCell;

        cellReference = new DancingCell(cellReference, ch[cellV], rowCount, cellV);
        cellReference.cell = cell;
        newCell = new DancingCell(cellReference, ch[rowV], rowCount, rowV);
        newCell.cell = cell;
        newCell = new DancingCell(cellReference, ch[colV], rowCount, colV);
        newCell.cell = cell;
        newCell = new DancingCell(cellReference, ch[boxV], rowCount, boxV);
        newCell.cell = cell;
    }


    public int getRowHeaderNo(int r) {
        return (((N*N)-1)+(r)*(N));
    }


    public int getColHeaderNo(int c){
        return ((2*(N*N)-1)+(c*N));
    }


    public int getCellHeaderNo(int r,int c){
        return (r*(N)+c);
    }


    public int getBoxHeaderNo(int r,int c){
        int q = ((r/n)*n+(c/n))*(N);
        return ((3*(N*N)-1)+q);
    }


    public boolean findSolution() {
        if(h.R == h) {
            return true;
        }

        int min = Integer.MAX_VALUE;

        DancingCell minHeader = null;
        for(DancingCell j = h.R; j != h; j = j.R){
            if(j.count < min){
                minHeader = j;
                min = j.count;
            }
        }

        findCover(minHeader.j);

        for (DancingCell r = minHeader.D; r != minHeader; r = r.D) {
            possibleSolution.push(r.cell);
            for (DancingCell l = r.R; l != r; l = l.R) {
                findCover(l.j);
            }

            if (findSolution()) {
                return true;
            }

            // backtrack
            for (DancingCell l = r.L; l != r; l = l.L) {
                uncoverAll(l.j);
            }

            possibleSolution.pop();
        }
        uncoverAll(minHeader.j);
        return false;

    }


    void findCover(int j) {
        DancingCell c = ch[j];
        c.unlinkHorizontalCell();

        for (DancingCell k = c.D; k != c; k = k.D) {
            for (DancingCell l = k.R; l != k; l = l.R) {
                l.unlinkVerticalCell();
                ch[l.j].count--;
            }
        }
    }


    void uncoverAll(int j) {
        DancingCell c = ch[j];

        for (DancingCell i = c.U; i != c; i = i.U)
            for (DancingCell l = i.L; l != i; l = l.L) {
                ch[l.j].count++;
                l.linkVerticalCell();
            }
        c.linkHorizontalCell();
    }
}
