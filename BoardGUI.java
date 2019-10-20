/**
Battleship!
KU EECS 448 project 2
TeamName: BigSegFaultEnergy
  * \Author: Chance Penner
  * \Author: Markus Becerra
  * \Author: Sarah Scott
  * \Author: Thomas Gardner
  * \Author: Haonan Hu
  * \File:	 BoardGUI.java
  * \Date:   10/20/2019
  * \Brief:  This class serves as a the executive class of
             Battleship as well as making basic gui elements.

KU EECS 448 project 1
TeamName: Poor Yorick
  * \Author: Max Goad
  * \Author: Jace Bayless
  * \Author: Tri Pham
  * \Author: Apurva Rai
  * \Author: Meet Kapadia
  * \File:	 BoardGUI.java
  * \Brief:  This class serves as a the executive class of
             Battleship as well as making basic gui elements.
*/

//Here are erternal classes that need to be imported
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

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
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;  //for random numbers

import javafx.collections.*;
import javafx.collections.FXCollections;

import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.geometry.HPos;
import javafx.geometry.VPos;

import java.awt.Point;

import javafx.stage.Popup;
import javafx.stage.Stage;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;

public class BoardGUI implements OverScene, EventHandler<ActionEvent>
{

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
   * @ param string, stage, font, number of ships, players' names
   * @ post constuctor
   * @ return none
   */
  public BoardGUI(String gamemode, Stage s, Font f, int numOfShips, String player1name, String player2name)
  {
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
    radarImage = new Image(getClass().getResourceAsStream("images/radar.png"),70,70, false, false);//https://stackoverflow.com/questions/27894945/how-do-i-resize-an-imageview-image-in-javafx

    ships = new Image[5];
    shipsInOrder = new Image[5];
    shipsVert = new Image[5];

    for (int rep = 0; rep < 5; rep++)
    {
      ships[rep] = new Image("images/1x" + Integer.toString(rep + 1) + ".png", 50 * (rep + 1), 50, true, true);
    }

    for (int rep = 0; rep < 5; rep++)
    {
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

    //Define their placement in the grid
    gr = new GridPane();
    player1 = new GridPane();
    player2 = new GridPane();
    //make columns for each board

    for (int x = 0; x < cols; x++)
    {
      ColumnConstraints c = new ColumnConstraints();
      c.setPercentWidth(100.0 / cols);
      player1.getColumnConstraints().add(c);
      player2.getColumnConstraints().add(c);
    }

    //rows for each board
    for (int y = 0; y < rows; y++)
    {
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
    radar.setOnAction(e ->
    {
      useRadar = true;
    });

    gr.getChildren().add(radar);
    gr.setHalignment(radar, HPos.CENTER); //positioning the radar
    radar.setTranslateX(375); //overiding to position the radar image
    radar.setTranslateY(20);

    radar.setDisable(true);  //when true, you cannot click the radar. We default to this so the users cannot press the radar button during setup

    radar.setGraphic(new ImageView(radarImage));  //sets the image of the radar button


    for (int c = 0; c < cols - 1; c++)
    {
      for (int r = 0; r < rows - 1; r++)
      {
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
   * @ param Player, x and y coordinate of the grid, length of the ship
   * @ post places the ships
   * @ return none
   */
  public void placeShips(GameBoard player, int x, int y, int shipLength)
  {
    Ship tempShip = new Ship(shipLength);

    if (!player.isOccupied(x, y, shipLength, horizontal))
    {
      for (int rep = 0; rep < shipLength; rep++)
      {
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
   * @ param a message
   * @ post flips the screen
   * @ return none
   */
  public void flipScreen(String messageToPlayer)
  {
    //this changes the screen to the other players view
    //i.e. blocks the ship locations from the other persons pov
    //also handles the popup menu in between

    //https://www.geeksforgeeks.org/javafx-popup-class/
    //popup code

    //clear boards inbetween turns!!!!
    for (int y = 0; y < 8; y++)
    {       //double for loop for each location in the board
      for (int x = 0; x < 8; x++)
      {
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

    Task<Void> sleeper = new Task<Void>()  //creating a new task called sleeper to pause the game during popups
    {
      @Override
      protected Void call() throws Exception
      {    //set the function as protected
        try
        {                    //try catch block for the sleeper
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

    sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>()//function for when the sleeper returns
    {
      @Override
      public void handle(WorkerStateEvent event)
      {
        if(close.getText().contains("wins"))       //if the text in a popup reads "wins" end the program
        {
          System.exit(0);   //once a player wins, exit the program
        }
        pop.hide();          //this line and the two following it happen after every turn
        stage.hide();        //hide the stage the popup is on
        popupActive = false;    //popup is no longer active

        if (p1turn)//if it's player1's turn
        {
          int[][] oppBoard = player1board.getOppBoard();    //create a new 2D board array for player1's view of player2's board
          for (int y = 0; y < 8; y++)//for loop to set graphics of player1's view of player2's board
          {
            for (int x = 0; x < 8; x++)
            {
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
          for (int y = 0; y < 8; y++) //for loop to set graphics of player2's view of player1's board
          {
            for (int x = 0; x < 8; x++)
            {
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

        if (p2selecting || p1selecting)
        {                  //if either player is selecting
          options.setCursor(new ImageCursor(ships[0],   //set the cursor to the ships image they are currently on
          ships[0].getWidth() / 2,
          ships[0].getHeight() / 2));
        }
      }
    });
    new Thread(sleeper).start();      //the popup is called and starts the sleeper
  }   //end setOnSucceeded

  /*
   * @ pre none
   * @ param none
   * @ post gets the next scene
   * @ return returns next scene
   */
  @Override
  public Scene getScene()
  {
    return options;
  }


  /*
   * @ pre none
   * @ param none
   * @ post AI place ships
   * @ return none
   */
  public void AIturn() //for AI choosing ship placement. Needs to be outside of EventHandler since AI isn't actually pressing a button
  {
    for(int i = shipSelecting;i<numOfShips;i++)
    {
      //AI PLAYER
      if (p2selecting && shipSelecting < 5 && versusAI)
      {
        yAI = 9;  //reinitialize to value outside of range
        xAI = 9;  //reinitialize to value outside of range
        //run until random values are within bounds and also do not result in collision
        do
        {
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
            }
            else
            {
              board2[y][x + rep].setGraphic(new ImageView(shipsInOrder[rep]));
              board2ref[y][x + rep] = new ImageView(shipsInOrder[rep]);
            }
          }
        }
        else if (!horizontal)
        {
          for (int rep = 0; rep < shipSelecting + 1; rep++)
          {
            if (rep == shipSelecting)
            {
              shipsCopy[1] = shipsInOrder[4];
              board2[y + rep][x].setGraphic(new ImageView(shipsInOrder[4]));
              board2[y + rep][x].getGraphic().setRotate(90);

              board2ref[y + rep][x] = (new ImageView(shipsInOrder[4]));
              board2ref[y + rep][x].setRotate(90);
            }
            else
            {
              board2[y + rep][x].setGraphic(new ImageView(shipsInOrder[rep]));
              board2[y + rep][x].getGraphic().setRotate(90);

              board2ref[y + rep][x] = (new ImageView(shipsInOrder[rep]));
              board2ref[y + rep][x].setRotate(90);
            }
          }
        }
        shipSelecting++;

        if (shipSelecting < numOfShips)
        {
          if (horizontal) //sets the cursor image to the image of the current ship to be placed horizontally
          {
            options.setCursor(new ImageCursor(ships[shipSelecting],
                    ships[shipSelecting].getWidth() / (2 * (shipSelecting + 1)),
                    ships[shipSelecting].getHeight() / (2)));
          }
          else if (!horizontal) //sets the cursor image to the image of the current ship to be placed vertically
          {
            options.setCursor(new ImageCursor(shipsVert[shipSelecting],
                    shipsVert[shipSelecting].getWidth() / (2),
                    shipsVert[shipSelecting].getHeight() / (2 * (shipSelecting + 1))));
          }
        }
        else if (shipSelecting == numOfShips) //Once done placing the ships
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

  /*
   * @ pre none
   * @ param none
   * @ post AI shoot the player
   * @ return none
   */
  public void AIshoot()  //when we want the AI to shoot. Needs to be outside of EventHandler since AI isn't actually pressing a button
  {
    if(p2turn && versusAI)
    {
      yAI = 9;  //reinitialize to value outside of range
      xAI = 9;  //reinitialize to value outside of range
      //run until random values are within bounds and also do not result in collision

      // randomly shooting
      if(gamemode == "Easy")
      {
        do
        {
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

          do
          {
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

          if (player1board.gameOver())
          {
            flipScreen(player2name.getText() + " wins!\n");
          }
          else
          {
            status.setText(player1name.getText() + "'s Turn\n");
            flipScreen("AI MISSED YOUR SHIPS!\n");
          }
              //you missed
              //add transition screen code here
        }
        else if (str == "Hit")  //when Hit, set that location to the Hit image
        {
          hitsInaRowP2++; //updates the combo counter for earning a Nuke
          board1[y][x].setGraphic(new ImageView(new Image("images/hit.png", 50, 50, true, true)));
          board1ref[y][x] = (new ImageView(new Image("images/hit.png", 50, 50, true, true)));
          player2board.updateOppBoard(x, y, str);
          p1turn = true;
          p2turn = false;
          if (player1board.gameOver())
          {
            flipScreen(player2name.getText() + " wins!\n");
          }
          else
          {
            status.setText(player1name.getText() + "'s Turn\n");
            flipScreen("AI HIT ONE OF YOUR SHIPS!\n");
          }

              //you hit my battleship
              //add transition screen code here
        }
        else if (str == "Sunk") //when Sunk, set that location to the Sunk image
        {
          //need to change every texture of the ship
          hitsInaRowP2++;
          Ship s = player1board.shipAt(x, y);
          for (Point p : (s.getShipCoordinates()))
          {
            board1[(int) p.getY()][(int) p.getX()].setGraphic(new ImageView(new Image("images/sunk.png", 50, 50, true, true)));
            board1ref[(int) p.getY()][(int) p.getX()] = (new ImageView(new Image("images/sunk.png", 50, 50, true, true)));
            player2board.updateOppBoard((int) p.getX(), (int) p.getY(), str);
          }

          p1turn = true;
          p2turn = false;

          if (player1board.gameOver())
          {
            flipScreen(player2name.getText() + " wins!\n");
          }
          else
          {
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

            if (ke.getCode() == KeyCode.R && (p1selecting || p2selecting)) //if the player presses 'R' to rotate a ship
            {
                if (horizontal) //if the ship is to be placed horizontally, the image at the cursor is a horizontal ship
                {
                    options.setCursor(new ImageCursor(shipsVert[shipSelecting],
                            shipsVert[shipSelecting].getHeight() / (2 * (shipSelecting + 1)),
                            shipsVert[shipSelecting].getWidth() / (2)));
                } else if (!horizontal) //if the ship is to be placed vertically, the image at the cursor is a vertical ship
                {
                    options.setCursor(new ImageCursor(ships[shipSelecting],
                            ships[shipSelecting].getHeight() / (2),
                            ships[shipSelecting].getWidth() / (2 * (shipSelecting + 1))));
                }

                horizontal = !horizontal; //updates horizontal since every 'R' press flips it

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
                if (e.getSource() == board2[y][x])  //gets the coordinates of where the user placed the radar
                {
                  radar(x,y); //calls radar at that location
                }
            }

          }
          p1radarWasUsed = true;  //since radar is a one-time use item per player, player1 cannot use the radar again
          radar.setDisable(true); //disable player 1 from using the radar
      }
      else
      {
        for (int x = 0; x < cols - 1; x++)
        {
            for (int y = 0; y < rows - 1; y++)
            {
                if (e.getSource() == board1[y][x])  //gets the coordinates of where the user placed the radar
                {
                  radar(x,y); //calls radar at that location
                }
            }

          }
          p2radarWasUsed = true;  //since the radar is a one-time use item per player, player2 cannot use the radar again
          radar.setDisable(true); //disable player 2 from using the radar
      }

        useRadar = false; //reinitialize to false
        return; //return so that we break out of the EventHandler so that it waits for another mouse input to then shoot after using the radar
                //if we didn't return, the code below would run, and it would try to shoot at the coordinate they placed the radar at
      }

    if (p1selecting && shipSelecting < 5 && !popupActive) //if player1 is placing ships
    {
        for (int x = 0; x < cols - 1; x++) {
            for (int y = 0; y < rows - 1; y++) {
                if (e.getSource() == board1[y][x] && shipSelecting < numOfShips && !player1board.isOccupied(x, y, shipSelecting + 1, horizontal)) //gets the location to place a ship if it isn't occupied all along where it would be placed
                {
                    placeShips(player1board, x, y, shipSelecting + 1);  //places the ship at the locations per piece of the ship

                    if (horizontal) //if horizontal, places the ship parts horizontally
                    {
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
                    } else if (!horizontal) //if vertical, places the ship parts vertically
                    {
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

                    shipSelecting++;  //updates to the next ship length to be placed

                    if (shipSelecting < numOfShips)  //sets the cursor to the image of the ship to be placed
                    {
                        if (horizontal) {
                            options.setCursor(new ImageCursor(ships[shipSelecting],
                                    ships[shipSelecting].getWidth() / (2 * (shipSelecting + 1)),
                                    ships[shipSelecting].getHeight() / (2)));
                        } else if (!horizontal) {
                            options.setCursor(new ImageCursor(shipsVert[shipSelecting],
                                    shipsVert[shipSelecting].getWidth() / (2),
                                    shipsVert[shipSelecting].getHeight() / (2 * (shipSelecting + 1))));
                        }

                    } else if (shipSelecting == numOfShips) //all ships placed, switch to the next player's turn
                    {
                        shipSelecting = 0;
                        p1selecting = false;
                        p2selecting = true;
                        status.setText(player2name.getText() + " selecting ships");


                        flipScreen("");

                        if(versusAI)
                        {
                          AIturn(); //if playing against the AI, then it is AI's turn to place ships
                        }

                    }

                } else if (player1board.isOccupied(x, y, shipSelecting, horizontal))
                {
                  //do nothing so that the player has to keep clicking until clicking a valid location
                }

            }

        }

    }

    //REAL PERSON PLAYER2
    if (p2selecting && shipSelecting < 5 && !popupActive && !versusAI)  //if player2 is placing ships and not versus the AI
    {
        for (int x = 0; x < cols - 1; x++) {
            for (int y = 0; y < rows - 1; y++) {
                if (e.getSource() == board2[y][x] && shipSelecting < numOfShips && !player2board.isOccupied(x, y, shipSelecting + 1, horizontal)) {

                    placeShips(player2board, x, y, shipSelecting + 1);  //places the ship at the locations per piece of the ship

                    if (horizontal)
                    {
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
                        for (int rep = 0; rep < shipSelecting + 1; rep++) //if vertical, places the ship parts vertically
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

                    shipSelecting++;  //updates to the next ship length to be placed

                    if (shipSelecting < numOfShips) //sets the cursor to the image of the ship to be placed
                    {
                        if (horizontal) {
                            options.setCursor(new ImageCursor(ships[shipSelecting],
                                    ships[shipSelecting].getWidth() / (2 * (shipSelecting + 1)),
                                    ships[shipSelecting].getHeight() / (2)));
                        } else if (!horizontal) {
                            options.setCursor(new ImageCursor(shipsVert[shipSelecting],
                                    shipsVert[shipSelecting].getWidth() / (2),
                                    shipsVert[shipSelecting].getHeight() / (2 * (shipSelecting + 1))));
                        }

                    } else if (shipSelecting == numOfShips) //all ships placed, switch to the next player's turn
                    {
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
    if (!p1selecting && !p2selecting && !popupActive) //the shooting phase
    {
        if (p1turn) {
            for (int x = 0; x < cols - 1; x++) {
                for (int y = 0; y < rows - 1; y++) {
                    if (e.getSource() == board2[y][x] && player1board.getOppBoard()[y][x] == 0) //gets the location to be shot at as long as it's an empty spot
                    {
                      if(hitsInaRowP1 == 3 && p1canNuke)  //first checks if p1 has earned a Nuke from the previous turn. If so, this shot will use the Nuke
                      {
                        nukeExecute(x,y);
                        hitsInaRowP1 = 0; //reinitializes to 0
                      }
                      else{
                        String str = player2board.fire(x, y); //sets str to either Miss, Hit, or Sunk
                        if (str == "Miss") {
                          hitsInaRowP1 = 0; //if a miss, resets the nuke combo counter
                            board2[y][x].setGraphic(new ImageView(new Image("images/miss.png", 50, 50, true, true)));

                            board2ref[y][x] = (new ImageView(new Image("images/miss.png", 50, 50, true, true)));

                            player1board.updateOppBoard(x, y, str); //updates the board to the image represented by str

                            p1turn = false;
                            p2turn = true;  //updates to player 2's turn
                            if (player2board.gameOver())  //if player1 wins, displays so
                            {
                                flipScreen(player1name.getText() + " wins!\n");
                            } else {
                                status.setText(player2name.getText() + "'s Turn");
                                if(versusAI)
                                {
                                  flipScreen("YOU MISSED!");  //if versus AI and p1 missed, displays so
                                }
                                else
                                {
                                  flipScreen(player1name.getText() + " MISSED!\n"); //if not versus AI, displays player1's name missed
                                }

                                if(versusAI)  //when we want the AI to then shoot next
                                {
                                  AIshoot();
                                }
                            }

                            //you missed
                            //add transition screen code here
                        } else if (str == "Hit") {
                          hitsInaRowP1++; //if a hit, updates the combo counter
                            board2[y][x].setGraphic(new ImageView(new Image("images/hit.png", 50, 50, true, true)));

                            board2ref[y][x] = (new ImageView(new Image("images/hit.png", 50, 50, true, true)));

                            player1board.updateOppBoard(x, y, str); //updates the opponent board

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
                                  if(hitsInaRowP1 == 3) //displays that p1 earned a nuke after hitting 3 ships in a row
                                  {
                                    flipScreen(player1name.getText() + " HIT AN ENEMY SHIP!\n" + player1name.getText() + " EARNED A NUKE!\n");
                                  }
                                  else  //otherwise, we just display that you hit an enemy ship
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


                            p1turn = false; //changes turns
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
                                  if(hitsInaRowP1 == 3 && p1canNuke) //if p1 earned a nuke, displays that
                                  {
                                    flipScreen(player1name.getText() + " HIT AN ENEMY SHIP!\n" + player1name.getText() + " EARNED A NUKE!\n");
                                  }
                                  else  //otherwise, we just display that you sunk a ship
                                  {
                                    flipScreen(player1name.getText() + " SUNK AN ENEMY SHIP!\n");
                                  }
                                }

                                if(versusAI)  //when we want the AI to then shoot next
                                {
                                  AIshoot();
                                }
                            }

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
                    if (e.getSource() == board1[y][x] && player2board.getOppBoard()[y][x] == 0) //gets the location to be shot at as long as it's an empty spot
                    {
                      if(hitsInaRowP2 == 3 && p2canNuke)  //first checks if p2 has earned a Nuke from the previous turn. If so, this shot will use the Nuke
                 {
                   nukeExecute(x,y);
                   hitsInaRowP2 = 0;  //reinitializes to 0
                 }
                 else{
                        String str = player1board.fire(x, y); //sets str to either Miss, Hit, or Sunk
                        if (str == "Miss") {
                          hitsInaRowP2 = 0;
                            board1[y][x].setGraphic(new ImageView(new Image("images/miss.png", 50, 50, true, true)));

                            board1ref[y][x] = (new ImageView(new Image("images/miss.png", 50, 50, true, true)));

                            player2board.updateOppBoard(x, y, str); //updates the board to the image represented by str
                            p1turn = true;  //updates to player 1's turn
                            p2turn = false;

                            if (player1board.gameOver())  //if player2 wins, displays so
                            {
                                flipScreen(player2name.getText() + " wins!\n");
                            } else {
                                status.setText(player1name.getText() + "'s Turn\n");
                                flipScreen(player2name.getText() + " MISSED!\n");
                            }

                            //you missed
                            //add transition screen code here

                        } else if (str == "Hit") {
                          hitsInaRowP2++; //if a hit, updates the combo counter
                            board1[y][x].setGraphic(new ImageView(new Image("images/hit.png", 50, 50, true, true)));

                            board1ref[y][x] = (new ImageView(new Image("images/hit.png", 50, 50, true, true)));

                            player2board.updateOppBoard(x, y, str); //updates the opponent board
                            p1turn = true;
                            p2turn = false;
                            if (player1board.gameOver()) {
                                flipScreen(player2name.getText() + " wins!\n");
                            } else {
                                status.setText(player1name.getText() + "'s Turn\n");
                                //TODO remove AI code since this is in player2 and player2 never goes against an AI
                                if(versusAI)
                                {
                                  flipScreen("YOU HIT AN AI SHIP!\n");  //if versus AI, we display this message on a hit
                                }
                                else  //if not versus AI
                                {
                                  if(hitsInaRowP2 == 3 && p2canNuke) //if you earned a Nuke, we display that you hit and earned a Nuke
                                  {
                                    flipScreen(player2name.getText() + " HIT AN ENEMY SHIP!\n" + player2name.getText() + " EARNED A NUKE!\n");
                                  }
                                  else  //otherwise, we just display that you hit a ship
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


                            p1turn = true;  //change to p1's turn
                            p2turn = false;

                            if (player1board.gameOver())  //if player 2 wins, display so
                            {
                                flipScreen(player2name.getText() + " wins!\n");
                            } else {
                                status.setText(player1name.getText() + "'s Turn\n");
                                if(hitsInaRowP2 == 3) //if earned a Nuke, display so
                                {
                                  flipScreen(player2name.getText() + " SUNK AN ENEMY SHIP!\n" + player2name.getText() + " EARNED A NUKE!\n");
                                }
                                else  //otherwise, just display that a ship was sunk
                                {
                                  flipScreen(player2name.getText() + " SUNK AN ENEMY SHIP!\n");
                                }
                            }
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

/*
 * @ pre none
 * @ param x and y for coordinates
 * @ post nuke function for if the player earns a nuke
 * @ return none
 */
public void Nuke(int x, int y)
{
  nukeShotCounter++;    //increase the nukeShotCounter variable, since we have called this function from nukeExecute

  if(nukeShotCounter == 8)    //if nukeShotCounter is 8, we have exhausted the shots for our nuke
  {
    flipScreen("LAUNCHING NUKE!\n");    //send the message for the popup as launching nuke
    nukeShotCounter = 0;    //reset nukeShotCounter for the next player's use
  }

  if(p2turn)    //if it's player 2's turn
  {
  p2canNuke = false;      //set p2canNuke to false, they have now used their one-time use nuke
  String str = player1board.fire(x, y);       //set a string for determining the value of the shot for firing


  if (str == "Miss") {      //if the string is a miss
      board1[y][x].setGraphic(new ImageView(new Image("images/miss.png", 50, 50, true, true)));   //set the graphic to a miss on player1's board

      board1ref[y][x] = (new ImageView(new Image("images/miss.png", 50, 50, true, true)));    //set the graphic to a miss on the left board

      player2board.updateOppBoard(x, y, str);   //update the oppBoard of player2 passing in a miss for string



      if (player1board.gameOver()) {    //if the game ended
      }
      else {  //if the game did not end
          status.setText(player1name.getText() + "'s Turn");    //set the center label text to player1's turn
      }

    }
  else if (str == "Hit") {      //if the string is a hit
   board1[y][x].setGraphic(new ImageView(new Image("images/hit.png", 50, 50, true, true)));   //set the graphic to a hit on player1's board
   board1ref[y][x] = (new ImageView(new Image("images/hit.png", 50, 50, true, true)));    //set the graphic to a hit on the left board

   player2board.updateOppBoard(x, y, str);    //update the oppBoard of player2 passing in a hit for the string

   if (player1board.gameOver()) {   //if the game ended
       flipScreen(player2name.getText() + " wins!");    //set the pop up text to player2 wins
   } else {   //if the game did not end
       status.setText(player1name.getText() + "'s Turn");   //set the center label text to player1's turn
   }

   //you hit my battleship
   //add transition screen code here
} else if (str == "Sunk") {   //if the string is a sunk
   Ship s = player1board.shipAt(x, y);        //create a new ship object at the x and y that player2 shot
   for (Point p : (s.getShipCoordinates())) {   //for each pice of the ship
       board1[(int) p.getY()][(int) p.getX()].setGraphic(new ImageView(new Image("images/sunk.png", 50, 50, true, true)));    //set the graphic to sunk on player1's board
       board1ref[(int) p.getY()][(int) p.getX()] = (new ImageView(new Image("images/sunk.png", 50, 50, true, true)));   //set the graphic to sunk on the left board
       player2board.updateOppBoard((int) p.getX(), (int) p.getY(), str);    //update player2 board, passing in sunk for the string
   }

   if (player1board.gameOver()) {   //if the game ended
   }
   else {   //if the game did not end
       status.setText(player1name.getText() + "'s Turn");   //set the center label text to player1's turn
   }
}
}

else{     //if it's player1's turn
p1canNuke = false;    //set p1canNuke to false, they have used their one use nuke
String str = player2board.fire(x, y);   //create a string for determing the value of the shot
if (str == "Miss") {    //if the string is a miss
  hitsInaRowP1 = 0;   //set hitsInaRowP1 to 0, they did not get a hit
    board2[y][x].setGraphic(new ImageView(new Image("images/miss.png", 50, 50, true, true)));   //set the graphic of player2's board to a miss
    board2ref[y][x] = (new ImageView(new Image("images/miss.png", 50, 50, true, true)));    //set the graphic of the right board to a miss

    player1board.updateOppBoard(x, y, str);   //update player1's opp board, passing in miss as the string

    if (player2board.gameOver()) {      //if the game ended
        flipScreen(player1name.getText() + " wins!\n");   //set the popup text to player 1 wins
    }
    else {    //if the game did not end
        status.setText(player2name.getText() + "'s Turn");    //set the center label to player2's turn
        if(versusAI)    //if we are playing AI
        {
          // flipScreen("YOU MISSED!");
        }
        else
        {
          // flipScreen(player1name.getText() + " MISSED!\n");
        }

        if(versusAI)  //when we want the AI to then shoot next
        {
          AIshoot();    //call AIshoot so the AI shoots
        }
    }
  }
else if (str == "Hit") {    //if the string is a hit
  hitsInaRowP1++;             //increment hitsInaRowP1
    board2[y][x].setGraphic(new ImageView(new Image("images/hit.png", 50, 50, true, true)));  //set the graphic of player2's board to hit
    board2ref[y][x] = (new ImageView(new Image("images/hit.png", 50, 50, true, true)));   //set the graphic of the right board to hit

    player1board.updateOppBoard(x, y, str);   //update player1's opp board passing in hit as the string

    if (player2board.gameOver()) {    //if the game ended
        flipScreen(player1name.getText() + " wins!\n");
    }
    else {    //if the game did not end
        status.setText(player2name.getText() + "'s Turn");      //set the center label to player2's turn
        if(versusAI)    //if we are facing AI
        {
          // flipScreen("YOU HIT AN ENEMY SHIP!\n");
        }
        else    //if we are not facing AI
        {
          // flipScreen(player1name.getText() + " HIT AN ENEMY SHIP!\n");
        }

        if(versusAI)  //when we want the AI to then shoot next
        {
          AIshoot();    //call AIshoot so the AI shoots
        }
    }
}
else if (str == "Sunk") {     //if the string is sunk
  hitsInaRowP1++;   //increment hitsInaRowP1
    Ship s = player2board.shipAt(x, y);         //create a ship object at the x and y the player shot
    for (Point p : (s.getShipCoordinates())) {    //for the length of the ship
        board2[(int) p.getY()][(int) p.getX()].setGraphic(new ImageView(new Image("images/sunk.png", 50, 50, true, true)));   //set the graphic of player2's board to a sunk
        board2ref[(int) p.getY()][(int) p.getX()] = (new ImageView(new Image("images/sunk.png", 50, 50, true, true)));      //set the graphi of the right board to a sunk

        player1board.updateOppBoard((int) p.getX(), (int) p.getY(), str);   //udpate p1's oppBoard, passing in sunk as the string

    }

    if (player2board.gameOver()) {      //if the game ended
        flipScreen(player1name.getText() + " wins!\n");     //set the popup text to player1 wins
    } else {
        status.setText(player2name.getText() + "'s Turn");    //set the center label text to player2's turn
        if(versusAI)    //if we are facing AI
        {
          // flipScreen("YOU SUNK AN AI SHIP!");     //when you sink an AI ship
        }
        else    //if we are not facing AI
        {
          // flipScreen(player1name.getText() + " SUNK AN ENEMY SHIP!\n");
        }

        if(versusAI)  //when we want the AI to then shoot next
        {
          AIshoot();      //call AIshoot so the AI can shoot
        }
      }
    }
  }
}   //end nuke function

/*
 * @ pre none
 * @ param x and y as coordinates
 * @ post nukeExecute function that calls the nuke function for each location
 * @ return none
 */
public void nukeExecute(int x, int y)
{
    Nuke(x,y);      //call nuke at the center location
    Nuke(x+1,y);    //call nuke at the right location
    Nuke(x-1,y);    //call nuke at the left location
    Nuke(x-1,y+1);  //call nuke at the top left location
    Nuke(x,y+1);    //call nuke at the middle top location
    Nuke(x+1,y+1);  //call nuke at the top right location
    Nuke(x,y-1);    //call nuke at the middle bottom location
    Nuke(x-1,y-1);  //call nuke at the bottom left location
    Nuke(x+1,y-1);  //call nuke at the bottom right location

    p1turn = !p1turn;   //switching to the next turn
    p2turn = !p2turn;

    if(p2turn && versusAI) //calling for AI's turn
    {
      AIshoot();    //call AIshoot so AI can shoot
    }
}   //end nukeExecute function

/*
 * @ pre none
 * @ param x and y are coordinates
 * @ post a + shape radar placed on desired location
 * @ return none
 */
public void radar(int x, int y)   //radar function that calls scan to gather the enemy board
{
  if(p1turn)    //if it's player1 using the radar
  {
    p1xRadarCoord = x;    //set the origin coordinates of the radar
    p1yRadarCoord = y;
  }
  else          //if it's player2 using the radar
  {
    p2xRadarCoord = x;
    p2yRadarCoord = y;
  }

  if(p1turn)    //if it's player1 using the radar
  {
  scan(x,y);      //call scan at the center location
  scan(x, y+1);   //call scan at the center top location
  scan(x, y-1);   //call scan at the center bottom location
  scan(x+1,y);    //call scan at the right location
  scan(x-1, y);   //call scan at the left location
}
else             //if it's player2 using the radar
{
  scan(x,y);      //call scan at the center location
  scan(x, y+1);   //call scan at the center top location
  scan(x, y-1);   //call scan at the center bottom location
  scan(x+1,y);    //call scan at the right location
  scan(x-1, y);   //call scan at the left location
}

}   //end radar function

/*
 * @ pre none
 * @ param x and y are coordinates
 * @ post scan function that scans the enemy player's board
 * @ return none
 */
public void scan(int x, int y)
{

  if (x > cols-2 || y > cols-2 || x < 0 || y < 0) //checking if out of bounds
  {
    return;   //if out of bounds just return out of the function
  }
  else if(p1turn)   //if in bounds and player1's turn
  {
    if(player2board.getBoard()[y][x] == 0)  //if that location is an empty space, we will update it to a "MISS"
    {
      board2ref[y][x] = (new ImageView(new Image("images/miss.png", 50, 50, true, true)));    //update the right board with misses if there are no ships on player2's board
      player1board.getOppBoard()[y][x] = 1;  //updates from player 1's view that that location is now a miss, so they cannot click it and shoot there
    }
    board2[y][x].setGraphic(board2ref[y][x]);   //set the graphic of player2's board to newly updated graphic locations

  }
  else    //if in bounds and player2's turn
  {
    if(player1board.getBoard()[y][x] == 0)  //if that location is an empty space, we will update it to a "MISS"
    {
      board1ref[y][x] = (new ImageView(new Image("images/miss.png", 50, 50, true, true)));    //update the left board with misses if there are no ships on player1's board
      player2board.getOppBoard()[y][x] = 1;  //updates from player 2's view that that location is now a miss, so they cannot click it and shoot there

    }
    board1[y][x].setGraphic(board1ref[y][x]);   //set the graphic of player1's board to the newly updated graphic locations
  }
}   //end the scan function

}  //end boardGUI
