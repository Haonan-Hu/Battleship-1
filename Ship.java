import java.awt.Point;
import java.util.ArrayList;

public class Ship{

    private ArrayList<Point> shipCoordinates = new ArrayList<Point>();
    private int shipSize;
    private int shipPieces;

    public Ship(int size){
        this.shipSize = size;
        this.shipPieces = size;
    }

    public ArrayList<Point> getShipCoordinates(){ return shipCoordinates; }

    public void addCoordinates(int x, int y){ 
        
        shipCoordinates.add(new Point(x,y)); 
    }

    public boolean inline(int newX, int newY){
        if(shipCoordinates.size() == 0)
            return true;

        for(Point shipPiece : shipCoordinates){
            int x = (int)shipPiece.getX();
            int y = (int)shipPiece.getY();
            if(newX == x && (newY == y + 1 || newY == y - 1))
                return true;
            else if(newY == y && (newX == x + 1 || newX == x - 1))
                return true;
        }
        return false;
    }

    public void hit(int x, int y){ shipPieces--; }

    public boolean containsCoordinate(int x, int y){
        if(shipCoordinates.size() == 0)
            return false;

        if(x >= 0 && x <= 7 && y >= 0 && y <= 7){
            for(int i = 0; i < shipCoordinates.size(); i++){
                int cordX = (int)shipCoordinates.get(i).getX();
                int cordY = (int)shipCoordinates.get(i).getY();
                if(cordX == x && cordY == y)
                    return true;
            }
        }
        return false;
    }

    public boolean isDestroyed(){
        if (shipPieces <= 0)
            return true;
        return false;
    }

}
