import java.awt.Point;
import java.util.ArrayList;

public class Ship {

    private ArrayList<Point> shipCoordinates = new ArrayList<Point>();
    private int shipSize;
    private int shipPieces;

    /*
     * @ pre none
     *	@ param Ships' size
     *	@ post constuctor
     * @ return none
     */
    public Ship(int size) {
        this.shipSize = size;
        this.shipPieces = size;
    }

    /*
     * @ pre none
     *	@ param none
     *	@ post gets ship's size
     * @ return returns ships size
     */
    public int getSize() {
        return shipSize;
    }

    /*
     * @ pre none
     *	@ param none
     *	@ post gets ships coordinates
     * @ return returns ship's coordinates
     */
    public ArrayList<Point> getShipCoordinates() {
        return shipCoordinates;
    }

    /*
     * @ pre none
     *	@ param x and y values of the grid
     *	@ post adds new coordinates of the ship
     * @ return none
     */
    public void addCoordinates(int x, int y) {
        shipCoordinates.add(new Point(x, y));
    }

    /*
     * @ pre none
     *	@ param new x and y values of the grid
     *	@ post none
     * @ return returns true or false on whether or not the ships are in line
     */
    public boolean inline(int newX, int newY) {
        if (shipCoordinates.size() == 0)
            return true;

        for (Point shipPiece : shipCoordinates) {
            int x = (int) shipPiece.getX();
            int y = (int) shipPiece.getY();
            if (newX == x && (newY == y + 1 || newY == y - 1))
                return true;
            else if (newY == y && (newX == x + 1 || newX == x - 1))
                return true;
        }
        return false;
    }

    /*
     * @ pre the coordinate an opponenet entered to fire at
     *	@ param x and y values of the grid
     *	@ post decreases the number of ships because it was "attacked"
     * @ return none
     */
    public void hit(int x, int y) {
        shipPieces--;
    }

    /*
     * @ pre none
     *	@ param x and y values of the grid
     *	@ post none
     * @ return returns true or false on whether or not a specific coordinate exists or not
     */
    public boolean containsCoordinate(int x, int y) {
        if (shipCoordinates.size() == 0)
            return false;

        if (x >= 0 && x <= 7 && y >= 0 && y <= 7) {
            for (int i = 0; i < shipCoordinates.size(); i++) {
                int cordX = (int) shipCoordinates.get(i).getX();
                int cordY = (int) shipCoordinates.get(i).getY();
                if (cordX == x && cordY == y)
                    return true;
            }
        }
        return false;
    }

    /*
     * @ pre the coordinate an opponenet entered to fire at a specific ship
     *	@ param none
     *	@ post none
     * @ return returns true or false on whether or not the ship is destroyed or not
     */
    public boolean isDestroyed() {
        if (shipPieces <= 0)
            return true;
        return false;
    }

}
