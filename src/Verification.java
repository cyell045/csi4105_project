import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by celineyelle on 2017-03-08.
 */
public class Verification {

    public static boolean verifyPuzzle ( int [][] puzzle, int [][] solution){
        int N = puzzle.length;


        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (puzzle[i][j] != 0 && puzzle[i][j] != solution[i][j])
                {
                    return false;
                }

            }
        }
        return true;
    }


    public static boolean verifySolution (Solution s){
        int [][] algoSolution = s.getSolvedBoard();
        int [][] solution =  s.getSolutionBoard();
        int N = algoSolution.length;

        for (int i = 0; i < N; i++)  {
            for (int j = 0; j < N; j++) {
                if (algoSolution[i][j] != solution[i][j]) {
                        return false;
                }
            }
        }
        return true;
    }

}
