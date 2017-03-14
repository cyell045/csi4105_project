/**
 * Created by celineyelle on 2017-03-09.
 *
 * Based off of the solution provided by Anish Ratnawat
 * which was based off of Algorithm X by Donald Knuth
 */


class DancingCell
{
    DancingCell L, R, U, D;
    int i, j;
    int count;
    SudokuCell cell = null;

    DancingCell(DancingCell tail, DancingCell header, int i, int j) {

        if(tail == null) {
            L=this;
            R=this;
        } else {
            L   = tail.L;
            R   = tail;
            L.R = this;
            R.L = this;
        }


        if(header == null) {
            U = this;
            D = this;
        }  else {
            U = header.U;
            D   = header;
            U.D = this;
            D.U = this;
        }

        this.i = i;
        this.j = j;
        count = 0;
    }


    void unlinkVerticalCell() {
        U.D = D;
        D.U = U;
    }

    void linkVerticalCell() {
        D.U = this;
        U.D = this;
    }

    void unlinkHorizontalCell() {
        R.L = L;
        L.R = R;
    }

    void linkHorizontalCell() {
        L.R = this;
        R.L = this;
    }
}
