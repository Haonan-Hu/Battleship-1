import java.awt.Point;
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
            board[y][x] = 1;
        }

        ships.add(newShip);
    }

    public String fire(int x, int y){
        if(x >= boardSize || y >= boardSize || x < 0 || y < 0)
            return "Error Bounds";

        if(board[y][x] == 0)
            return "Miss";

        else if(board[y][x] == 1){
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
    
    public int[][] getOppBoard(){return oppBoard;}

    public boolean gameOver(){
        for(Ship ship : ships){
            if(!ship.isDestroyed())
                return false;
        }
        return true;
    }
    
    public Ship shipAt(int x, int y){
        for(Ship s : ships){
            if(s.getShipCoordinates().contains(new Point(x,y))){
                return s;
            }
        }
        return null;
    }

    public boolean isOccupied(int x, int y, int size, boolean horizontal){
        for(int rep = 0; rep<size; rep++)
        {
            if(horizontal){
                if(x+rep > 7  || board[y][x+rep] == 1)
                    return true;      
            }
            else if(!horizontal){
                if(y+rep > 7 || board[y+rep][x] == 1)
                    return true;   
            }
            
        }   
        
        return false;
    }

    public void updateOppBoard(int x, int y, String outcome){
        if(outcome == "Miss")
            oppBoard[y][x] = 1;
        else
            oppBoard[y][x] = 2;
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
