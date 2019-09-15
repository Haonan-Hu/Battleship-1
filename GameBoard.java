public class GameBoard{
    private int boardSize;
    private int[][] board;
    private Ship[] ships;

    /*** BOARD VARIABLES ***
        0 = EMPTY SPACE
        1 = SHIP
        2 = SHIP HIT
        3 = SUNK SHIP ?
     ***                 ***/

    public GameBoard(int boardSize, Ship[] ships){
        this.boardSize = boardSize;
        this.ships = ships;
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

    public String fire(int x, int y){
        if(x >= boardSize || y >= boardSize || x < 0 || y < 0)
            return "Error: out of board bounds";

        if(board[x][y] == 0){
            return "Miss";
        }
        else if(board[x][y] == 1){
            for(Ship ship : ships){
                if(ship.contains(x,y)){
                    ship.hit(x,y);
                    return "Hit";
                }
            }
            return "HIT!";
        }
        return "Error";
    }

    public int[][] getBoard(){
        return board;
    }

    public boolean gameOver(){
        boolean gameOver = true;
        for(Ship ship :ships){
            if(!ship.sunk()){
                gameOver = false;
                break;
            }
            return gameOver;
        }
    }
}
