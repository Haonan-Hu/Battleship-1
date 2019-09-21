import java.util.Scanner;

class Main{

    public static void main(String args[]){
        Scanner input = new Scanner(System.in);
        int numShips = -1;

        System.out.println("Welcome to Battleship!");
        while(numShips > 5 || numShips < 1){
            System.out.print("How many ships would you like to have(1-5): ");
            numShips = input.nextInt();
        }


    }

}
