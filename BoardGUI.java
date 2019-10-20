import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ComboBox;

import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;
import java.util.ArrayList;

import javafx.collections.*;
import javafx.collections.FXCollections;

import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.ColumnConstraints;

import javafx.scene.shape.Rectangle;

import javafx.geometry.Pos;

import javafx.scene.image.ImageView;

import javafx.scene.image.Image;

import javafx.scene.text.Text;

import javafx.scene.text.TextAlignment;

import javafx.geometry.HPos;
import javafx.geometry.VPos;

import javafx.scene.Cursor;
import javafx.scene.ImageCursor;

import javafx.scene.Node;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import java.awt.Point;


import javafx.stage.Popup;

import javafx.scene.layout.TilePane;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;



import java.util.concurrent.ThreadLocalRandom;  //for random numbers


//#megaclass!!!!!


public class BoardGUI implements OverScene, EventHandler<ActionEvent> {

    //Declare variables for the labels and text fields
    private Label a;
    private Button start;
    private String gamemode;
    private GridPane player1, player2, gr;
    private Scene options;
    private int rows = 9, cols = 9;
    private int x, y; //x and y coord of button that is hovered over
    private Image[] ships, shipsInOrder, shipsVert, shipsCopy;

    private int numOfShips;
    private GameBoard player1board, player2board;

    private boolean p1selecting = false, p2selecting = false, horizontal = true, p1turn = false, p2turn = false, initFire = false, popupActive = false;

    private Button[][] board1, board2;  //board1 is the displayed left board, board2 is the displayed right board
    private ImageView[][] board1ref, board2ref; //board1ref is the left (player1's) board that keeps track of ships, hits, and sunks, as images. board2ref does the same for player2
    private int shipSelecting = 0;  //how many ships to be played with
    private Image image;  //gets set to "water.png"
    private Image radarImage; //the radar image
    private Stage s;

    private Label player1name, player2name, status, rotateInstr, nukeTextP1, nukeTextP2;

    private boolean versusAI = false; //set to true if playing again AI
    private int xAI, yAI; //to randomly select coordinates for placing and shooting
    private int randomHorizontal; //used to randomly place ship horizontal or vertical

    private int hitsInaRowP1 = 0; //keeps track of how many hits in a row player1 has done. If it equals 3, p1 earns a nuke
    private int hitsInaRowP2 = 0; //keeps track of how many hits in a row player2 has done. If it equals 3, p2 earns a nuke
    private int hitsInaRowAi = 0; //keeps track of how many hits in a row AI has done. If it equals 3, AI earns a nuke
    private int nukeShotCounter = 0;  //used to call Nuke 9 times, in order to hit a 3x3 area
    private boolean p1canNuke = true;	//so player1 can only nuke one time per game
    private boolean p2canNuke = true; //so player2 can only nuke one time per game
    private boolean shootRandomly = true; //if true, then AI is shooting randomly (Like in easy mode or medium mode when it hasn't yet hit or all ships on screen are sunk)
    private int xFirstHit = 0;  //used in medium AI to keep track of the origin shot
    private int yFirstHit = 0;  //used in medium AI to keep track of the origin shot
    private int xCurrentCoordinate = 0; //used in medium AI to keep track of the current target location
    private int yCurrentCoordinate = 0; //used in medium AI to keep track of the current target location
    private boolean shootUP = true; //medium AI always shoots up first, if possible
    private boolean shootDOWN = false;  //used for medium AI to shoot down
    private boolean shootRIGHT = false; //used for medium AI to right down
    private boolean shootLEFT = false;  //used for medium AI to left down
    private boolean letsShootNow = false; //used for medium AI when it has found the next spot to attempt a shot at

    private Button radar = new Button();  //the radar button

    private boolean useRadar = false; //used to disable or enable the radar at appropriate times

    //since a radar can only be used once per game per player, these variables store the location that the radar was used vvvv
    //this is because we need to recall the location the player placed the radar each turn to update the board to display the hidden ships
    //since the boards are cleared every turn
    private int p1xRadarCoord;
    private int p1yRadarCoord;
    private int p2xRadarCoord;
    private int p2yRadarCoord;
    private boolean p1radarWasUsed = false; //if the radar was used, then p1 cannot use it again
    private boolean p2radarWasUsed = false; //if the radar was used, then p1 cannot use it again


    /*
     * @ pre none
     *	@ param string, stage, font, number of ships, players' names
     *	@ post constuctor
     * @ return none
     */
    public BoardGUI(String gamemode, Stage s, Font f, int numOfShips, String player1name, String player2name) {

        this.gamemode = gamemode;
        this.numOfShips = numOfShips;
        this.s = s;

        this.player1name = new Label(player1name);  //label containing player1's name
        this.player2name = new Label(player2name);  //label containing player2's name

        if(player2name == "AI")
        {
          versusAI = true;  //used to run certain code if player versus the AI
        }

        status = new Label(this.player1name.getText() + " selecting ships\n");    //middle text for selecting ships
        status.setWrapText(true);
        rotateInstr = new Label("Press R to rotate ship" + "\n" + "Radar button dislpays enemy ships (one per player)"+ "\n" + "Three hits in a row = nuke (one per player)");


        this.player1name.setFont(f);
        this.player2name.setFont(f);
        rotateInstr.setFont(f);
        status.setFont(f);

        //images code begin

        image = new Image(getClass().getResourceAsStream("images/water.png"));
        radarImage = new Image(getClass().getResourceAsStream("images/radar.png"),70,70, false, false);
        //https://stackoverflow.com/questions/27894945/how-do-i-resize-an-imageview-image-in-javafx



        ships = new Image[5];
        shipsInOrder = new Image[5];
        shipsVert = new Image[5];

        for (int rep = 0; rep < 5; rep++) {
            ships[rep] = new Image("images/1x" + Integer.toString(rep + 1) + ".png", 50 * (rep + 1), 50, true, true);
        }

        for (int rep = 0; rep < 5; rep++) {
            shipsVert[rep] = new Image("images/" + Integer.toString(rep + 1) + "x1r.png", 50, 50 * (rep + 1), true, true);
        }


        //These are to set each array index to an image depending on the length of the ship
        //The 1x1 just uses the 1x1 image, the 1x3 uses a combination of a front, mid, and 1x1 image
        //When we place our ships, they use the full image for 1x4 or whichever ship
        //Notice that the longer ships look fine when using the mouse to move them around the board,
        //But when you place the ship, it looks more broken up because the images occupy the grid individually
        shipsInOrder[0] = new Image("images/front.png", 50, 50, true, true);
        shipsInOrder[1] = new Image("images/mid.png", 50, 50, true, true);
        shipsInOrder[2] = new Image("images/mid.png", 50, 50, true, true);
        shipsInOrder[3] = new Image("images/mid.png", 50, 50, true, true);
        shipsInOrder[4] = new Image("images/1x1.png", 50, 50, true, true);


        shipsCopy = shipsInOrder.clone();

        //image code end


        //Define their placement in the grid

        gr = new GridPane();

        player1 = new GridPane();

        player2 = new GridPane();

        //make columns for each board

        for (int x = 0; x < cols; x++) {
            ColumnConstraints c = new ColumnConstraints();
            c.setPercentWidth(100.0 / cols);
            player1.getColumnConstraints().add(c);
            player2.getColumnConstraints().add(c);
        }

        //rows for each board

        for (int y = 0; y < rows; y++) {
            RowConstraints r = new RowConstraints();
            r.setPercentHeight(100.0 / rows);
            player1.getRowConstraints().add(r);
            player2.getRowConstraints().add(r);
        }

        String str = "ABCDEFGH";
        String str2 = "12345678";

        board1 = new Button[rows - 1][cols - 1];
        board2 = new Button[rows - 1][cols - 1];

        board1ref = new ImageView[rows - 1][cols - 1];
        board2ref = new ImageView[rows - 1][cols - 1];


        // Button radar = new Button();
        // radar.setText("RADAR 1 Per Player");

        radar.setOnAction(e -> {
          useRadar = true;
        });

        gr.getChildren().add(radar);
        gr.setHalignment(radar, HPos.CENTER); //positioning the radar
        radar.setTranslateX(375); //overiding to position the radar image
        radar.setTranslateY(20);

        radar.setDisable(true);  //when true, you cannot click the radar. We default to this so the users cannot press the radar button during setup

        radar.setGraphic(new ImageView(radarImage));  //sets the image of the radar button


        for (int c = 0; c < cols - 1; c++) {
            for (int r = 0; r < rows - 1; r++) {

                board1[r][c] = new Button();
                board2[r][c] = new Button();

                board1[r][c].setShape(new Rectangle(50, 50));
                board2[r][c].setShape(new Rectangle(50, 50));

                board1[r][c].setGraphic(new ImageView(image));
                board2[r][c].setGraphic(new ImageView(image));

                board1ref[r][c] = new ImageView(image);
                board2ref[r][c] = new ImageView(image);


                board1[r][c].setMinSize(50, 50);
                board2[r][c].setMinSize(50, 50);

                board1[r][c].setMaxSize(50, 50);
                board2[r][c].setMaxSize(50, 50);

                board1[r][c].setOnAction(this);
                board2[r][c].setOnAction(this);


                player1.add(board1[r][c], c + 1, r + 1);
                player2.add(board2[r][c], c + 1, r + 1);

                player1.setHalignment(board1[r][c], HPos.CENTER);
                player2.setHalignment(board2[r][c], HPos.CENTER);

                Text t_row1 = new Text(str2.substring(r, r + 1));
                Text t_row2 = new Text(str2.substring(r, r + 1));

                player1.add(t_row1, 0, r + 1);
                player2.add(t_row2, 0, r + 1);

                player1.setHalignment(t_row1, HPos.CENTER);
                player2.setHalignment(t_row2, HPos.CENTER);

            }
            //letters above columns
            Text t_col1 = new Text(str.substring(c, c + 1));
            Text t_col2 = new Text(str.substring(c, c + 1));

            player1.add(t_col1, c + 1, 0);
            player2.add(t_col2, c + 1, 0);

            player1.setHalignment(t_col1, HPos.CENTER);
            player2.setHalignment(t_col2, HPos.CENTER);
        }


        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(46);
        gr.getColumnConstraints().add(c1);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(12);
        gr.getColumnConstraints().add(c2);
        ColumnConstraints c3 = new ColumnConstraints();
        c3.setPercentWidth(46);
        gr.getColumnConstraints().add(c3);


        RowConstraints r1 = new RowConstraints();
        r1.setPercentHeight(10);
        RowConstraints r2 = new RowConstraints();
        r2.setPercentHeight(80);
        RowConstraints r3 = new RowConstraints();
        r3.setPercentHeight(10);

        player1.setHgap(0);
        player2.setHgap(0);

        player1.setVgap(0);
        player2.setVgap(0);


        gr.getRowConstraints().add(r1);

        gr.setConstraints(player1, 0, 1);
        gr.setConstraints(player2, 2, 1);

        gr.getChildren().add(player1);
        gr.getChildren().add(player2);

        gr.add(this.player1name, 0, 2);
        gr.setHalignment(this.player1name, HPos.CENTER);
        gr.add(this.player2name, 2, 2);
        gr.setHalignment(this.player2name, HPos.CENTER);
        gr.add(status, 1, 2);
        gr.setHalignment(status, HPos.RIGHT);
        gr.add(rotateInstr, 0, 0);
        gr.setHalignment(rotateInstr, HPos.LEFT);

        //HERE WE ADD THE NUKE TEXT UNDER PLAYER1
        nukeTextP1 = new Label();
        nukeTextP1.setWrapText(true);
        nukeTextP1.setTextAlignment(TextAlignment.CENTER);
        nukeTextP1.setFont(Font.font("Verdana", 30));
        nukeTextP1.setTextFill(Color.web("#E50303"));

        gr.add(nukeTextP1, 0,3);
        gr.setHalignment(nukeTextP1, HPos.CENTER);

        nukeTextP2 = new Label();
        nukeTextP2.setWrapText(true);
        nukeTextP2.setTextAlignment(TextAlignment.CENTER);
        nukeTextP2.setFont(Font.font("Verdana", 30));
        nukeTextP2.setTextFill(Color.web("#E50303"));

        gr.add(nukeTextP2, 2,3);
        gr.setHalignment(nukeTextP2, HPos.CENTER);


        gr.setStyle("-fx-background-color: lightslategray;");
        options = new Scene(gr, 1000, 800);

        //two player boards
        player1board = new GameBoard();
        player2board = new GameBoard();


        p1selecting = true;
        flipScreen("");

    }


    /*
     * @ pre none
     *	@ param Player, x and y coordinate of the grid, length of the ship
     *	@ post places the ships
     * @ return none
     */
    //from jace's Main.java
    public void placeShips(GameBoard player, int x, int y, int shipLength) {
        Ship tempShip = new Ship(shipLength);

        if (!player.isOccupied(x, y, shipLength, horizontal)) {
            for (int rep = 0; rep < shipLength; rep++) {

                if (horizontal)
                    tempShip.addCoordinates(x + rep, y);
                else if (!horizontal)
                    tempShip.addCoordinates(x, y + rep);

            }
        }

        player.addShip(tempShip);

    }


    /*
     * @ pre none
     *	@ param a message
     *	@ post flips the screen
     * @ return none
     */
    public void flipScreen(String messageToPlayer) {
        //this changes the screen to the other players view
        //i.e. blocks the ship locations from the other persons pov
        //also handles the popup menu in between

        //https://www.geeksforgeeks.org/javafx-popup-class/
        //popup code

        //clear boards inbetween turns!!!!
        for (int y = 0; y < 8; y++) {       //double for loop for each location in the board
            for (int x = 0; x < 8; x++) {
                board1[y][x].setGraphic(new ImageView(image));  //set the images of board1 to all water images
                board2[y][x].setGraphic(new ImageView(image));  //set the images of board2 to all water images
            }
        }

        Label close = new Label();    //creating a label for the popups for each player
        Stage stage = new Stage();    //create a new stage for the popups to occur on

        if(versusAI)    //if versusAI is chosen at the menu screen
        {
          if (p1turn)   //if it's player1's turn
          {
            close.setText(messageToPlayer + this.player1name.getText() + "'s turn in 2 seconds\n");   //set the text of the pop up to it's player1's turn
          }
          else if (p2turn)    //if it's player2's turn
          {
            close.setText(messageToPlayer);   //AI doesn't get a popup for their turn
          }
          else if (p1selecting)   //if player1 is selecting ships
          {
              close.setText(this.player1name.getText() + " selecting ships in 2 seconds\n");  //change pop up to say player1 is selecting ships
          }
          else if (p2selecting)   //if player2 is selecting ships
          {
            close.setText(this.player2name.getText() + " selecting ships in 2 seconds");  //change pop up to say player 2 is selecting ships
          }
          if (messageToPlayer.contains("wins"))   //if the popup reads "win"
          {
            close.setText(messageToPlayer + "Closing game in 2 seconds");   //set the pop up text to closing game in 2 seconds
          }
        }
        else  //if player vs player is chosen at the start menu
        {
          if (p1turn)   //if it's player1's turn
          {
            close.setText(messageToPlayer + this.player1name.getText() + "'s turn in 5 seconds\n");   //change pop up to say player1's turn
          }
          else if (p2turn)    //if it's player2's turn
          {
            close.setText(messageToPlayer + this.player2name.getText() + "'s turn in 5 seconds");   //change pop up to say player2's turn
          }
          else if (p1selecting)   //if player1 is selecting ships
          {
            close.setText(this.player1name.getText() + " selecting ships in 5 seconds\n");  //change pop up to say player1's selecting ships
          }
          else if (p1selecting) {
              close.setText(this.player1name.getText() + " selecting ships in 5 seconds\n");
          }
          else if (p2selecting)   //if player2 is selecting ships
          {
            close.setText(this.player2name.getText() + " selecting ships in 5 seconds");  //change pop up to say selecting player2's selecting ships
          }
          if (messageToPlayer.contains("wins"))   //if pop up contains "wins"
          {
            close.setText(messageToPlayer + "Closing game in 5 seconds");   //change pop up to say closing game
          }
        }



        Popup pop = new Popup();              //creating a basic popup
        TilePane tilepane = new TilePane();   //creating a tilepane to hold that popup
        tilepane.getChildren().add(close);    //adding the popup, close to a tilepane

        // create a scene
        Scene scene = new Scene(tilepane, 250, 100);  //THIS IS WHERE THE ACTUAL POPUP IS CREATED!!!!

        stage.setScene(scene);

        if(versusAI && p2turn || p2selecting)   //if the AI active and is its turn
        {

          stage.setX(545);      //set the x and y coordinate of the pop up to below the player 1 pop up
          stage.setY(400);

          stage.setAlwaysOnTop(true);   //the popup always appears on top of the other scenes
          stage.show();                 //show the popup
          popupActive = true;           //popup is now active
        }
        else    //if we aren't playering against AI
        {
          stage.setX(545);    //set the x and y coordinate of player2's pop up to be in line with player1's
          stage.setY(200);

          stage.setAlwaysOnTop(true);   //the popup always appears on top of the other scenes
          stage.show();                 //show the popup
          popupActive = true;           //popup is now active
        }

        //https://stackoverflow.com/questions/26454149/make-javafx-wait-and-continue-with-code/26454506
        //sleep thread code

        Task<Void> sleeper = new Task<Void>() {   //creating a new task called sleeper to pause the game during popups
            @Override
            protected Void call() throws Exception {    //set the function as protected
              try{                    //try catch block for the sleeper
                    if(versusAI)      //if versusAI was chosen at the menu
                    {
                      Thread.sleep(2000);   //set the sleep timer to 2 seconds
                    }
                    else    //if player vs player
                    {
                      Thread.sleep(5000);   //set the sleep timer to 5 seconds
                    }
                }
              catch (InterruptedException e)
              {
              }
              return null;    //returns null, as the sleeper function only pauses the timer and doesn't return anything
            }
        };

        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {   //function for when the sleeper returns
            @Override
            public void handle(WorkerStateEvent event) {
                if (close.getText().contains("wins"))       //if the text in a popup reads "wins" end the program
                {
                  System.exit(0);   //once a player wins, exit the program
                }
                pop.hide();          //this line and the two following it happen after every turn
                stage.hide();        //hide the stage the popup is on
                popupActive = false;    //popup is no longer active

                if (p1turn) {   //if it's player1's turn

                    int[][] oppBoard = player1board.getOppBoard();    //create a new 2D board array for player1's view of player2's board
                    for (int y = 0; y < 8; y++) {       //for loop to set graphics of player1's view of player2's board
                        for (int x = 0; x < 8; x++) {

                            board1[y][x].setGraphic(board1ref[y][x]);   //the left board gets set to player1's view

                            if (oppBoard[y][x] == 0)       //if the oppboard is a 0
                                board2[y][x].setGraphic(new ImageView(image));    //set the graphic to water
                            else if (oppBoard[y][x] == 1)   //if the oppbaord is a 1
                                board2[y][x].setGraphic(new ImageView(new Image("images/miss.png", 50, 50, true, true)));   //set the image to the miss icon
                            else if (oppBoard[y][x] == 2)   //if the oppbaord is a 2
                                board2[y][x].setGraphic(new ImageView(new Image("images/hit.png", 50, 50, true, true)));    //set the image to the hit icon
                            else if (oppBoard[y][x] == 3)   //if the oppbaord is a 3
                                board2[y][x].setGraphic(new ImageView(new Image("images/sunk.png", 50, 50, true, true)));   //set the image to the sunk icon
                        }
                    }
                    if(p1radarWasUsed)  //if player 1 has used his one-time radar, then each turn we have to call radar with the permanent coordinates of
                                        //p1xRadarCoord,p1yRadarCoord, so that way it re-updates the '+' sign of the radar. If we didn't do this, then, because
                                        //every turn the boards are cleared, then after the turn the radar is used, the ships disovered by the radar would not show up
                    {
                      radar(p1xRadarCoord,p1yRadarCoord);   //call radar, passing in the origin given by where the player clicked on the opponent's board
                    }
                    else    //if radar was not used this turn
                    {
                      radar.setDisable(false); //enables player 1's radar if not yet used
                    }

                }

                else if (p2turn)    //if it's player2's turn
                {

                    int[][] oppBoard = player2board.getOppBoard();    //create a new 2D board array for player2's view of player1's board
                    for (int y = 0; y < 8; y++) {     //for loop to set graphics of player2's view of player1's board
                        for (int x = 0; x < 8; x++) {

                            board2[y][x].setGraphic(board2ref[y][x]);   //the right board gets set to player2's view

                            if (oppBoard[y][x] == 0)    //if the oppboard is a 0
                                board1[y][x].setGraphic(new ImageView(image));    //set the graphic to water
                            else if (oppBoard[y][x] == 1)  //if the oppboard is a 1
                                board1[y][x].setGraphic(new ImageView(new Image("images/miss.png", 50, 50, true, true)));   //set the image to the miss icon
                            else if (oppBoard[y][x] == 2)  //if the oppboard is a 2
                                board1[y][x].setGraphic(new ImageView(new Image("images/hit.png", 50, 50, true, true)));    //set the image to the hit icon
                            else if (oppBoard[y][x] == 3)  //if the oppboard is a 3
                                board1[y][x].setGraphic(new ImageView(new Image("images/sunk.png", 50, 50, true, true)));   //set the image to the sunk icon
                        }
                    }
                    if(p2radarWasUsed)//if player 2 has used his one-time radar, then each turn we have to call radar with the permanent coordinates of
                                        //p1xRadarCoord,p1yRadarCoord, so that way it re-updates the '+' sign of the radar. If we didn't do this, then, because
                                        //every turn the boards are cleared, then after the turn the radar is used, the ships disovered by the radar would not show up
                    {
                      radar(p2xRadarCoord,p2yRadarCoord);   //call radar, passing in the origin given by where the player clicked on the opponent's board
                    }
                    else    //if radar was not used this turn
                    {
                      radar.setDisable(false);  //enables player 2's radar if not yet used
                    }

                }
                if (p2selecting || p1selecting)                   //if either player is selecting
                    options.setCursor(new ImageCursor(ships[0],   //set the cursor to the ships image they are currently on
                            ships[0].getWidth() / 2,
                            ships[0].getHeight() / 2));
            }
        });

        new Thread(sleeper).start();      //the popup is called and starts the sleeper
    }   //end setOnSucceeded


    /*
     * @ pre none
     *	@ param none
     *	@ post gets the next scene
     * @ return returns next scene
     */

    @Override
    public Scene getScene() {
        return options;
    }


    //for AI choosing ship placement. Needs to be outside of EventHandler since AI isn't actually pressing a button
    public void AIturn()
    {

      for(int i = shipSelecting;i<numOfShips;i++)
      {
      //AI PLAYER
      if (p2selecting && shipSelecting < 5 && versusAI) {

        yAI = 9;  //reinitialize to value outside of range
        xAI = 9;  //reinitialize to value outside of range
        //run until random values are within bounds and also do not result in collision
        do{
          yAI = ThreadLocalRandom.current().nextInt(0, 8);
          xAI = ThreadLocalRandom.current().nextInt(0, 8);
          //works cited https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
          randomHorizontal = ThreadLocalRandom.current().nextInt(0,2); //randomly chooses 0 or 1, hence false or true.
          if(randomHorizontal == 0)
          {
            horizontal = false;
          }
          else
          {
            horizontal = true;
          }
        }while(!(shipSelecting < numOfShips && !player2board.isOccupied(xAI, yAI, shipSelecting + 1, horizontal)));

        int x = xAI;
        int y = yAI;

                      placeShips(player2board, x, y, shipSelecting + 1);  //places the ships at the selected location

                      if (horizontal)
                      {

                          for (int rep = 0; rep < shipSelecting + 1; rep++) //places components of the ship based on which ship length is to be placed at this time
                          {
                              if (rep == shipSelecting)
                              {
                                  shipsCopy[1] = shipsInOrder[4];
                                  board2[y][x + rep].setGraphic(new ImageView(shipsInOrder[4]));
                                  board2ref[y][x + rep] = new ImageView(shipsInOrder[4]);
                              } else
                              {

                                  board2[y][x + rep].setGraphic(new ImageView(shipsInOrder[rep]));
                                  board2ref[y][x + rep] = new ImageView(shipsInOrder[rep]);
                              }
                          }
                      } else if (!horizontal)
                      {
                          for (int rep = 0; rep < shipSelecting + 1; rep++)
                          {

                              if (rep == shipSelecting) {
                                  shipsCopy[1] = shipsInOrder[4];
                                  board2[y + rep][x].setGraphic(new ImageView(shipsInOrder[4]));
                                  board2[y + rep][x].getGraphic().setRotate(90);

                                  board2ref[y + rep][x] = (new ImageView(shipsInOrder[4]));
                                  board2ref[y + rep][x].setRotate(90);

                              } else {
                                  board2[y + rep][x].setGraphic(new ImageView(shipsInOrder[rep]));
                                  board2[y + rep][x].getGraphic().setRotate(90);

                                  board2ref[y + rep][x] = (new ImageView(shipsInOrder[rep]));
                                  board2ref[y + rep][x].setRotate(90);
                              }

                          }

                      }

                      shipSelecting++;

                      if (shipSelecting < numOfShips) {

                          if (horizontal) //sets the cursor image to the image of the current ship to be placed horizontally
                          {
                              options.setCursor(new ImageCursor(ships[shipSelecting],
                                      ships[shipSelecting].getWidth() / (2 * (shipSelecting + 1)),
                                      ships[shipSelecting].getHeight() / (2)));
                          } else if (!horizontal) //sets the cursor image to the image of the current ship to be placed vertically
                          {
                              options.setCursor(new ImageCursor(shipsVert[shipSelecting],
                                      shipsVert[shipSelecting].getWidth() / (2),
                                      shipsVert[shipSelecting].getHeight() / (2 * (shipSelecting + 1))));
                          }

                      } else if (shipSelecting == numOfShips) //Once done placing the ships
                      {
                          shipSelecting = 0;
                          p2selecting = false;  //no longer selecting, so set to false
                          p1selecting = false;  //no longer selecting, so set to false
                          options.setCursor(Cursor.DEFAULT);
                          p1turn = true;  //now it will begin with p1's turn to shoot


                          status.setText(player1name.getText() + "'s Turn\n");
                          flipScreen("");

                      }
                      else
                      {
                        // System.out.println("else statement for whem shipSelecting != numOfShip");
                      }

              }

          }

  return;
}

//when we want the AI to shoot. Needs to be outside of EventHandler since AI isn't actually pressing a button
public void AIshoot()
{

  if(p2turn && versusAI) {

      yAI = 9;  //reinitialize to value outside of range
      xAI = 9;  //reinitialize to value outside of range
      //run until random values are within bounds and also do not result in collision

      // randomly shooting
      if(gamemode == "Easy")
      {
        do{
          yAI = ThreadLocalRandom.current().nextInt(0, 8);  //setting random location to shoot as long as it is a valid location
          xAI = ThreadLocalRandom.current().nextInt(0, 8);
        }while(player2board.getOppBoard()[yAI][xAI] != 0);
      }

      if(gamemode == "Medium")
      {

        shootRandomly = true; //reinitialize to true

        for(int xvalue=0;xvalue<cols-1;xvalue++)  //scanning through the board to see if there is a HIT ship. If not, we shoot randomly. Else, use logic
        {
          for(int yvalue=0;yvalue<rows-1;yvalue++)
          {
            if(player2board.getOppBoard()[yvalue][xvalue] == 2)//if there is a HIT ship on the board, we do not want to shoot randomly.
                                                               //We want to use logic to shoot adjacently
            {
              shootRandomly = false;
            }
          }
        }

        if(shootRandomly) //if there were no HITs on the screen, that means we can shoot randomly until there is a HIT
        {
          shootUP = true; //since we are back to shooting randomly, we reinitialize this to true so that next HIT we start by shooting vertically from it
          shootLEFT = false; //need to reinitialize this to false since leftmost would be the last location of a ship to be shot at, so
          shootDOWN = false;
          shootRIGHT = false;                  //if the leftmost part of a ship is destroyed and there are no more HITs, then the else statement in the left section won't run and set itself back to false

          do{
            yAI = ThreadLocalRandom.current().nextInt(0, 8);
            xAI = ThreadLocalRandom.current().nextInt(0, 8);
          }while(player2board.getOppBoard()[yAI][xAI] != 0);
          //even though these locations might be a miss, we set them to FirstHit because if it results to a hit,
          //then the else statement runs the next turn, and therefore they are the location of the first hit that go around
          xFirstHit = xAI;
          yFirstHit = yAI;
          xCurrentCoordinate = xFirstHit; //initialize xCurrentCoordinate to the first hit
          yCurrentCoordinate = yFirstHit; //initialize xCurrentCoordinate to the first hit

        }
        else
        {
          letsShootNow = false; //reinitialize it to false so that while-loop runs

          xCurrentCoordinate = xFirstHit;
          yCurrentCoordinate = yFirstHit;

          while(!letsShootNow)
          {

            //if the origin is a sunk ship spot then change origin to any hit spot
            if(player2board.getOppBoard()[yFirstHit][xFirstHit] == 3)
            {
              for(int xvalue=0;xvalue<cols-1;xvalue++)
              {
                for(int yvalue=0;yvalue<rows-1;yvalue++)
                {
                  if(player2board.getOppBoard()[yvalue][xvalue] == 2)
                  {
                    shootUP = true; //reinitialize to shoot up next
                    shootDOWN = false;
                    shootLEFT = false;
                    shootRIGHT = false;
                    yFirstHit = yvalue; //setting the new origin
                    xFirstHit = xvalue;
                  }
                }
              }
            }

            if(shootUP)
            {
              //SHOOTING UP
              if(yCurrentCoordinate-1 < 0)  //if up one is out of bounds, shoot down instead
              {
                shootUP = false;  //we will now shoot down next time
                shootRIGHT = false;
                shootLEFT = false;
                shootDOWN = true;

                xCurrentCoordinate = xFirstHit; //we exhausted all up moves, so go back to origin and try going right and left
                yCurrentCoordinate = yFirstHit;
              }
              else if(player2board.getOppBoard()[yCurrentCoordinate-1][xCurrentCoordinate] == 2) //traverse vertically UP until to a location that isn't a HIT
              {
                yCurrentCoordinate--;
              }
              else if((player2board.getOppBoard()[yCurrentCoordinate][xCurrentCoordinate] != 1) && (player2board.getOppBoard()[yCurrentCoordinate-1][xCurrentCoordinate] == 0)) //if the current coordinate is not a miss AND if the next one up after going up following all the HITs is a blank spot, then we should shoot at it
              {
                yCurrentCoordinate--;
                letsShootNow = true;  //kick out of while-loop
              }
              else
              {

                xCurrentCoordinate = xFirstHit; //we exhausted all up moves, so go back to origin and try going right and left
                yCurrentCoordinate = yFirstHit;

                shootUP = false;  //we will now shoot down next time
                shootRIGHT = false;
                shootLEFT = false;
                shootDOWN = true;
              }
            }
            else if(shootDOWN)
            {
              //SHOOTING DOWN
              if(yCurrentCoordinate+1 >= 8) //if down one is out of bounds, shoot right instead
              {
                shootUP = false;
                shootRIGHT = true;
                shootLEFT = false;
                shootDOWN = false;

                xCurrentCoordinate = xFirstHit; //go back to the origin
                yCurrentCoordinate = yFirstHit;
              }
              else if(player2board.getOppBoard()[yCurrentCoordinate+1][xCurrentCoordinate] == 2)  //traverse vertically DOWN until to a location that isn't a HIT
              {
                yCurrentCoordinate++; //starts traversing down now
              }

              else if((player2board.getOppBoard()[yCurrentCoordinate][xCurrentCoordinate] != 1) && player2board.getOppBoard()[yCurrentCoordinate+1][xCurrentCoordinate] == 0) ////if the current coordinate is not a miss AND if the next one down after going down following all the HITs is a blank spot, then we should shoot at it
              {
                yCurrentCoordinate++;
                letsShootNow = true;  //kick out of while-loop
              }
              else
              {
                xCurrentCoordinate = xFirstHit; //we exhausted all up moves, so go back to origin and try going right and left
                yCurrentCoordinate = yFirstHit;
                shootDOWN = false;
                shootRIGHT = true; //we will now shoot right next time
                shootUP = false;
                shootLEFT = false;
              }
            }
            else if(shootRIGHT)
            {
              //SHOOTING RIGHT

              if(xCurrentCoordinate+1 >= 8) //if right once is out of bounds, shoot left instead
              {
                shootUP = false;
                shootRIGHT = false;
                shootLEFT = true;
                shootDOWN = false;

                xCurrentCoordinate = xFirstHit; //go back to the origin
                yCurrentCoordinate = yFirstHit;
              }
              else if(player2board.getOppBoard()[yCurrentCoordinate][xCurrentCoordinate+1] == 2)  //traverse horizontally RIGHT until to a location that isn't a HIT
              {
                xCurrentCoordinate++; //starts traversing down now
              }
              else if((player2board.getOppBoard()[yCurrentCoordinate][xCurrentCoordinate] != 1) &&player2board.getOppBoard()[yCurrentCoordinate][xCurrentCoordinate+1] == 0) ////if the current coordinate is not a miss AND if the next one right after going right following all the HITs is a blank spot, then we should shoot at it
              {
                xCurrentCoordinate++;
                letsShootNow = true;  //kick out of while-loop
              }
              else
              {
                xCurrentCoordinate = xFirstHit; //we exhausted all right moves, so go back to origin and try going right and left
                yCurrentCoordinate = yFirstHit;

                shootRIGHT = false;
                shootLEFT = true; //we will now shoot left next time
                shootUP = false;
                shootDOWN = false;
              }
            }
            else if(shootLEFT)
            {
              //SHOOTING LEFT

              if(xCurrentCoordinate+1 < 0)
              {
                shootUP = true;
                shootRIGHT = false;
                shootLEFT = false;
                shootDOWN = false;

                xCurrentCoordinate = xFirstHit; //go back to the origin
                yCurrentCoordinate = yFirstHit;
              }
              else if(player2board.getOppBoard()[yCurrentCoordinate][xCurrentCoordinate-1] == 2)  //traverse horizontally LEFT until to a location that isn't a HIT
              {
                xCurrentCoordinate--; //starts traversing down now
              }
              else if((player2board.getOppBoard()[yCurrentCoordinate][xCurrentCoordinate] != 1) && player2board.getOppBoard()[yCurrentCoordinate][xCurrentCoordinate-1] == 0) //if the current coordinate is not a miss AND if the next one left after going left following all the HITs is a blank spot, then we should shoot at it
              {
                xCurrentCoordinate--;
                letsShootNow = true;  //kick out of while-loop
              }
              else
              {
                xCurrentCoordinate = xFirstHit; //we exhausted all left moves, so go back to origin and try going right and left
                yCurrentCoordinate = yFirstHit;

                shootLEFT = false;
                shootUP = true; //we will now shoot UP next time
                shootRIGHT = false;
                shootDOWN = false;
              }
            }
          }

          xAI = xCurrentCoordinate;
          yAI = yCurrentCoordinate;
        }
        //end Medium shooting
      }

      if(gamemode == "Hard")
      {
          for(int xvalue=0;xvalue<cols-1;xvalue++)
          {
            for(int yvalue=0;yvalue<rows-1;yvalue++)
            {

              xAI = xvalue;
              yAI = yvalue;

              if(player1board.getBoard()[yAI][xAI] == 1 && player2board.getOppBoard()[yAI][xAI] == 0)
              {
                break;  //breaks out of this for loop if yAI and xAI represent a location that is a ship and hasn't been shot at yet
              }
            }
            if(player1board.getBoard()[yAI][xAI] == 1 && player2board.getOppBoard()[yAI][xAI] == 0)
            {
              break;  //breaks out of the final for loop if yAI and xAI represent a location that is a ship and hasn't been shot at yet
                      //need two breaks so that we can break out of the double for loop.
            }
          }
      }

      x = xAI;
      y = yAI;

            if(hitsInaRowP2 == 3 && p2canNuke)  //checks if AI has earned a nuke. If so, the the shot will be the Nuke shot
            {
              nukeExecute(x,y);
              hitsInaRowP2 = 0;
            }
            else
            {
                  String str = player1board.fire(x, y); //sets str to either "Miss" "Hit" or "Sunk"
                  if (str == "Miss")  //when Miss, set that location to the Miss image
                  {
                      hitsInaRowP2 = 0; //resets the combo counter for earning a Nuke

                      board1[y][x].setGraphic(new ImageView(new Image("images/miss.png", 50, 50, true, true)));

                      board1ref[y][x] = (new ImageView(new Image("images/miss.png", 50, 50, true, true)));

                      player2board.updateOppBoard(x, y, str);
                      p1turn = true;
                      p2turn = false;


                      if (player1board.gameOver()) {
                          flipScreen(player2name.getText() + " wins!\n");
                      } else {
                          status.setText(player1name.getText() + "'s Turn\n");
                          flipScreen("AI MISSED YOUR SHIPS!\n");
                      }

                      //you missed
                      //add transition screen code here

                  } else if (str == "Hit")  //when Hit, set that location to the Hit image
                  {
                      hitsInaRowP2++; //updates the combo counter for earning a Nuke

                      board1[y][x].setGraphic(new ImageView(new Image("images/hit.png", 50, 50, true, true)));

                      board1ref[y][x] = (new ImageView(new Image("images/hit.png", 50, 50, true, true)));

                      player2board.updateOppBoard(x, y, str);
                      p1turn = true;
                      p2turn = false;
                      if (player1board.gameOver()) {
                          flipScreen(player2name.getText() + " wins!\n");
                      } else {
                          status.setText(player1name.getText() + "'s Turn\n");
                          flipScreen("AI HIT ONE OF YOUR SHIPS!\n");
                      }

                      //you hit my battleship
                      //add transition screen code here
                  } else if (str == "Sunk") //when Sunk, set that location to the Sunk image
                  {
                      //need to change every texture of the ship
                      hitsInaRowP2++;
                      Ship s = player1board.shipAt(x, y);
                      for (Point p : (s.getShipCoordinates())) {
                          board1[(int) p.getY()][(int) p.getX()].setGraphic(new ImageView(new Image("images/sunk.png", 50, 50, true, true)));

                          board1ref[(int) p.getY()][(int) p.getX()] = (new ImageView(new Image("images/sunk.png", 50, 50, true, true)));
                          player2board.updateOppBoard((int) p.getX(), (int) p.getY(), str);

                      }

                      p1turn = true;
                      p2turn = false;

                      if (player1board.gameOver()) {
                          flipScreen(player2name.getText() + " wins!\n");
                      } else {
                          status.setText(player1name.getText() + "'s Turn\n");
                          flipScreen("AI SUNK YOUR BATTLESHIP!\n");
                      }

                      //you sunk my battleship
                      //add transition screen code here
                  }

              }
  }

  if(hitsInaRowP2 == 3 && p2canNuke)    //TEXT FOR IF P1 HAS A NUKE
  {
    nukeTextP2.setText("YOU EARNED A NUKE! LAUNCH IT NOW!");
  }
  else
  {
    nukeTextP2.setText(""); //set text back to nothing once nuke is used
  }

}

    /*
     * @ pre none
     *	@ param action event / button pressed
     *	@ post every time you click a button or press a key it reacts
     * @ return none
     */
    @Override
    public void handle(ActionEvent e) {
        //https://www.programcreek.com/java-api-examples/?api=javafx.scene.input.KeyEvent
        //for keyPressedEvent
        options.getRoot().setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {

                if (ke.getCode() == KeyCode.R && (p1selecting || p2selecting)) {

                    if (horizontal) {
                        options.setCursor(new ImageCursor(shipsVert[shipSelecting],
                                shipsVert[shipSelecting].getHeight() / (2 * (shipSelecting + 1)),
                                shipsVert[shipSelecting].getWidth() / (2)));
                    } else if (!horizontal) {
                        options.setCursor(new ImageCursor(ships[shipSelecting],
                                ships[shipSelecting].getHeight() / (2),
                                ships[shipSelecting].getWidth() / (2 * (shipSelecting + 1))));
                    }

                    horizontal = !horizontal;

                }
            }
        });

        if(useRadar)  //if the radar button was clicked
        {

          if(p1turn)
          {
            for (int x = 0; x < cols - 1; x++)
            {
                for (int y = 0; y < rows - 1; y++)
                {
                    if (e.getSource() == board2[y][x])
                    {
                      radar(x,y);
                    }
                }

              }
              p1radarWasUsed = true;
              radar.setDisable(true); //disable player 1 from using the radar
          }
          else
          {
            for (int x = 0; x < cols - 1; x++)
            {
                for (int y = 0; y < rows - 1; y++)
                {
                    if (e.getSource() == board1[y][x])
                    {
                      radar(x,y);
                    }
                }

              }
              p2radarWasUsed = true;
              radar.setDisable(true); //disable player 2 from using the radar
          }

            useRadar = false;
            return;
          }

        if (p1selecting && shipSelecting < 5 && !popupActive) {

            for (int x = 0; x < cols - 1; x++) {
                for (int y = 0; y < rows - 1; y++) {
                    if (e.getSource() == board1[y][x] && shipSelecting < numOfShips && !player1board.isOccupied(x, y, shipSelecting + 1, horizontal)) {

                        placeShips(player1board, x, y, shipSelecting + 1);

                        if (horizontal) {

                            for (int rep = 0; rep < shipSelecting + 1; rep++) {

                                if (rep == shipSelecting) {
                                    shipsCopy[1] = shipsInOrder[4];
                                    board1[y][x + rep].setGraphic(new ImageView(shipsInOrder[4]));
                                    board1ref[y][x + rep] = new ImageView(shipsInOrder[4]);

                                } else {
                                    board1[y][x + rep].setGraphic(new ImageView(shipsInOrder[rep]));
                                    board1ref[y][x + rep] = new ImageView(shipsInOrder[rep]);
                                }

                            }
                        } else if (!horizontal) {
                            for (int rep = 0; rep < shipSelecting + 1; rep++) {

                                if (rep == shipSelecting) {
                                    shipsCopy[1] = shipsInOrder[4];
                                    board1[y + rep][x].setGraphic(new ImageView(shipsInOrder[4]));
                                    board1[y + rep][x].getGraphic().setRotate(90);

                                    board1ref[y + rep][x] = (new ImageView(shipsInOrder[4]));
                                    board1ref[y + rep][x].setRotate(90);
                                } else {
                                    board1[y + rep][x].setGraphic(new ImageView(shipsInOrder[rep]));
                                    board1[y + rep][x].getGraphic().setRotate(90);

                                    board1ref[y + rep][x] = (new ImageView(shipsInOrder[rep]));
                                    board1ref[y + rep][x].setRotate(90);
                                }

                            }

                        }

                        shipSelecting++;

                        if (shipSelecting < numOfShips) {

                            if (horizontal) {
                                options.setCursor(new ImageCursor(ships[shipSelecting],
                                        ships[shipSelecting].getWidth() / (2 * (shipSelecting + 1)),
                                        ships[shipSelecting].getHeight() / (2)));
                            } else if (!horizontal) {
                                options.setCursor(new ImageCursor(shipsVert[shipSelecting],
                                        shipsVert[shipSelecting].getWidth() / (2),
                                        shipsVert[shipSelecting].getHeight() / (2 * (shipSelecting + 1))));
                            }

                        } else if (shipSelecting == numOfShips) {
                            shipSelecting = 0;
                            p1selecting = false;
                            p2selecting = true;
                            status.setText(player2name.getText() + " selecting ships");


                            flipScreen("");

                            if(versusAI)
                            {
                              AIturn();
                            }

                        }

                    } else if (player1board.isOccupied(x, y, shipSelecting, horizontal)) {
                    }

                }

            }

        }

        //REAL PERSON PLAYER2
        if (p2selecting && shipSelecting < 5 && !popupActive && !versusAI) {
            for (int x = 0; x < cols - 1; x++) {
                for (int y = 0; y < rows - 1; y++) {
                    if (e.getSource() == board2[y][x] && shipSelecting < numOfShips && !player2board.isOccupied(x, y, shipSelecting + 1, horizontal)) {

                        placeShips(player2board, x, y, shipSelecting + 1);

                        if (horizontal) {

                            for (int rep = 0; rep < shipSelecting + 1; rep++) {

                                if (rep == shipSelecting) {
                                    shipsCopy[1] = shipsInOrder[4];
                                    board2[y][x + rep].setGraphic(new ImageView(shipsInOrder[4]));
                                    board2ref[y][x + rep] = new ImageView(shipsInOrder[4]);
                                } else {

                                    board2[y][x + rep].setGraphic(new ImageView(shipsInOrder[rep]));
                                    board2ref[y][x + rep] = new ImageView(shipsInOrder[rep]);
                                }

                            }
                        } else if (!horizontal) {
                            for (int rep = 0; rep < shipSelecting + 1; rep++) {

                                if (rep == shipSelecting) {
                                    shipsCopy[1] = shipsInOrder[4];
                                    board2[y + rep][x].setGraphic(new ImageView(shipsInOrder[4]));
                                    board2[y + rep][x].getGraphic().setRotate(90);

                                    board2ref[y + rep][x] = (new ImageView(shipsInOrder[4]));
                                    board2ref[y + rep][x].setRotate(90);

                                } else {
                                    board2[y + rep][x].setGraphic(new ImageView(shipsInOrder[rep]));
                                    board2[y + rep][x].getGraphic().setRotate(90);

                                    board2ref[y + rep][x] = (new ImageView(shipsInOrder[rep]));
                                    board2ref[y + rep][x].setRotate(90);
                                }

                            }

                        }

                        shipSelecting++;

                        if (shipSelecting < numOfShips) {

                            if (horizontal) {
                                options.setCursor(new ImageCursor(ships[shipSelecting],
                                        ships[shipSelecting].getWidth() / (2 * (shipSelecting + 1)),
                                        ships[shipSelecting].getHeight() / (2)));
                            } else if (!horizontal) {
                                options.setCursor(new ImageCursor(shipsVert[shipSelecting],
                                        shipsVert[shipSelecting].getWidth() / (2),
                                        shipsVert[shipSelecting].getHeight() / (2 * (shipSelecting + 1))));
                            }

                        } else if (shipSelecting == numOfShips) {
                            shipSelecting = 0;
                            p2selecting = false;
                            p1selecting = false;
                            options.setCursor(Cursor.DEFAULT);
                            p1turn = true;

                            status.setText(player1name.getText() + "'s Turn\n");
                            flipScreen("");

                        }

                    } else if (player2board.isOccupied(x, y, shipSelecting, horizontal)) {
                        // System.out.println("Invalid Spot");
                    }

                }

            }

        }

        //the game loop
        if (!p1selecting && !p2selecting && !popupActive) {
            if (p1turn) {
                for (int x = 0; x < cols - 1; x++) {
                    for (int y = 0; y < rows - 1; y++) {
                        if (e.getSource() == board2[y][x] && player1board.getOppBoard()[y][x] == 0) {
                          if(hitsInaRowP1 == 3 && p1canNuke)
                          {
                            nukeExecute(x,y);
                            hitsInaRowP1 = 0;
                          }
                          else{
                            String str = player2board.fire(x, y);
                            if (str == "Miss") {
                              hitsInaRowP1 = 0;
                                board2[y][x].setGraphic(new ImageView(new Image("images/miss.png", 50, 50, true, true)));

                                board2ref[y][x] = (new ImageView(new Image("images/miss.png", 50, 50, true, true)));

                                player1board.updateOppBoard(x, y, str);

                                p1turn = false;
                                p2turn = true;
                                if (player2board.gameOver()) {
                                    flipScreen(player1name.getText() + " wins!\n");
                                } else {
                                    status.setText(player2name.getText() + "'s Turn");
                                    if(versusAI)
                                    {
                                      flipScreen("YOU MISSED!");
                                    }
                                    else
                                    {
                                      flipScreen(player1name.getText() + " MISSED!\n");
                                    }

                                    if(versusAI)  //when we want the AI to then shoot next
                                    {
                                      AIshoot();
                                    }
                                }

                                //you missed
                                //add transition screen code here
                            } else if (str == "Hit") {
                              hitsInaRowP1++;
                                board2[y][x].setGraphic(new ImageView(new Image("images/hit.png", 50, 50, true, true)));

                                board2ref[y][x] = (new ImageView(new Image("images/hit.png", 50, 50, true, true)));

                                player1board.updateOppBoard(x, y, str);

                                p1turn = false;
                                p2turn = true;
                                if (player2board.gameOver()) {
                                    flipScreen(player1name.getText() + " wins!\n");
                                } else {
                                    status.setText(player2name.getText() + "'s Turn");
                                    if(versusAI)
                                    {
                                      flipScreen("YOU HIT AN ENEMY SHIP!\n");

                                    }
                                    else
                                    {
                                      if(hitsInaRowP1 == 3)
                                      {
                                        flipScreen(player1name.getText() + " HIT AN ENEMY SHIP!\n" + player1name.getText() + " EARNED A NUKE!\n");
                                      }
                                      else
                                      {
                                        flipScreen(player1name.getText() + " HIT AN ENEMY SHIP!\n");
                                      }
                                    }

                                    if(versusAI)  //when we want the AI to then shoot next
                                    {
                                      AIshoot();
                                    }
                                }


                                //you hit my battleship
                                //add transition screen code here
                            } else if (str == "Sunk") {
                              hitsInaRowP1++;
                                //need to change every texture of the ship
                                Ship s = player2board.shipAt(x, y);
                                for (Point p : (s.getShipCoordinates())) {
                                    board2[(int) p.getY()][(int) p.getX()].setGraphic(new ImageView(new Image("images/sunk.png", 50, 50, true, true)));
                                    board2ref[(int) p.getY()][(int) p.getX()] = (new ImageView(new Image("images/sunk.png", 50, 50, true, true)));

                                    player1board.updateOppBoard((int) p.getX(), (int) p.getY(), str);

                                }


                                p1turn = false;
                                p2turn = true;


                                if (player2board.gameOver()) {
                                    flipScreen(player1name.getText() + " wins!\n");
                                } else {
                                    status.setText(player2name.getText() + "'s Turn");
                                    if(versusAI)
                                    {
                                      flipScreen("YOU SUNK AN AI SHIP!");     //when you sink an AI ship
                                    }
                                    else
                                    {
                                      if(hitsInaRowP1 == 3)
                                      {
                                        flipScreen(player1name.getText() + " HIT AN ENEMY SHIP!\n" + player1name.getText() + " EARNED A NUKE!\n");
                                      }
                                      else
                                      {
                                        flipScreen(player1name.getText() + " SUNK AN ENEMY SHIP!\n");
                                      }
                                    }

                                    if(versusAI)  //when we want the AI to then shoot next
                                    {
                                      AIshoot();
                                    }
                                }


                                //you sunk my battleship
                                //add transition screen code here
                            }
                        }
                      }
                    }
                }

                if(hitsInaRowP1 == 3 && p1canNuke)    //TEXT FOR IF P1 HAS A NUKE
                {
                  nukeTextP1.setText("YOU EARNED A NUKE! LAUNCH IT NOW!");
                }
                else
                {
                  nukeTextP1.setText(""); //set text back to nothing once nuke is used
                }
            }

            else if (p2turn && !versusAI) {

                for (int x = 0; x < cols - 1; x++) {
                    for (int y = 0; y < rows - 1; y++) {
                        if (e.getSource() == board1[y][x] && player2board.getOppBoard()[y][x] == 0) {
                          if(hitsInaRowP2 == 3 && p2canNuke)
                     {
                       nukeExecute(x,y);
                       hitsInaRowP2 = 0;
                     }
                     else{
                            String str = player1board.fire(x, y);
                            if (str == "Miss") {
                              hitsInaRowP2 = 0;
                                board1[y][x].setGraphic(new ImageView(new Image("images/miss.png", 50, 50, true, true)));

                                board1ref[y][x] = (new ImageView(new Image("images/miss.png", 50, 50, true, true)));

                                player2board.updateOppBoard(x, y, str);
                                p1turn = true;
                                p2turn = false;


                                if (player1board.gameOver()) {
                                    flipScreen(player2name.getText() + " wins!\n");
                                } else {
                                    status.setText(player1name.getText() + "'s Turn\n");
                                    flipScreen(player2name.getText() + " MISSED!\n");
                                }

                                //you missed
                                //add transition screen code here

                            } else if (str == "Hit") {
                              hitsInaRowP2++;
                                board1[y][x].setGraphic(new ImageView(new Image("images/hit.png", 50, 50, true, true)));

                                board1ref[y][x] = (new ImageView(new Image("images/hit.png", 50, 50, true, true)));

                                player2board.updateOppBoard(x, y, str);
                                p1turn = true;
                                p2turn = false;
                                if (player1board.gameOver()) {
                                    flipScreen(player2name.getText() + " wins!\n");
                                } else {
                                    status.setText(player1name.getText() + "'s Turn\n");
                                    if(versusAI)
                                    {
                                      flipScreen("YOU HIT AN AI SHIP!\n");
                                    }
                                    else
                                    {
                                      if(hitsInaRowP2 == 3)
                                      {
                                        flipScreen(player2name.getText() + " HIT AN ENEMY SHIP!\n" + player2name.getText() + " EARNED A NUKE!\n");
                                      }
                                      else
                                      {
                                        flipScreen(player2name.getText() + " HIT AN ENEMY SHIP!\n");
                                      }
                                    }
                                }

                                //you hit my battleship
                                //add transition screen code here
                            } else if (str == "Sunk") {
                              hitsInaRowP2++;
                                //need to change every texture of the ship
                                Ship s = player1board.shipAt(x, y);
                                for (Point p : (s.getShipCoordinates())) {
                                    board1[(int) p.getY()][(int) p.getX()].setGraphic(new ImageView(new Image("images/sunk.png", 50, 50, true, true)));

                                    board1ref[(int) p.getY()][(int) p.getX()] = (new ImageView(new Image("images/sunk.png", 50, 50, true, true)));
                                    player2board.updateOppBoard((int) p.getX(), (int) p.getY(), str);

                                }


                                p1turn = true;
                                p2turn = false;

                                if (player1board.gameOver()) {
                                    flipScreen(player2name.getText() + " wins!\n");
                                } else {
                                    status.setText(player1name.getText() + "'s Turn\n");
                                    if(hitsInaRowP2 == 3)
                                    {
                                      flipScreen(player2name.getText() + " SUNK AN ENEMY SHIP!\n" + player2name.getText() + " EARNED A NUKE!\n");
                                    }
                                    else
                                    {
                                      flipScreen(player2name.getText() + " SUNK AN ENEMY SHIP!\n");
                                    }
                                }


                                //you sunk my battleship
                                //add transition screen code here
                            }


                        }
                    }
                  }
                }

                if(hitsInaRowP2 == 3 && p2canNuke)    //TEXT FOR IF P2 HAS A NUKE
                {
                  nukeTextP2.setText("YOU EARNED A NUKE! LAUNCH IT NOW!");
                }
                else
                {
                  nukeTextP2.setText(""); //set text back to nothing once nuke is used
                }
            }
            initFire = true;

        }

        radar.setDisable(true); //disables the radar
    }


    public void Nuke(int x, int y)
    {
      nukeShotCounter++;

      if(nukeShotCounter == 8)
      {
        flipScreen("LAUNCHING NUKE!\n");
        nukeShotCounter = 0;
      }

      if(p2turn)
      {
      p2canNuke = false;
      String str = player1board.fire(x, y);


      if (str == "Miss") {
          board1[y][x].setGraphic(new ImageView(new Image("images/miss.png", 50, 50, true, true)));

          board1ref[y][x] = (new ImageView(new Image("images/miss.png", 50, 50, true, true)));

          player2board.updateOppBoard(x, y, str);



          if (player1board.gameOver()) {
          } else {
              status.setText(player1name.getText() + "'s Turn");
          }
          //you missed
       //add transition screen code here

    } else if (str == "Hit") {
       board1[y][x].setGraphic(new ImageView(new Image("images/hit.png", 50, 50, true, true)));

       board1ref[y][x] = (new ImageView(new Image("images/hit.png", 50, 50, true, true)));

       player2board.updateOppBoard(x, y, str);

       if (player1board.gameOver()) {
           flipScreen(player2name.getText() + " wins!");
       } else {
           status.setText(player1name.getText() + "'s Turn");
       }

       //you hit my battleship
       //add transition screen code here
    } else if (str == "Sunk") {
       //need to change every texture of the ship
       Ship s = player1board.shipAt(x, y);
       for (Point p : (s.getShipCoordinates())) {
           board1[(int) p.getY()][(int) p.getX()].setGraphic(new ImageView(new Image("images/sunk.png", 50, 50, true, true)));

           board1ref[(int) p.getY()][(int) p.getX()] = (new ImageView(new Image("images/sunk.png", 50, 50, true, true)));
           player2board.updateOppBoard((int) p.getX(), (int) p.getY(), str);

       }




       if (player1board.gameOver()) {
           // flipScreen(player2name.getText() + " wins!");
       } else {
           status.setText(player1name.getText() + "'s Turn");
           // flipScreen("YOU SUNK MY BATTLESHIP!");
       }
       //you sunk my battleship
       //add transition screen code here
    }
  }
  else{
    p1canNuke = false;
    String str = player2board.fire(x, y);
    if (str == "Miss") {
      hitsInaRowP1 = 0;
        board2[y][x].setGraphic(new ImageView(new Image("images/miss.png", 50, 50, true, true)));

        board2ref[y][x] = (new ImageView(new Image("images/miss.png", 50, 50, true, true)));

        player1board.updateOppBoard(x, y, str);

        if (player2board.gameOver()) {
            flipScreen(player1name.getText() + " wins!\n");
        }
        else {
            status.setText(player2name.getText() + "'s Turn");
            if(versusAI)
            {
              // flipScreen("YOU MISSED!");
            }
            else
            {
              // flipScreen(player1name.getText() + " MISSED!\n");
            }

            if(versusAI)  //when we want the AI to then shoot next
            {
              AIshoot();
            }
        }


        //you missed
        //add transition screen code here
    } else if (str == "Hit") {
      hitsInaRowP1++;
        board2[y][x].setGraphic(new ImageView(new Image("images/hit.png", 50, 50, true, true)));

        board2ref[y][x] = (new ImageView(new Image("images/hit.png", 50, 50, true, true)));

        player1board.updateOppBoard(x, y, str);

        if (player2board.gameOver()) {
            flipScreen(player1name.getText() + " wins!\n");
        } else {
            status.setText(player2name.getText() + "'s Turn");
            if(versusAI)
            {
              // flipScreen("YOU HIT AN ENEMY SHIP!\n");
            }
            else
            {
              // flipScreen(player1name.getText() + " HIT AN ENEMY SHIP!\n");
            }

            if(versusAI)  //when we want the AI to then shoot next
            {
              AIshoot();
            }
        }


        //you hit my battleship
        //add transition screen code here
    } else if (str == "Sunk") {
      hitsInaRowP1++;
        //need to change every texture of the ship
        Ship s = player2board.shipAt(x, y);
        for (Point p : (s.getShipCoordinates())) {
            board2[(int) p.getY()][(int) p.getX()].setGraphic(new ImageView(new Image("images/sunk.png", 50, 50, true, true)));
            board2ref[(int) p.getY()][(int) p.getX()] = (new ImageView(new Image("images/sunk.png", 50, 50, true, true)));

            player1board.updateOppBoard((int) p.getX(), (int) p.getY(), str);

        }

        if (player2board.gameOver()) {
            flipScreen(player1name.getText() + " wins!\n");
        } else {
            status.setText(player2name.getText() + "'s Turn");
            if(versusAI)
            {
              // flipScreen("YOU SUNK AN AI SHIP!");     //when you sink an AI ship
            }
            else
            {
              // flipScreen(player1name.getText() + " SUNK AN ENEMY SHIP!\n");
            }

            if(versusAI)  //when we want the AI to then shoot next
            {
              AIshoot();
            }
        }

        //you sunk my battleship
        //add transition screen code here
    }


  }


    }

    public void nukeExecute(int x, int y)
    {
        Nuke(x,y);
        Nuke(x+1,y);
        Nuke(x-1,y);
        Nuke(x-1,y+1);
        Nuke(x,y+1);
        Nuke(x+1,y+1);
        Nuke(x,y-1);
        Nuke(x-1,y-1);
        Nuke(x+1,y-1);

        p1turn = !p1turn;
        p2turn = !p2turn;

        if(p2turn && versusAI) //calling for AI's turn
        {
          AIshoot();
        }
    }

    public void radar(int x, int y)
    {
      if(p1turn)
      {
        p1xRadarCoord = x;
        p1yRadarCoord = y;
      }
      else
      {
        p2xRadarCoord = x;
        p2yRadarCoord = y;
      }


      if(p1turn)
      {
      scan(x,y);
      scan(x, y+1);
      scan(x, y-1);
      scan(x+1,y);
      scan(x-1, y);
    }
    else
    {
      scan(x,y);
      scan(x, y+1);
      scan(x, y-1);
      scan(x+1,y);
      scan(x-1, y);
    }

    }

    public void scan(int x, int y)
    {

      if (x > cols-2 || y > cols-2 || x < 0 || y < 0) //checking if out of bounds
      {
        return;
      }
      else if(p1turn)
      {
        if(player2board.getBoard()[y][x] == 0)  //if that location is an empty space, we will update it to a "MISS"
        {
          board2ref[y][x] = (new ImageView(new Image("images/miss.png", 50, 50, true, true)));
          player1board.getOppBoard()[y][x] = 1;  //updates from player 1's view that that location is now a miss, so they cannot click it and shoot there
        }
        board2[y][x].setGraphic(board2ref[y][x]);

      }
      else
      {
        if(player1board.getBoard()[y][x] == 0)  //if that location is an empty space, we will update it to a "MISS"
        {
          board1ref[y][x] = (new ImageView(new Image("images/miss.png", 50, 50, true, true)));
          player2board.getOppBoard()[y][x] = 1;  //updates from player 2's view that that location is now a miss, so they cannot click it and shoot there

        }
        board1[y][x].setGraphic(board1ref[y][x]);
      }
    }


}
