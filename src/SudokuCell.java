/*
 * Created by celineyelle on 2017-03-09.
 */

public class SudokuCell {
    int cellValue;
    int arr[];
    int valueCount;
    int row;
    int col;

    SudokuCell(int row, int col, int val) {
        this.row = row;
        this.col = col;
        this.cellValue = val;
    }

    SudokuCell() {

    }
}



