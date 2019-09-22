import java.util.Scanner;

class Game{
  /*
  * @ pre none
  *	@ param number of ships
  *	@ post constuctor
  * @ return none
  */
    public Game(int numOfShips){
        Scanner input = new Scanner(System.in);

        GameBoard Player1 = new GameBoard();
        GameBoard Player2 = new GameBoard();


        placeShips(input, Player2, numOfShips);


        //playGame(input, Player1, Player2);

        input.close();
    }

    /*
  	* @ pre user picks number of ships
  	*	@ param users input, the player, number of ships
  	*	@ post the ships are placed
  	* @ return none
  	*/
    public void placeShips(Scanner input, GameBoard player, int numShips){
        for(int i = 1; i <= numShips; i++){
            int shipLength = i;
            Ship tempShip = new Ship(shipLength);



            for(int j = 1; j <= shipLength; j++){

                int shipCol = getColumn(input);
                int shipRow = getRow(input);

                if(tempShip.inline(shipRow, shipCol) && !player.isOccupied(shipRow, shipCol) && !tempShip.containsCoordinate(shipRow, shipCol))
                    tempShip.addCoordinates(shipRow,shipCol);
                else
                    j--;
            }

            player.addShip(tempShip);
        }
    }

    /*
  	* @ pre ships are placed
  	*	@ param users input, the players
  	*	@ post constuctor
  	* @ return none
  	*/
    public void playGame(Scanner input, GameBoard Player1, GameBoard Player2){
        while(!Player1.gameOver() && !Player2.gameOver()){
            int row, col;
            boolean validInput = false;

            System.out.println("");
            System.out.println("Player 1 please fire");
            Player1.printOppBoard();

            validInput = false;
            while(!validInput){
                col = getColumn(input);
                row = getRow(input);
                switch(Player2.fire(row,col)){
                    case "Miss":
                        System.out.println("Miss");
                        Player1.updateOppBoard(row,col,"Miss");
                        validInput = true;
                        break;

                    case "Hit":
                        System.out.println("Hit!");
                        Player1.updateOppBoard(row,col,"Hit");
                        validInput = true;
                        break;

                    case "Sunk":
                        System.out.println("Sunk!");
                        Player1.updateOppBoard(row,col,"Hit");
                        validInput = true;
                        break;

                    case "Error Bounds":
                        System.out.println("Out of bounds");
                        break;

                    case "Error":
                        System.out.println("IDK what happened");
                        break;
                }
            }
            if(Player2.gameOver())
                break;

            System.out.println("");
            System.out.println("Player 2 please fire");
            Player2.printOppBoard();

            validInput = false;
            while(!validInput){
                col = getColumn(input);
                row = getRow(input);
                switch(Player1.fire(row,col)){
                    case "Miss":
                        System.out.println("Miss");
                        Player2.updateOppBoard(row,col,"Miss");
                        validInput = true;
                        break;

                    case "Hit":
                        System.out.println("Hit!");
                        Player2.updateOppBoard(row,col,"Hit");
                        validInput = true;
                        break;

                    case "Sunk":
                        System.out.println("Sunk!");
                        Player2.updateOppBoard(row,col,"Hit");
                        validInput = true;
                        break;

                    case "Error Bounds":
                        System.out.println("Out of bounds");
                        break;

                    case "Error":
                        System.out.println("IDK what happened");
                        break;
                }
            }

            if(Player1.gameOver())
                break;
        }

        if(Player1.gameOver())
            System.out.println("Congrats Player 2, you won");

        if(Player2.gameOver())
            System.out.println("Congrats Player 1, you won");
    }

    /*
  	* @ pre none
  	*	@ param users' imput
  	*	@ post gets the column
  	* @ return returns the ship's column
  	*/
    private int getColumn(Scanner input){
        int shipCol = -1;

        while(shipCol == -1){
            String shipColString;


            shipColString = input.next().toUpperCase();

            switch (shipColString){
                case "A": shipCol = 0; break;
                case "B": shipCol = 1; break;
                case "C": shipCol = 2; break;
                case "D": shipCol = 3; break;
                case "E": shipCol = 4; break;
                case "F": shipCol = 5; break;
                case "G": shipCol = 6; break;
                case "H": shipCol = 7; break;
                default: shipCol = -1; break;
            }
        }
        return shipCol;
    }

    /*
  	* @ pre none
  	*	@ param users' input
  	*	@ post gets the row
  	* @ return returns the row
  	*/
    private int getRow(Scanner input){
        int shipRow = -1;
        while(shipRow == -1){
            int tempRow;

            System.out.print("Ship row(1-8): ");

            while(!input.hasNextInt()){
                System.out.print("Ship row(1-8): ");
                input.next();
            }
            tempRow = input.nextInt();

            if(tempRow > 0 && tempRow <= 8)
                shipRow = tempRow - 1;
        }
        return shipRow;
    }

}
