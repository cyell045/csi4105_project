/**
 * Created by celineyelle on 2017-03-09.
 *
 * Based off of the solution provided by Anish Ratnawat
 * which was based off of Algorithm X by Donald Knuth
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



