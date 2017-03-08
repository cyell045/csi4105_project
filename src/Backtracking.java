import java.util.Date;

/**
 * Created by celineyelle on 2017-03-04.
 */
public class Backtracking {

    private int n;
    private int nsquared;
    private int numberOfCells;
    private int [][] board;
    private int [][] algoSolution;
    private int [][] solution;

    public Backtracking (Sudoku sudoku, Solution sol) {
        this.n = sudoku.getN();
        this.nsquared = sudoku.getNSquared();
        this.numberOfCells = sudoku.getTotalNumberOfCells();
        this.board = sudoku.getCopyOfBoard();
        this.solution = sol.getSolutionBoard();
    }


    public Solution solve () {
        long lStartTime = new Date().getTime();

        int row_index = 0;
        int col_index = 0;
        int index = 0;
        boolean backtracking_needed = false;

        while(index>=0 && index<nsquared*nsquared) {
            row_index = index/nsquared;
            col_index = index%nsquared;

            System.out.println();
            System.out.println("index:"+index+"   ["+row_index+"]["+col_index+"]");

            if(isUnassigned(index)){
                board[row_index][col_index]+=1;

                if(board[row_index][col_index]>=nsquared){
                    //NO VALID VALUE EXISTS!
                }
                //We need to reset cell and backtrack
                System.out.println("["+row_index+"]["+col_index+"]: 0 => BACKTRACKING NEEDED");
                board[row_index][col_index]=0;
                backtracking_needed = true;
            }else{
                System.out.println("["+row_index+"]["+col_index+"]: "+board[row_index][col_index]);
                backtracking_needed = false;

                isCorrect(index, board[row_index][col_index]);
            }


            if(backtracking_needed){
                //If backtracking is needed we move backward by 1.
                index -= 1;

            }else{
                //If no backtracking is needed we move forward by 1.
                index += 1;
            }
        }


        //for now set the solution equal to board (remove this)
        algoSolution = board;

        long lEndTime = new Date().getTime();
        long time = lEndTime - lStartTime;
        return new Solution(board, algoSolution, solution, time);
    }



    private boolean isUnassigned(int index){
        return board[index/nsquared][index%nsquared]==0;
    }

    private boolean isCorrect(int index, int assigned_number){
        return false;
    }
}