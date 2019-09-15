public class ShipPlacement{

  public void placement(){
        public  int numRows = 8;
        public  int numCols = 8;
        public  int[][] grid = new int[numRows][numCols];
        System.out.println ("Deploy your Ships!");
         for (int z = 1; z <= 5; z++){
            System.out.println ("Enter X & Y cordinate");
            int x = input.nextInt();
            int y = input.nextInt();
            if (x>=0 && x < numRows && y<=0 && y<= numCols){
              grid[x][y] = 1;
            }
            else if (grid[x][y] == 1){
              System.out.println ("/n This spot has already been taken by a diffrent ship/n")
            }
            else{
              System.out.println ("/n Placement is out of bounds/n")
            }

         }
  }
}
