giimport java.awt.Point;
import java.util.ArrayList;

public class GameBoard{

    private int boardSize;
    private int[][] board;
    private int[][] oppBoard;
    private ArrayList<Ship> ships = new ArrayList<Ship>();

    /*** OWN BOARD VARIABLES ***
        0 = EMPTY SPACE
        1 = SHIP
     ***                 ***/

    /*** OPP BOARD VARIABLES ***
        0 = BLANK
        1 = MISS
        2 = HIT SHIP
     ***                 ***/

    public GameBoard(){
        boardSize = 8;
        board = new int[boardSize][boardSize];
        oppBoard = new int[boardSize][boardSize];
    }

    public void addShip(Ship newShip){
        ArrayList<Point> shipCords = newShip.getShipCoordinates();

        for(Point cords : shipCords){
            int x = (int)cords.getX();
            int y = (int)cords.getY();
            board[x][y] = 1;
        }

        ships.add(newShip);
    }

    public String fire(int x, int y){
        if(x >= boardSize || y >= boardSize || x < 0 || y < 0)
            return "Error Bounds";

        if(board[x][y] == 0)
            return "Miss";

        else if(board[x][y] == 1){
            for(Ship ship : ships){
                if(ship.containsCoordinate(x,y)){
                    ship.hit(x,y);
                    if(ship.isDestroyed())
                        return "Sunk";
                    return "Hit";
                }
            }
        }
        return "Error";
    }

    public int[][] getBoard(){ return board; }

    public boolean gameOver(){
        for(Ship ship : ships){
            if(!ship.isDestroyed())
                return false;
        }
        return true;
    }

    public boolean isOccupied(int x, int y){
        if(board[x][y] == 0)
            return false;
        else
            return true;
    }

    public void updateOppBoard(int x, int y, String outcome){
        if(outcome == "Miss")
            oppBoard[x][y] = 1;
        else
            oppBoard[x][y] = 2;
    }

    /****** remove this probably ******/
    public void printBoard(){
        System.out.println("  A B C D E F G H");
        for(int i = 0; i < boardSize; i++){
            System.out.print((i + 1) + " ");
            for(int j = 0; j < boardSize; j++){
                System.out.print(board[i][j] + " ");
            }
            System.out.println(" ");
        }
    }

    public void printOppBoard(){
        System.out.println("  A B C D E F G H");
        for(int i = 0; i < boardSize; i++){
            System.out.print((i + 1) + " ");
            for(int j = 0; j < boardSize; j++)
                System.out.print(oppBoard[i][j] + " ");
            System.out.println(" ");
        }
    }
}
