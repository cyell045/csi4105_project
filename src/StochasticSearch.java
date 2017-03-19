/**
 * Created by celineyelle on 2017-03-04.
 */
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

    public StochasticSearch(Sudoku sudoku) {
        this.problem = sudoku;
        this.n = sudoku.getN();
        this.N = sudoku.getNSquared();
        this.numberOfCell = sudoku.getNumberOfCell();
        this.t_0 = 100.0;
        this.alpha = 0.99;
    }

    public Solution solve() {


        long lStartTime = new Date().getTime();

        problem = getOptimizedBoard(problem);

        if(problem.isBoardSolved()){
            System.out.println("Miraculously solved the board. No need to go to the Simulated Annealing algorithm.");
            long lEndTime = new Date().getTime();
            BigDecimal time = BigDecimal.valueOf(lEndTime - lStartTime).divide(BigDecimal.valueOf(1000000));
            return new Solution(problem, time);
        }

        problem = fillIn(problem);

        current_cost = costFunction(problem);
        best_cost = current_cost;
        T = t_0;

        while(T>0){
            Random rand_col = new Random();
            int col = rand_col.nextInt(N);

            Random rand_row1 = new Random();
            int row1= rand_row1.nextInt(N);

            Random rand_row2 = new Random();
            int row2 = rand_row2.nextInt(N);

        }

        System.out.print("Not solved yet. Going to Simulated Annealing algorithm.");

        long lEndTime = new Date().getTime();
        BigDecimal time = BigDecimal.valueOf(lEndTime - lStartTime).divide(BigDecimal.valueOf(1000000));
        return new Solution(problem, time);
    }

    public boolean flipNumbers(Sudoku sudoku, Integer row1, Integer row2, Integer col){
        int cur_cost_row1 = 0; int cur_cost_row2 = 0; int cur_cost_box1 = 0; int cur_cost_box2 = 0;
        int new_cost_row1 = 0; int new_cost_row2 = 0; int new_cost_box1 = 0; int new_cost_box2 = 0;
        for(int i = 1; i<=N; i++)
        {
            if(sudoku.isUsedInRow(row1,i )){cur_cost_row1 +=1;}
            if(sudoku.isUsedInRow(row2,i )){cur_cost_row2 +=1;}
            if(sudoku.isUsedInBox(row1,i)){cur_cost_box1 +=1;}
            if(sudoku.isUsedInBox(row2,i)){cur_cost_box2 +=1;}
        }

        int num1 = sudoku.getNumber(row1, col);
        int num2 = sudoku.getNumber(row2, col);

        int tempCost = current_cost - (cur_cost_box1 + cur_cost_box2 + cur_cost_row1 + cur_cost_row2);

        sudoku.setNumber(row1, col, num2);
        sudoku.setNumber(row2, col, num1);

        for(int j = 1; j<=N; j++)
        {
            if(sudoku.isUsedInRow(row1,j )){new_cost_row1 +=1;}
            if(sudoku.isUsedInRow(row2,j )){new_cost_row2 +=1;}
            if(sudoku.isUsedInBox(row1,j)){new_cost_box1 +=1;}
            if(sudoku.isUsedInBox(row2,j)){new_cost_box2 +=1;}
        }

        int newCost = tempCost + new_cost_box1 + new_cost_box2 + new_cost_row1 + new_cost_row2;
        double delta = -(newCost - current_cost);
        double rand  = Math.random();

        if(Math.exp(delta/T) - rand >0){
            current_cost = newCost;
        }else if(newCost < best_cost){
            best_solution = problem;
            best_cost = newCost;
            current_cost = newCost;
        } else if(newCost == 0){
            best_solution = problem;
            return true;
        } else{
            problem.setNumber(row1,col, num1);
            problem.setNumber(row2, col, num2);
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
                    int[] sBox = new int[]{-1, -1};

                    //Find box the cell is in
                    for (int k = n; k <= N; k += n) {
                        if (i < k && sBox[0] == -1) {
                            sBox[0] = k - n;
                        }
                        if (j < k && sBox[1] == -1) {
                            sBox[1] = k - n;
                        }
                        if (sBox[0] != -1 && sBox[1] != -1) {
                            break;
                        }
                    }
                    //System.out.println("Box: [" + sBox[0] + "][" + sBox[1]+"]");

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
                        i = 0;
                        j = 0;
                    }
                }
            }
        }
        return sudoku;
    }

    public Sudoku fillIn(Sudoku sudoku){

        LinkedList<Integer> numList = new LinkedList<Integer>() ;
        for(int i = 0; i<N; i++){
            numList.add(i+1);
        }

        LinkedList<LinkedList<Integer>> possibleNumsList = new LinkedList<LinkedList<Integer>>() ;
        for(int i=0; i<N; i++){ //col
            possibleNumsList.add(i, numList);
            for(int j=0; j<N; j++){ //row
                if(sudoku.getNumber(j,i)!=0){ //updates the possible numbers to add into the sudoku
                    possibleNumsList.get(i).remove(sudoku.getNumber(j,i));
                }
            }
            for(int k=0; k<N; k++){
                if(sudoku.getNumber(k,i)==0){ //set numbers from possible nums list
                    sudoku.setNumber(k,i, possibleNumsList.get(i).removeFirst());
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
