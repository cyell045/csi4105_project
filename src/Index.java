/**
 * Created by ChloeDiTomassoMasse on 2017-03-08.
 */
public class Index<R,C>{
    public final R row_index;
    public final C col_index;

    public Index(R row_index, C column_index){
        this.row_index=row_index;
        this.col_index=column_index;
    }

    public static <R, C> Index<R,C> createPair(R r, C c){
        return new Index<R,C>(r, c);
    }

    public R getRowIndex(){
        return row_index;
    }

    public C getColIndex(){
        return col_index;
    }
}
