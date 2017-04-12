/**
 * By Camille La Rose & Sara El
 * Based on the algorithm provided by Rhydian Lewis at
 * https://www.researchgate.net/publication/221411168_On_the_Combination_of_Constraint_Programming_and_Stochastic_Search_The_Sudoku_Case
 */
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.ArrayList;
import java.util.Random;


public class StochasticSearch {

    private int n;
    private int N;
    private int numberOfCell;
    private Sudoku problem;
    private Sudoku best_solution;
    private double t_0;
    private double alpha;
    private int best_cost;
    private int current_cost;
    private double T;
    private int[][] fixed;
    long lStartTime;

    public StochasticSearch(Sudoku sudoku) {
        this.problem = sudoku;
        this.n = sudoku.getN();
        this.N = sudoku.getNSquared();
        this.numberOfCell = sudoku.getNumberOfCell();
        this.t_0 = 100;
        this.alpha = 0.99;
        this.fixed = new int[N][N];
    }

    public Solution solve() {


        lStartTime = System.nanoTime();

        problem = getOptimizedBoard(problem);
        problem.printBoard(1);

        if(problem.isBoardSolved()){
            System.out.println("Miraculously solved the board. No need to go to the Simulated Annealing algorithm.");
            long lEndTime = System.nanoTime();;
            BigDecimal time = BigDecimal.valueOf(lEndTime - lStartTime).divide(BigDecimal.valueOf(1000000));
            return new Solution(problem, time);
        }

        System.out.println("Not solved yet. Going to Simulated Annealing algorithm.");

        problem = fillIn(problem);
        current_cost = costFunction(problem);
        best_cost = current_cost;
        System.out.println(current_cost);
        T = t_0;

        boolean isSolved = false;
        int numTemperatures = 0;
        int improvements = 0;
        while(!isSolved){
            Random rand_col = new Random();
            int col = rand_col.nextInt(N);

            int iterations = getUnfixedCells(col);
            while(iterations>0) {
                Random rand_row1 = new Random();
                int row1 = rand_row1.nextInt(N);

                Random rand_row2 = new Random();
                int row2 = rand_row2.nextInt(N);

                if (fixed[row1][col] != 0 || fixed[row2][col] != 0) {
                    continue;
                }

                int tempCost = current_cost;
                boolean solved = flipNumbers(problem, row1, row2, col);
                if (solved) {
                    isSolved = solved;
                    break;
                }
                if(tempCost > current_cost){
                    improvements+=1;
                }
                iterations--;
            }
            T = alpha * T;
            numTemperatures +=1;

            if(numTemperatures == 20 && improvements == 0){
                T= t_0;
                numTemperatures = 0;
            }
            if(numTemperatures == 20 && improvements > 0){
                improvements = 0;
                numTemperatures = 0;
            }
        }

        long lEndTime = System.nanoTime();
        BigDecimal time = BigDecimal.valueOf(lEndTime - lStartTime).divide(BigDecimal.valueOf(1000000));
        return new Solution(best_solution, time);
    }

    public boolean flipNumbers(Sudoku sudoku, Integer row1, Integer row2, Integer col){


        int num1 = sudoku.getNumber(row1, col);
        int num2 = sudoku.getNumber(row2, col);

        sudoku.setNumber(row1, col, num2);
        sudoku.setNumber(row2, col, num1);

        int newCost = costFunction(sudoku);
        double delta = -(newCost - current_cost);
        double rand  = Math.random();

        //System.out.println("Old Cost: " + current_cost);
        //System.out.println("New Cost: " + newCost);

        if(newCost < best_cost){
            long lEndTime = System.nanoTime();;
            BigDecimal time = BigDecimal.valueOf(lEndTime - lStartTime).divide(BigDecimal.valueOf(1000000));
            best_solution = sudoku;
            best_cost = newCost;
            System.out.println("New best cost: " + best_cost);
            System.out.println(time);

        }
        if(Math.exp(delta/T) - rand >0){
            problem = sudoku;
            current_cost = newCost;
            //System.out.println("Updated current sudoku problem");
        }
        if(newCost == 0){
            best_solution = problem;
            return true;
        }
        return false;
    }


    /** This method takes the initial board and solves the 'obvious' cells to obtain a better partially filled board. **/
    public Sudoku getOptimizedBoard(Sudoku sudoku){

        ArrayList<Integer> numList = new ArrayList<Integer>() ;
        for(int i = 0; i<N; i++){
            numList.add(i+1);
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                //System.out.print("Row: " + i + "\t");
                //System.out.println("Col: " + j);

                //See if the cell is empty
                if(sudoku.getNumber(i,j) == 0) {
                    ArrayList<Integer> tempNumList = new ArrayList<Integer>();
                    Integer[] tempNumArray = numList.toArray(new Integer[tempNumList.size()]);

                    //Find possible values
                    for(int k =0; k< N; k++){
                        if (sudoku.isAllowed(i,j, tempNumArray[k])) {
                            tempNumList.add(tempNumArray[k]);
                        }
                    }

                    //If only one value is possible, assign this value to the cell and restart the outer
                    // loop.
                    if (tempNumList.size() == 1) {
                        //System.out.println("Added number in cell: " + tempNumList.get(0));
                        sudoku.setNumber(i, j, tempNumList.get(0));
                        fixed[i][j] = tempNumList.get(0);
                        i = 0;
                        j = 0;
                    }
                }
                else{
                    fixed[i][j] = sudoku.getNumber(i,j);
                }
            }
        }
        System.out.println();
        return sudoku;
    }

    public int getUnfixedCells(int col){
        int blank_cells=0;
        for (int i = 0; i < N; i++) {
            if(fixed[i][col]==0){
                blank_cells+=1;
            }
        }
        return blank_cells;
    }


    public Sudoku fillIn(Sudoku sudoku){
        for(int i=0; i<N; i++){ //col

            ArrayList<Integer> possNumList = new ArrayList<Integer>();
            for(int j = 0; j<N; j++){
                possNumList.add(j+1);
            }

            for(int j=0; j<N; j++){ //row
                if(sudoku.getNumber(j,i)!=0){ //updates the possible numbers to add into the sudoku
                    possNumList.remove((Object) sudoku.getNumber(j,i));
                }
            }
            for(int k=0; k<N; k++){
                if(possNumList.size() == 0){
                    k = N;
                }else if(sudoku.getNumber(k,i)==0){ //set numbers from possible nums list
                    sudoku.setNumber(k,i, possNumList.get(k%possNumList.size()));
                    possNumList.remove(k%possNumList.size());
                }
            }
        }
        return sudoku;
    }

    public Integer costFunction (Sudoku sudoku){
        int cost=0;
        for(int j=0; j<N; j++){
            for(int k=1; k<=N; k++){
                if(!sudoku.isUsedInRow(j, k)) {
                    cost += 1;
                }
            }
            for(int k=1; k<=N; k++){
                if(!sudoku.isUsedInBox(j, k)) {
                    cost += 1;
                }
            }
        }
        return cost;
    }
}
