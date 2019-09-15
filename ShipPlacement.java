import java.util.*;

public class ShipPlacement{

   public  int numRows = 8;
   public  int numCols = 8;
   public  int [][] grid = new int [numRows][numCols];
   public void placement(int size, Ship[] ships){
        Scanner input = new Scanner(System.in);
        System.out.println ("Deploy your Ships!");
        boolean hasSpace = true;

        for (int z = 1; z <= size;){
            System.out.println ("Enter X cordinate");
            int x = input.nextInt();
            System.out.println ("Enter Y cordinate");
            int y = input.nextInt();

            if(x >= 0 && x < numRows && y >= 0 && y < numCols && grid[x][y] == 0){
              grid[x][y] = 1;
            }
            else if (grid[x][y] == 1){
              System.out.println ("This spot has already been taken by a diffrent ship");
            }
            else if (x < 0 || x >= numRows || y < 0 || y >= numCols){
              System.out.println ("Placement is out of bounds");
            }
            z++;

            for(int i = 0; i < numRows; i++){
              for(int j = 0; j < numCols; j++){
                System.out.print(grid[i][j] + " ");
              }
              System.out.println(" ");
            }
         }

  }

}
