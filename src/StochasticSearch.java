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
    private Sudoku problem;
    private Sudoku best_solution;
    private double t_0;
    private double alpha;
    private int best_cost;
    private int current_cost;
    private double T;
    private int[][] fixed;
    private ArrayList<Integer>[][] cellsLists;
    long lStartTime;

    public StochasticSearch(Sudoku sudoku) {
        this.problem = sudoku;
        this.n = sudoku.getN();
        this.N = sudoku.getNSquared();
        //this.t_0 = 100;
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

        this.t_0 = obtainInitialTemp(problem);
        System.out.println("Initial Temperature: " + t_0);

        current_cost = costFunction(problem);
        best_cost = current_cost;
        best_solution = problem;
        System.out.println(current_cost);
        T = t_0;

        boolean solved = false;
        int numTemperatures = 0;
        int improvements = 0;
        int iterations = getNeighbourhoodMoves();
        System.out.println("Num of iterations: " + iterations);
        while(!solved){
            Random rand_col = new Random();
            int col = rand_col.nextInt(N);
            for(int i =0; i<iterations; i++) {
                Random rand_row1 = new Random();
                int row1 = rand_row1.nextInt(N);

                Random rand_row2 = new Random();
                int row2 = rand_row2.nextInt(N);

                if (fixed[row1][col] != 0 || fixed[row2][col] != 0) {
                    continue;
                }

                int tempCost = current_cost;
                solved = flipNumbers(problem, row1, row2, col);
                if (solved) {
                    break;
                }
                if(tempCost > current_cost){
                    improvements+=1;
                }
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

        if(Math.exp(delta/T) - rand >0){
            problem = sudoku;
            current_cost = newCost;
        }

        if(current_cost < best_cost){
            long lEndTime = System.nanoTime();;
            BigDecimal time = BigDecimal.valueOf(lEndTime - lStartTime).divide(BigDecimal.valueOf(1000000));
            best_solution = sudoku;
            best_cost = current_cost;
            System.out.println("New best cost: " + best_cost);
            System.out.println(time);
        }

        if(newCost == 0){
            best_solution = problem;
            return true;
        }
        return false;
    }


    /** This method takes the initial board and solves the 'obvious' cells to obtain a better partially filled board. **/
    public Sudoku getOptimizedBoard(Sudoku sudoku) {
        cellsLists = new ArrayList[N][N];

        ArrayList<Integer> numList = new ArrayList<Integer>() ;
        for(int i = 0; i<N; i++){
            numList.add(i+1);
        }

        sudoku = partiallyFill(sudoku, numList);
        updateCellsLists(sudoku, numList);

        for(int i = 0; i<N; i++){
            for (int j = 0; j < N; j++) {
                if(sudoku.getNumber(i,j) == 0){
                    for(int number: cellsLists[i][j]){
                        if( !isThere("col", number, i, j, sudoku) ||
                            !isThere("row", number, i, j, sudoku) ||
                            !isThere("box", number, i, j, sudoku)){
                            sudoku.setNumber(i, j, number);
                            sudoku = partiallyFill(sudoku, numList);
                            updateCellsLists(sudoku, numList);
                            fixed[i][j] = number;
                            i = 0;
                            j = 0;
                            break;
                        }
                    }
                }
            }
        }

        sudoku = partiallyFill(sudoku, numList);
        return sudoku;
    }

    public void updateCellsLists(Sudoku sudoku, ArrayList<Integer> numList) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                cellsLists[i][j] = new ArrayList<Integer>();
                if (sudoku.getNumber(i, j) == 0) {
                    ArrayList<Integer> tempNumList = new ArrayList<Integer>();
                    Integer[] tempNumArray = numList.toArray(new Integer[tempNumList.size()]);

                    //Find possible values
                    for (int k = 0; k < N; k++) {
                        if (sudoku.isAllowed(i, j, tempNumArray[k])) {
                            tempNumList.add(tempNumArray[k]);
                        }
                    }
                    cellsLists[i][j] = tempNumList;
                }
            }
        }
    }

    private boolean isThere(String type, int number, int row, int col, Sudoku sudoku){
        boolean isThere = false;
        switch(type){
            case "box":
                int startRow = row - (row%n);
                int startCol = col - (col%n);
                for(int i = startRow; i<n+startRow && !isThere; i++){
                    for(int j = startCol; j< n + startCol; j++){
                        if(cellsLists[i][j].contains(number)
                                &&(j!=col || i!=row)){
                            isThere = true;
                            break;
                        }
                    }
                }
                break;
            case "col":
                for(int i = 0; i<N; i++) {
                    if (cellsLists[i][col].contains(number) && i != row) {
                        isThere = true;
                        break;
                    }
                }
                break;
            case "row":
                for(int i = 0; i<N; i++) {
                    if (cellsLists[row][i].contains(number) && i != col) {
                        isThere = true;
                        break;
                    }
                }
                break;
        }
        return isThere;
    }

    public Sudoku partiallyFill(Sudoku sudoku, ArrayList<Integer> numList){

        updateCellsLists(sudoku, numList);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                //See if the cell is empty
                if(sudoku.getNumber(i,j) == 0) {
                    //If only one value is possible, assign this value to the cell and restart the outer
                    // loop.
                    ArrayList<Integer> tempNumList = cellsLists[i][j];
                    if (tempNumList.size() == 1) {
                        sudoku.setNumber(i, j, tempNumList.get(0));
                        fixed[i][j] = tempNumList.get(0);
                        updateCellsLists(sudoku, numList);
                        i = 0;
                        j = 0;
                    }
                }
                else{
                    fixed[i][j] = sudoku.getNumber(i,j);
                }
            }
        }
        return sudoku;
    }

    public int getNeighbourhoodMoves(){
        int blank_cells=0;
        for(int j = 0; j<N; j++){
            for (int i = 0; i < N; i++) {
                if(fixed[i][j]==0){
                   blank_cells+=1;
                }
            }
        }
        return blank_cells*blank_cells;
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

    public double obtainInitialTemp(Sudoku sudoku){
        int[] costs = new int[100];

        int i = 0;
        int sum = 0;
        while(i<100){
            Random rand_col = new Random();
            int col = rand_col.nextInt(N);

            Random rand_row1 = new Random();
            int row1 = rand_row1.nextInt(N);

            Random rand_row2 = new Random();
            int row2 = rand_row2.nextInt(N);

            if (fixed[row1][col] != 0 || fixed[row2][col] != 0) {
                continue;
            }

            int num1 = sudoku.getNumber(row1, col);
            int num2 = sudoku.getNumber(row2, col);

            sudoku.setNumber(row1, col, num2);
            sudoku.setNumber(row2, col, num1);

            costs[i] = costFunction(sudoku);
            sum += costs[i];
            i++;
        }

        double avg = sum/costs.length;
        double avg_squared = avg*avg;
        sum = 0;

        for(i = 0; i< costs.length; i++){
            int sq = costs[i]*costs[i];
            sum += sq;
        }

        double avg_of_squares = sum/costs.length;

        return avg_of_squares - avg_squared;
    }
}
