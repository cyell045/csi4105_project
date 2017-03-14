import java.math.BigDecimal;

/**
 * Created by celineyelle on 2017-03-04.
 */
public class Backtracking {

    private Sudoku problem;
    private int n;
    private int N;
    private int numberOfCell;


    public Backtracking (Sudoku sudoku) {
        this.problem = sudoku;
        this.n = sudoku.getN();
        this.N = sudoku.getNSquared();
        this.numberOfCell = sudoku.getNumberOfCell();
    }


    public Solution solve () {
        long lStartTime = System.nanoTime();

        int row;
        int col;

        int index = 0;

        boolean backtracking_needed = false;

        while (index >= 0 && index < numberOfCell) {
            row = index / N;
            col = index % N;

            //problem.printBoard();
            //System.out.println();

            //Only work with unassigned cell
            if (!problem.isSolved(index)) {
                //System.out.println();
                //System.out.println("INDEX: " + index);

                problem.setNumber(index, problem.getNumber(index) + 1);
                //System.out.println("BOARD[" + row + "][" + col + "]: "+problem.getNumber(index));

                //Find next valid value to try, if one exists
                while (!problem.isAllowed(index, problem.getNumber(index)) && problem.getNumber(index) <= N) {
                    problem.setNumber(index, problem.getNumber(index) + 1);
                }

                if(problem.getNumber(index) > N) {
                    //System.out.println(">> Number: " + problem.getNumber(index)+";");
                    //System.out.println("   Backtracking needed: TRUE.");

                    problem.setNumber(index, 0);
                    backtracking_needed = true;

                }else {
                    //System.out.println(">> Number: " + problem.getNumber(index) +";");
                    //System.out.println("   Backtracking needed: FALSE.");

                    backtracking_needed = false;
                }
            }

            if(backtracking_needed){
                index -=1;
                //System.out.println("   index - 1 = " + index);
            }else{
                index +=1;
                //System.out.println("   index + 1 = " + index);
            }
            //System.out.println("");
        }

        long lEndTime = System.nanoTime();
        BigDecimal time = BigDecimal.valueOf(lEndTime - lStartTime).divide(BigDecimal.valueOf(1000000));
        return new Solution(problem, time);
    }
}