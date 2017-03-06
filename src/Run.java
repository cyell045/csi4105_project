/**
 * Created by ChloeDiTomassoMasse on 2017-02-08.
 */

import java.io.IOException;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;

public class Run {
    public static void main(String[] args) throws IOException {
        System.out.println("Pick a size for your Sudoku puzzle (between 2 and 5:");
        int i=0;
        boolean wrongNumber;
        do {
            Scanner sc = new Scanner(System.in);
            i = sc.nextInt();
            if (i > 1 && i < 6) {
                wrongNumber = false;
            }
            else {
                System.out.println("Please pick a number between 2 and 5.");
                wrongNumber = true;
            }
        }
        while(wrongNumber);

        int n = i;

        String path = "puzzles/"+n+"x"+n;
        int max = (int)Files.list(Paths.get(path)).count();
        int random = ThreadLocalRandom.current().nextInt(1, max + 1);
        path = path + "/p" + random + ".txt";
        System.out.println("SUDOKU PUZZLE CHOSEN: p"+random);

        Sudoku sudoku = new Sudoku(n, path);
        sudoku.printGame();

        System.out.println("------------------------------------------------");
        System.out.println();

        System.out.println("BACKTRACKING");
        Solution s1 = new Backtracking(n, sudoku).solve();
        s1.printAlgoSolution();

        System.out.println("STOCHASTIC SEARCH");
        Solution s2 = new StochasticSearch(n, sudoku).solve();
        s2.printAlgoSolution();

        System.out.println("DANCING LINKS");
        Solution s3 = new DancingLinks(n, sudoku).solve();
        s3.printAlgoSolution();
    }

}
