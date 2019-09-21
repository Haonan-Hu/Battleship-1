import java.awt.Point;
import java.util.*;

public class Ship {
    private Point startCoordinate;
    private Point[] shipCoordinates;
    private int xCoordinate;
    private int yCoordinate;
    private int shipSize;
    private int shipPieces;

    public Ship (int size) {
        this.shipSize = size;
        this.shipPieces = size;
    }

    public Point[] getShipCoordinates() {
        return shipCoordinates;
    }

    public void setShipCoordinates() {
        Scanner input = new Scanner(System.in);
        shipCoordinates = new Point[shipSize];
        xCoordinate = input.nextInt();
        yCoordinate = input.nextInt();
        shipCoordinates[0] = setCoordinate(xCoordinate,yCoordinate);
        if (shipSize > 1) {
            for (int i = 1; i < shipSize; i++) {
                xCoordinate = input.nextInt();
                yCoordinate = input.nextInt();
                shipCoordinates[i] = setConsecutiveCoordinates(xCoordinate, yCoordinate);
            }
        }
    }

    public Point setConsecutiveCoordinates (int x, int y) {
        Point p = new Point(x,y);
        if (isAligned(startCoordinate,p)) {
            startCoordinate = p;
        }
        else throw new ArithmeticException("Coordinates not consecutive");
        return startCoordinate;
    }

    public Point setCoordinate (int x, int y) {
        if (x >= 0 && x <= 7 && y >= 0 && y <= 7) {
            this.xCoordinate = x;
            this.yCoordinate = y;
            this.startCoordinate = new Point(xCoordinate, yCoordinate);
        }
        else throw new ArithmeticException("Invalid coordinate");
        return startCoordinate;
    }

    private int x,y;
    public boolean isAligned(Point p1, Point p2) {
        if ((p1.x == p2.x) || (p1.y == p2.y)) {
            if (Math.sqrt((p2.x - p1.x)*(p2.x - p1.x) + (p2.y - p1.y)*(p2.y - p1.y)) == 1) return true;
        }
        return false;
    }

    public Point hit(int x, int y) {
        if (containCoordinate(x,y)) shipPieces--;
        return null;
    }

    public boolean containCoordinate(int x, int y) {
        if (x >= 0 && x <= 7 && y >= 0 && y <= 7) {
            Point p = new Point(x, y);
            for (int i = 0; i < shipSize; i++) {
                if (shipCoordinates[i] == p) return true;
            }
        }
        else throw new ArithmeticException("Invalid coordinate");
        return false;
    }

    public boolean containPoint(Point p) {
        if (p != null) {
            for (int i = 0; i < shipSize; i++) {
                if (shipCoordinates[i] == p) return true;
            }
        }
        return false;
    }

    public int getSize() {
        if (shipSize >= 1 && shipSize <=5) return shipSize;
        return -1;
    }

    public boolean isDestroyed() {
        if (shipPieces <= 0) return true;
        return false;
    }

}
