import java.awt.Point;
import java.util.*;

public class Ship {
    private Point startCoordinate;
    private Point[] shipCoordinates;
    private int xCoordinate;
    private int yCoordinate;
    private int shipSize;
    private int shipPieces;

    public void createShip (int size) {
        this.shipSize = size;
        this.shipPieces = size;
    }

    public void getShipCoordinates() {
        for (int i = 0; i < shipSize; i++) {
            System.out.println(shipCoordinates[i]);
        }
    }

    public void setShipCoordinates() {
        Scanner input = new Scanner(System.in);
        shipCoordinates = new Point[shipSize];
        for (int i = 0; i < shipSize; i++) {
            xCoordinate = input.nextInt();
            yCoordinate = input.nextInt();
            shipCoordinates[i] = setCoordinate(xCoordinate,yCoordinate);
        }
    }

    public Point setCoordinate (int x, int y) {
        if (x >= 0 && x <= 7 && y >= 0 && y <= 7) {
            this.startCoordinate = new Point(x,y);
            this.xCoordinate = x;
            this.yCoordinate = y;
        }
        else throw new ArithmeticException("Invalid coordinate");
        return startCoordinate;
    }

    public int getSize() {
        if (shipSize >= 1 && shipSize <=5) return shipSize;
        return -1;
    }

    public void decreaseShipPieces() {
        shipPieces--;
    }

    public boolean isDestroyed() {
        if (shipPieces <= 0) return true;
        return false;
    }

}