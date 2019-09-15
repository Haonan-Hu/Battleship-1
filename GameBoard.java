public class GameBoard{
    private int boardSize;
    private int[][] board;

    /*** BOARD VARIABLES ***
        0 = EMPTY SPACE
        1 = SHIP
        2 = SHIP HIT
        3 = SUNK SHIP
     ***                 ***/


    public GameBoard(int boardSize){
        this.boardSize = boardSize;
        board = new int[boardSize][boardSize];

        for(int row = 0; row < boardSize; row++){
            for(int col = 0; col < boardSize; col++){
                board[row][col] = 0;
            }
        }
    }

    public void printBoard(){
        for(int row = 0; row < boardSize; row++){
            for(int col = 0; col < boardSize; col++){
                System.out.print(board[row][col] + " ");
            }
            System.out.println(" ");
        }
    }

}
