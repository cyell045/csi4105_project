/**
 * Created by ChloeDiTomassoMasse on 2017-02-08.
 */
import java.io.*;


public class Sudoku
{

    static Set empty;
    static Set fullSet;

    static int [][] initial;
    static int [][] current;

    static int n;
    static int N;

    static int numberOfCell;

    public Sudoku (int n, String path) {
        this.n = n;
        this.N = n*n;
        this.numberOfCell = N*N;

        initializeBoard();

        File file = new File(path);
        readGame(file.getAbsolutePath());
    }

    /**
     * Create board array and create and initialize sets for the
     * board squares.
     **/
    public void initializeBoard() {
        this.initial = new int[N][N];
        this.current = new int[N][N];
        Set empty = new Set(N);
        Set fullSet = new Set(N);

        for(int i = 1; i <= N; i++)
            fullSet.insert(i);
    }

    /**
     * Read the game from a file.
     * @param filename
     */
    public static void readGame(String filename) {
        try
        {
            FileReader fr = new FileReader(filename);
            BufferedReader stdin = new BufferedReader(fr);

            String line = "";
            String [] array;

            for(int i = 0; i < N; i++)
            {
                line = stdin.readLine();
                array = line.split(",");
                for(int j = 0; j < N; j++){
                    initial[i][j] = Integer.parseInt(array[j]);
                    current[i][j] = Integer.parseInt(array[j]);
                }

            }
        }
        catch(java.io.IOException e)
        {
            System.out.println(e);
        }
    }

    /**
     * Output the current board settings on the console.
     */
    public static void printBoard() {
        System.out.println();
        for(int i = 0; i<N; i++){
            System.out.print(" ");
            for(int j = 0; j<N; j++){
                System.out.print(current[i][j] +" ");
            }
            System.out.println();
        }
    }

    /**
     * Reset the current board to the initial settings, meaning the initial board
     */
    public void reset(){
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                current[i][j]=initial[i][j];
            }
        }
    }

    /**
     * Returns a copy of the current sudoku board
     **/
    public static int[][] getCopyOfCurrent(){
        int[][] newBoard = new int[N][N];

        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                newBoard[i][j]=current[i][j];
            }
        }
        return newBoard;
    }

    /**
     * Returns a copy of the initial sudoku board
     **/
    public static int[][] getCopyOfInitial(){
        int[][] newBoard = new int[N][N];

        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                newBoard[i][j]=initial[i][j];
            }
        }
        return newBoard;
    }

    /**
     * Getter for the variable n
     * @return the value of this.n
     */
    public int getN(){
        return this.n;
    }

    /**
     * Getter for the variable N
     * @return the value of this.N
     */
    public int getNSquared(){
        return this.N;
    }

    /**
     * Getter for the variable numberOfCell
     * @return the value of this.numberOfCell
     */
    public int getNumberOfCell(){
        return this.numberOfCell;
    }

    /**
     * Setter to assign a new number to a specific cell in the current board
     * @param index     the index value between 0 and numberOfCell
     * @param number    the new number assigned to this cell
     */
    public void setNumber(int index, int number){
        this.current[index/N][index%N]=number;
    }

    /**
     *
     * @param row_index the index of the row
     * @param col_index the index of the col
     * @param number    the new number to be assigned to the cell
     */
    public void setNumber(int row_index, int col_index, int number) {
        this.current[row_index][col_index]=number;
    }

    public int getNumber(int index){
        return current[index/N][index%N];
    }

    public int getNumber(int row_index, int col_index){
        return current[row_index][col_index];
    }

    public boolean isSolved(int index){
        return initial[index/N][index%N]!=0;
    }

    public boolean isSolved(int row, int col){
        return initial[row][col]!=0;
    }

    public boolean isAllowed(int index, int number){
        return isAllowed(index/N, index%N, number);
    }

    public boolean isAllowed(int row, int col, int number){
        //printBoard();
        //System.out.println("");

        boolean isAllowed = false;

        if(!isUsedInRow(row, col, number)){
            if(!isUsedInCol(row, col, number)){
                if(!isUsedInBox(row, row-row%n, col, col-col%n,number)){
                    isAllowed = true;
                }
            }
        }
        //System.out.println("");

        return isAllowed;
    }

    public boolean isUsedInRow(int row, int col, int number){
        for(int x=0; x<N; x++){
            if(current[row][x]==number && x!=col){
                //System.out.println("   >isUsedInRow: TRUE");
                return true;
            }
        }
        //System.out.println("   >isUsedInRow: FALSE");
        return false;
    }

    public boolean isUsedInCol(int row, int col, int number) {
        for (int x = 0; x < N; x++) {
            if (current[x][col] == number && row!=x) {
                //System.out.println("   >isUsedInCol: TRUE");
                //System.out.println("   >Current["+x+"]["+col+"]: " + current[x][col]);
                return true;
            }
        }
        //System.out.println("   >isUsedInCol: FALSE");
        return false;
    }

    public boolean isUsedInBox(int row, int boxStartRow, int col, int boxStartCol, int number){
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                if(current[i+boxStartRow][j+boxStartCol]==number && row!=(i+boxStartRow) && col!=(j+boxStartCol)){
                    //System.out.println("   >isUsedInBox: TRUE");
                    return true;
                }
            }
        }
        //System.out.println("   >isUsedInBox: FALSE");
        return false;
    }
}

