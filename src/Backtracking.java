import java.math.BigDecimal;

/**
 * Created by celineyelle on 2017-03-04.
 */
public class Backtracking {

    private Sudoku problem;
    private int n;
    private int N;
    private int numberOfCell;
    private int[][] possibilityMatrix;
    private static final boolean PRINT_CONSTRAINTS = true;
    private static final boolean PRINT_BACKTRACKING = false;

    public Backtracking(Sudoku sudoku) {
        this.problem = sudoku;
        this.n = sudoku.getN();
        this.N = sudoku.getNSquared();
        this.numberOfCell = sudoku.getNumberOfCell();
        this.possibilityMatrix = new int[this.numberOfCell][this.N + 1];
        possibilityMatrix_initialize();
    }

    /**
     * ADDED SUDOKU FUNCTIONS
     **/
    /*Get the number of empty cells left to be solved in the current sudoku board configuration */
    public int numberOfEmptyCellLeft() {
        int sum = 0;
        for (int i = 0; i < numberOfCell; i++) {
            if (problem.getNumber(i) == 0) {
                sum += 1;
            }
        }
        return sum;
    }

    public int numberOfIncorrectlyAssignedCell(){
        int[][] solution = problem.getCopyOfBoard(2);
        int[][] current = problem.getCopyOfBoard(1);
        int sum = 0;

        for(int index=0; index<numberOfCell; index++){
            if(!problem.isSolved(index)){
                if(problem.getNumber(index)!=0){
                    int row = index/N;
                    int col = index%N;

                    if(solution[row][col]!=current[row][col]){
                        sum += 1;
                    }
                }
            }
        }

        return sum;
    }

    public int numberOfCorrectlyAssignedCell(){
        int[][] solution = problem.getCopyOfBoard(2);
        int[][] current = problem.getCopyOfBoard(1);
        int sum = 0;

        for(int index=0; index<numberOfCell; index++){
            if(problem.isSolved(index)){
                sum+=1;
            }else if(problem.getNumber(index)!=0){
                int row = index/N;
                int col = index%N;

                if(solution[row][col]==current[row][col]){
                    sum+=1;
                }
            }
        }

        return sum;
    }

    /**
     * POSSIBILITY MATRIX - FUNCTIONS
     **/
    /* Initialize the possibility matrix using the initial sudoku board configurations */
    public void possibilityMatrix_initialize() {
        for (int i = 0; i < numberOfCell; i++) {
            if (!problem.isSolved(i)) {
                int sum = 0;
                for (int j = 0; j < N; j++) {
                    if (problem.isAllowed(i, (j + 1))) {
                        possibilityMatrix[i][j] = 1;
                        sum += 1;
                    } else {
                        possibilityMatrix[i][j] = 0;
                    }
                }
                possibilityMatrix[i][N] = sum;
            } else {
                possibilityMatrix[i][N] = 1;
                possibilityMatrix[i][problem.getNumber(i) - 1] = 1;
            }
        }
    }

    /* Update the possibility matrix using the current sudoku board configuration */
    public void possibilityMatrix_update() {
        for (int i = 0; i < numberOfCell; i++) {
            if (!problem.isSolved(i)) {
                int sum = 0;

                for (int j = 0; j < N; j++) {
                    if (problem.getNumber(i) == 0) {
                        if (problem.isAllowed(i, j + 1)) {
                            possibilityMatrix[i][j] = 1;
                            sum += 1;
                        } else {
                            possibilityMatrix[i][j] = 0;
                        }
                    } else {
                        if (problem.getNumber(i) == (j + 1)) {
                            possibilityMatrix[i][j] = 1;
                            sum = 1;
                        } else {
                            possibilityMatrix[i][j] = 0;
                        }
                    }
                }
                possibilityMatrix[i][N] = sum;

            } else {
                possibilityMatrix[i][N] = 1;
                possibilityMatrix[i][problem.getNumber(i) - 1] = 1;
            }
        }
    }

    /* Print the possibility matrix on the console **/
    public void possibilityMatrix_print() {
        for (int i = 0; i < numberOfCell; i++) {
            if ((i / N) <= 9) {
                System.out.print("[ " + (i / N) + "][");
            } else {
                System.out.print("[" + (i / N) + "][");
            }

            if ((i % N) <= 9) {
                System.out.print(" " + (i % N) + "] >> ");
            } else {
                System.out.print((i % N) + "] >> ");
            }

            for (int j = 0; j < N; j++) {
                System.out.print("[" + possibilityMatrix[i][j] + "]");
            }
            if (possibilityMatrix[i][N] <= 9) {
                if (problem.isSolved(i)) {
                    System.out.print(" >> " + possibilityMatrix[i][N] + " Solved");
                } else if (problem.getNumber(i) != 0) {
                    System.out.print(" >> " + possibilityMatrix[i][N] + " Assigned");
                } else {
                    System.out.print(" >> " + possibilityMatrix[i][N]);
                }
            } else {
                System.out.print(" >> " + possibilityMatrix[i][N]);
            }
            System.out.println();
        }
        System.out.println();

        System.out.println("SUDOKU corresponding to possibility matrix values: ");
        System.out.println();
        problem.printBoard(1);
    }

    /* Get the value to be assigned for a specific index when its sum is equal to 1 */
    public int possibilityMatrix_getValueToAssign(int index) {
        int safe_value = 0;
        boolean found = false;

        for (int j = 0; j < N && !found; j++) {
            if (possibilityMatrix[index][j] == 1) {
                safe_value = (j + 1);
                found = true;
            }
        }

        return safe_value;
    }

    /**
     * CONSTRAINTS
     **/
    /* Execute the three different constraints until the constraints can no longer be apply on the current sudoku configuration board */
    public void constraints_execution() {
        while (constraint1_viability() || constraint2_viability()) {
            System.out.println();
            System.out.println(">>> 1. CONSTRAINTS EXECUTION <<<");
            System.out.println(" >> Number of empty cells: " + numberOfEmptyCellLeft());
            System.out.println("    Constraint 1: " + constraint1_viability());
            System.out.println("    Constraint 2: " + constraint2_viability());
            System.out.println();

            possibilityMatrix_update();
            constraint1_application();
            constraint2_application();
            //constraint3_application();

            //if(PRINT_CONSTRAINTS){
            //    System.out.println();
            //    problem.printBoard(1);
            //    System.out.println();
            //}
        }

        if(!constraint1_viability() && !constraint2_viability()){
            System.out.println();
            System.out.println(">>> 1. CONSTRAINTS EXECUTION <<<");
            System.out.println(" >> Number of empty cells: " + numberOfEmptyCellLeft());
            System.out.println("    Constraint 1: " + constraint1_viability());
            System.out.println("    Constraint 2: " + constraint2_viability());
            System.out.println();
        }

        possibilityMatrix_print();

    }

    /* CONSTRAINT 1: No other value is allowed according to the possibility matrix, meaning sum = 1*/
    public void constraint1_application() {
        System.out.println(">>> 1.1. CONSTRAINT 1 - APPLICATION <<<");
        System.out.println(" >> Number of empty cells: " + numberOfEmptyCellLeft());
        System.out.println();

        int index = 0;
        boolean changed = false;

        while (index >= 0 && index < numberOfCell) {
            //We can only apply the constraint #1 to unsolved cell
            if (!problem.isSolved(index)) {
                //We can only apply the constraint #1 to empty cell
                if (problem.getNumber(index) == 0) {
                    //We can only apply the constraint #1 to an empty cell that has a possibility sum of 1.
                    if (possibilityMatrix[index][N] == 1) {
                        if (PRINT_CONSTRAINTS) {
                            System.out.println(" >> SUDOKU[" + (index / N) + "][" + (index % N) + "]: Only possible value is >> " + possibilityMatrix_getValueToAssign(index));
                        }
                        problem.setNumber(index, possibilityMatrix_getValueToAssign(index));
                        possibilityMatrix_update();

                        if (PRINT_CONSTRAINTS) {
                            System.out.println();
                            possibilityMatrix_print();
                            System.out.println();
                        }

                        index = 0;
                        changed = true;

                    } else {
                        index += 1;
                    }
                } else {
                    index += 1;
                }
            } else {
                index += 1;
            }
        }

        if (!changed && PRINT_CONSTRAINTS) {
            System.out.println("    No changes were made to the possibility matrix.");
            System.out.println();
        }
    }

    /* Check the viability of applying the constraint #1 */
    public boolean constraint1_viability() {
        boolean viable = false;
        for (int index = 0; index < numberOfCell && !viable; index++) {
            if (!problem.isSolved(index)
                    && problem.getNumber(index) == 0
                    && possibilityMatrix[index][N] == 1) {
                viable = true;
            }
        }
        return viable;
    }

    /* CONSTRAINT 2: A certain value is allowed in no other cell in the same section:
     * >> row i corresponding to cell[i][j]
     * >> col j corresponding to cell[i][j]
     * >> box corresponding to startBox (row-row%n) and startCol (col-col%n)
     */
    public void constraint2_application() {
        System.out.println(">>> 1.2. CONSTRAINT 2 - APPLICATION <<<");
        System.out.println(" >> Number of empty cells: " + numberOfEmptyCellLeft());
        System.out.println();
        int index = 0;

        while (index >= 0 && index < numberOfCell) {
            //We can only apply constraint #2 on unsolved cell
            if (!problem.isSolved(index)) {
                //We can only apply constraint #2 on empty cell
                if (problem.getNumber(index) == 0) {
                    boolean found = false;
                    //Iterate through value 1 to N
                    for (int j = 0; j < N && !found; j++) {
                        if (possibilityMatrix[index][j] == 1) {
                            if (PRINT_CONSTRAINTS) {
                                System.out.println(" >> SUDOKU[" + (index / N) + "][" + (index % N) + "] && THE VALUE " + (j + 1) + ": ");
                            }

                            //Verification of all the cell of the corresponding row && col
                            if (constraint2_uniquePosition(index, j, true) || constraint2_uniquePosition(index, j, false)) {
                                if (PRINT_CONSTRAINTS) {
                                    System.out.println("    RESULT: Only possible position for value " + (j + 1) + " is SUDOKU[" + (index / N) + "][" + (index % N) + "]");
                                }
                                found = true;
                                problem.setNumber(index, (j + 1));
                                possibilityMatrix_update();
                            } else {
                                if (PRINT_CONSTRAINTS) {
                                    System.out.println("    RESULT: The value can be assigned at other position (either in the corresponding row or col");
                                }
                            }
                            if(PRINT_CONSTRAINTS){
                                System.out.println();
                            }
                        }
                    }

                    if (found) {
                        if(PRINT_CONSTRAINTS){
                            possibilityMatrix_print();
                        }
                        index = 0;
                    } else {
                        index += 1;
                    }
                } else {
                    index += 1;
                }
            } else {
                index += 1;
            }
        }
    }

    /* Check the viability of applying the constraint #2 */
    public boolean constraint2_viability(){
        boolean viable = false;
        for(int index=0; index<numberOfCell && !viable; index++){
            for(int j=0; j<N; j++){
                if(!problem.isSolved(index) && problem.getNumber(index)==0){
                    if(constraint2_uniquePosition(index,j)){
                        viable = true;
                    }
                }
            }
        }
        return viable;
    }

    /* Check if the value can uniquely be assigned to the observed cell (verifying the corresponding row and col) */
    public boolean constraint2_uniquePosition(int index, int j){
        int row = index/N;
        int col = index%N;
        int computed_row=0;
        int computed_col=0;
        boolean uniquePosition = true;

        for(int x=0; x<N && uniquePosition; x++){
            computed_row = row*N + x;
            computed_col = x*N + col;
            if((computed_row!=index && possibilityMatrix[computed_row][j]==1)
                    || (computed_col!=index && possibilityMatrix[computed_col][j]==1)) {
                uniquePosition = false;
            }
        }

        return uniquePosition;
    }

    /* Check if the value can uniquely be assigned to the observed cell
     * - Verify the corresponding cells in the row
     * - Verify the corresponding cells in the col
     */
    public boolean constraint2_uniquePosition(int index, int j, boolean rowObserved){

        if(PRINT_CONSTRAINTS) {
            if (rowObserved) {
                System.out.println("    >> VERIFICATION OF CORRESPONDING ROW");
            } else {
                System.out.println("    >> VERIFICATION OF CORRESPONDING COL");
            }
        }

        int row = index/N;
        int col = index%N;
        int computed_index = 0;
        boolean uniquePosition = true;

        for(int x = 0; x < N && uniquePosition; x++){
            if(rowObserved){
                computed_index = row*N + x;
            }else{
                computed_index = x*N + col;
            }

            if(PRINT_CONSTRAINTS){
                if(computed_index!=index && possibilityMatrix[computed_index][j]==1){
                    System.out.println("       >> [" + (computed_index/N) + "][" + (computed_index%N) + "]: the value " + (j+1) + " is allowed here.");
                    uniquePosition=false;
                }else if(computed_index!=index){
                    System.out.println("       >> [" + (computed_index/N) + "][" + (computed_index%N) + "]: the value " + (j+1) + " is not allowed here.");
                }
            }else{
                if(computed_index!=index && possibilityMatrix[computed_index][j]==1) {
                    uniquePosition = false;
                }
            }
        }

        if(PRINT_CONSTRAINTS){
            if(uniquePosition){
                System.out.println("       UNIQUE_POSITION = TRUE");
            } else{
                System.out.println("       UNIQUE_POSITION = FALSE");
            }
        }

        return uniquePosition;
    }

    /* CONSTRAINT 3: A certain value is allowed in no other cell in the same section:
     * >> box corresponding to startBox (row-row%n) and startCol (col-col%n)
     */
    public void constraint3_application(){
        System.out.println(">>> 1.2. CONSTRAINT 3 - APPLICATION <<<");
        System.out.println(" >> Number of empty cells: " + numberOfEmptyCellLeft());
        System.out.println();
        int index = 0;

        while (index >= 0 && index < numberOfCell) {
            //We can only apply constraint #3 on unsolved cell
            if (!problem.isSolved(index)) {
                //We can only apply constraint #3 on empty cell
                if (problem.getNumber(index) == 0) {
                    boolean found = false;

                    //Iterate through value 1 to N
                    for (int j = 0; j < N && !found; j++) {
                        if (possibilityMatrix[index][j] == 1) {
                            if (PRINT_CONSTRAINTS) {
                                System.out.println(" >> SUDOKU[" + (index / N) + "][" + (index % N) + "] && THE VALUE " + (j + 1) + ": ");
                            }
                            //Verification of all the cell of the corresponding box
                            int row = index/N;
                            int col = index%N;

                            if(constraint3_uniquePositionBox(row, row-row%n, col, col-col%n, j)){
                                if (PRINT_CONSTRAINTS) {
                                    System.out.println("    RESULT: Only possible position for value " + (j + 1) + " is SUDOKU[" + (index / N) + "][" + (index % N) + "]");
                                }
                                found = true;
                                problem.setNumber(index, (j + 1));
                                possibilityMatrix_update();
                            } else {
                                if (PRINT_CONSTRAINTS) {
                                    System.out.println("    RESULT: The value can be assigned at other position in the corresponding box.");
                                }
                            }
                            if(PRINT_CONSTRAINTS){
                                System.out.println();
                            }
                        }
                    }

                    if (found) {
                        index = 0;
                    } else {
                        index += 1;
                    }
                } else {
                    index += 1;
                }
            } else {
                index += 1;
            }
        }
    }

    public boolean constraint3_uniquePositionBox(int row, int startRow, int col, int startCol, int j){
        boolean unique = true;

        for(int x=0; x<n; x++){
            int computed_row = startRow + x;
            for(int y=0; y<n; y++){
                int computed_col = startCol + y;
                if(computed_col!=col && computed_row!=row){
                    int computed_index = computed_row*N + computed_col;
                    if(possibilityMatrix[computed_index][N]==1) {
                        if (PRINT_CONSTRAINTS) {
                            System.out.println("    >> [" + (computed_index / N) + "][" + (computed_index % N) + "]: the value " + (j + 1) + " is allowed here.");
                        }
                        unique = false;
                    }else{
                        if(PRINT_CONSTRAINTS){
                            System.out.println("    >> [" + (computed_index/N) + "][" + (computed_index%N) + "]: the value " + (j+1) + " is not allowed here.");
                        }
                    }
                }
            }
        }

        return unique;
    }

    public Solution solve() {
        long lStartTime = System.nanoTime();

        constraints_execution();

        int empty = numberOfEmptyCellLeft();
        int correct = numberOfCorrectlyAssignedCell();
        int incorrect = numberOfIncorrectlyAssignedCell();

        System.out.println();
        System.out.println(">>> 2. BACKTRACKING <<<");
        System.out.println(" >> Number of empty cells: " + empty);
        System.out.println(" >> Number of correctly assigned cells: " + correct);
        System.out.println(" >> Number of incorrectly assigned cells: " + incorrect);
        System.out.println(" >> Total number of cells = " + (empty+correct+incorrect));
        System.out.println();

        int index = 0;
        int row;
        int col;

        boolean backtracking_needed = false;


        while (index >= 0 && index < numberOfCell) {
            row = index / N;
            col = index % N;

            //We only apply the algorithm to the unsolved cell
            if (!problem.isSolved(index)) {
                if (PRINT_BACKTRACKING) {
                    System.out.println(" >> INDEX: " + index);
                }

                if (!backtracking_needed && problem.getNumber(index) != 0 && possibilityMatrix[index][N] == 1) {
                    if (PRINT_BACKTRACKING) {
                        System.out.println("    >> SUDOKU[" + row + "][" + col + "]");
                        System.out.println("       Current number assigned: " + problem.getNumber(index) + " (Assignment by constraints)");
                        System.out.println("       BACKTRACKING_NEEDED: FALSE");
                    }
                    backtracking_needed = false;
                } else {
                    if (PRINT_BACKTRACKING) {
                        System.out.println("    >> SUDOKU[" + row + "][" + col + "]");
                        System.out.println("       Current number assigned: " + problem.getNumber(index));
                    }
                    problem.setNumber(index, problem.getNumber(index) + 1);

                    //Find the next valid value, if one exists...
                    while (!problem.isAllowed(index, problem.getNumber(index)) && problem.getNumber(index) <= N) {
                        problem.setNumber(index, problem.getNumber(index) + 1);
                    }

                    if (problem.getNumber(index) > N) {
                        if (PRINT_BACKTRACKING) {
                            System.out.println("       >> Number: " + problem.getNumber(index));
                            System.out.println("          BACKTRACKING_NEEDED: TRUE");
                            System.out.println();
                            problem.printBoard(1);
                            System.out.println();
                        }
                        backtracking_needed = true;
                        problem.setNumber(index, 0);

                    } else {
                        backtracking_needed = false;
                        if (PRINT_BACKTRACKING) {
                            System.out.println("       >> Number: " + problem.getNumber(index));
                            System.out.println("          BACKTRACKING_NEEDED: FALSE");
                            System.out.println();
                            problem.printBoard(1);
                            System.out.println();
                        }
                    }
                }
            } else {
                if (PRINT_BACKTRACKING) {
                    System.out.println(" >> INDEX: " + index + " is already solved.");
                }
            }

            if (backtracking_needed) {
                index -= 1;
            } else {
                index += 1;
            }

            if (PRINT_BACKTRACKING) {
                System.out.println("    >> Moving to index: " + index + " [" + row + "][" + col + "]");
                System.out.println();
            }
        }

        long lEndTime = System.nanoTime();
        BigDecimal time = BigDecimal.valueOf(lEndTime - lStartTime).divide(BigDecimal.valueOf(1000000));
        return new Solution(problem, time);
    }
}