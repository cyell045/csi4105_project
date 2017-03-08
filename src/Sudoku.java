/**
 * Created by ChloeDiTomassoMasse on 2017-02-08.
 */
import java.io.*;


public class Sudoku
{

    static Set empty;
    static Set fullSet;
    static int [][] board;
    static int n;
    static int nsquared;

    public Sudoku (int n, String path)
    {
        n = n;
        nsquared = n*n;
        initializeBoard();

        File file = new File(path);
        readGame(file.getAbsolutePath());
    }

    /**
     * Create board array and create and initialize sets for the
     * board squares.
     **/
    public static void initializeBoard()
    {
        board = new int[nsquared][nsquared];

        Set empty = new Set(nsquared);
        Set fullSet = new Set(nsquared);
        for(int i = 1; i <= nsquared; i++)
            fullSet.insert(i);
    }


    /**
     * Read in a game from a file.
     **/
    public static void readGame(String filename)
    {
        try
        {
            FileReader fr = new FileReader(filename);
            BufferedReader stdin = new BufferedReader(fr);

            String line = "";
            String [] array;

            for(int i = 0; i < nsquared; i++)
            {
                line = stdin.readLine();
                array = line.split(",");
                for(int j = 0; j < nsquared; j++){
                    board[i][j] = Integer.parseInt(array[j]);
                }

            }
        }
        catch(java.io.IOException e)
        {
            System.out.println(e);
        }
    }


    /**
     * Output the current board.
     **/
    public static void printGame()
    {
        System.out.println();
        for(int i = 0; i < nsquared; i++)
        {
            for(int j = 0; j < nsquared; j++)
                System.out.print(board[i][j] + " ");
            System.out.println();
        }
    }


    /**
     * Returns a copy of the sudoku board
     **/
    public int[][] getCopyOfBoard(){
        int [][] newBoard = new int [nsquared][nsquared];

        for(int i = 0; i < nsquared; i++)
        {
            for(int j = 0; j < nsquared; j++){
                newBoard[i][j] = board[i][j];
            }
        }

        return newBoard;
    }

    public int getN(){
        return n;
    }

    public int getNSquared(){
        return nsquared;
    }

    public int getTotalNumberOfCells(){
        return nsquared*nsquared;
    }
}

