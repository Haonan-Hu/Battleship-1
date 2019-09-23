import java.awt.Point;
import java.util.ArrayList;

public class GameBoard {

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
     3 = SUNK SHIP
     ***                 ***/
    /*
     * @ pre none
     *	@ param none
     *	@ post constuctor
     * @ return none
     */
    public GameBoard() {
        boardSize = 8;
        board = new int[boardSize][boardSize];
        oppBoard = new int[boardSize][boardSize];
    }

    /*
     * @ pre none
     *	@ param a new ship
     *	@ post adds a new ship
     * @ return none
     */
    public void addShip(Ship newShip) {
        ArrayList<Point> shipCords = newShip.getShipCoordinates();

        for (Point cords : shipCords) {
            int x = (int) cords.getX();
            int y = (int) cords.getY();
            board[y][x] = 1;
        }

        ships.add(newShip);
    }

    /*
     * @ pre none
     *	@ param x and y coordinates of the grid
     *	@ post fires onto the opposition
     * @ return returns an error, miss, sunk, or hit message
     */
    public String fire(int x, int y) {
        if (x >= boardSize || y >= boardSize || x < 0 || y < 0)
            return "Error Bounds";

        if (board[y][x] == 0)
            return "Miss";

        else if (board[y][x] == 1) {
            for (Ship ship : ships) {
                if (ship.containsCoordinate(x, y)) {
                    ship.hit(x, y);
                    if (ship.isDestroyed())
                        return "Sunk";
                    return "Hit";
                }
            }
        }
        return "Error";
    }

    /*
     * @ pre none
     *	@ param none
     *	@ post gets the board
     * @ return returns the board
     */
    public int[][] getBoard() {
        return board;
    }

    /*
     * @ pre none
     *	@ param none
     *	@ post get the player 2 board
     * @ return returns player 2 board
     */

    public int[][] getOppBoard() {
        return oppBoard;
    }

    /*
     * @ pre one side's ships need to be all sunk
     *	@ param none
     *	@ post checks if the game is over
     * @ return returns true or false whether or not the game is over
     */
    public boolean gameOver() {
        for (Ship ship : ships) {
            if (!ship.isDestroyed())
                return false;
        }
        return true;
    }


    /*
     * @ pre none
     *	@ param x and y values of the grid
     *	@ post gets the ship at a specific coordinate
     * @ return returns a ships coordinates
     */
    public Ship shipAt(int x, int y) {
        for (Ship s : ships) {
            if (s.getShipCoordinates().contains(new Point(x, y)))
                return s;
        }
        return null;
    }

    /*
     * @ pre none
     *	@ param y and x values of the grid, ship's size, and if the ship is horizontal
     *	@ post checks to see if the stop is occupied
     * @ return returns true or false on the spot being occuppied or not
     */
    public boolean isOccupied(int x, int y, int size, boolean horizontal) {
        for (int rep = 0; rep < size; rep++) {
            if (horizontal) {
                if (x + rep > 7 || board[y][x + rep] == 1)
                    return true;
            } else if (!horizontal) {
                if (y + rep > 7 || board[y + rep][x] == 1)
                    return true;
            }

        }

        return false;
    }

    /*
     * @ pre none
     *	@ param x and y coordinates of the grid
     *	@ post updates the boards of hits and misses
     * @ return returns miss, hit, or sunk messages
     */
    public void updateOppBoard(int x, int y, String outcome) {
        if (outcome == "Miss")
            oppBoard[y][x] = 1;
        else if (outcome == "Hit")
            oppBoard[y][x] = 2;
        else if (outcome == "Sunk")
            oppBoard[y][x] = 3;
    }

    /*
     * @ pre none
     *	@ param none
     *	@ post prints the board
     * @ return none
     */
    public void printBoard() {
        System.out.println("  A B C D E F G H");
        for (int i = 0; i < boardSize; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < boardSize; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println(" ");
        }
    }

    /*
     * @ pre none
     *	@ param none
     *	@ post prints the second board
     * @ return none
     */
    public void printOppBoard() {
        System.out.println("  A B C D E F G H");
        for (int i = 0; i < boardSize; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < boardSize; j++)
                System.out.print(oppBoard[i][j] + " ");
            System.out.println(" ");
        }
    }
}
