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

    private Button[][] board1, board2;
    private ImageView[][] board1ref, board2ref;
    private int shipSelecting = 0;
    private Image image;
    private Stage s;

    private Label player1name, player2name, status, rotateInstr;

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

        this.player1name = new Label(player1name);
        this.player2name = new Label(player2name);
        status = new Label(this.player1name.getText() + " selecting ships");
        status.setWrapText(true);
        rotateInstr = new Label("Press R to rotate piece");


        this.player1name.setFont(f);
        this.player2name.setFont(f);
        rotateInstr.setFont(f);
        status.setFont(f);

        //images code begin

        image = new Image(getClass().getResourceAsStream("images/water.png"));

        ships = new Image[5];
        shipsInOrder = new Image[5];
        shipsVert = new Image[5];

        for (int rep = 0; rep < 5; rep++) {
            ships[rep] = new Image("images/1x" + Integer.toString(rep + 1) + ".png", 50 * (rep + 1), 50, true, true);
        }

        for (int rep = 0; rep < 5; rep++) {
            shipsVert[rep] = new Image("images/" + Integer.toString(rep + 1) + "x1r.png", 50, 50 * (rep + 1), true, true);
        }


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


        gr.setStyle("-fx-background-color: lightslategray;");
        options = new Scene(gr, 1000, 800);


        //The highest level backend object.
        //Controls a majority of the game logic

        // Game game = new Game(numOfShips);

        //two player boards
        player1board = new GameBoard();
        player2board = new GameBoard();


        p1selecting = true;
        flipScreen("");

    }


    /*
     * @ pre none
     *	@ param Player, x and y coordinate of the grid, lenght of the ship
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

        //System.out.println(tempShip.getShipCoordinates().toString());
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
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                board1[y][x].setGraphic(new ImageView(image));
                board2[y][x].setGraphic(new ImageView(image));
            }
        }

        Label close = new Label("ready");

        Stage stage = new Stage();


        if (p1turn)
            close.setText(messageToPlayer + "\n" + this.player1name.getText() + "'s turn in 5 seconds");
        else if (p2turn)
            close.setText(messageToPlayer + "\n" + this.player2name.getText() + "'s turn in 5 seconds");
        else if (p1selecting) {
            close.setText(this.player1name.getText() + " selecting ships in 5 seconds");
            System.out.println("player 1 selecting");
        } else if (p2selecting)
            close.setText(this.player2name.getText() + " selecting ships in 5 seconds");
        if (messageToPlayer.contains("wins"))
            close.setText(messageToPlayer + "\n" + "closing game in 5 seconds");


        Popup pop = new Popup();
        TilePane tilepane = new TilePane();


        tilepane.getChildren().add(close);

        // create a scene 
        Scene scene = new Scene(tilepane, 250, 100);

        stage.setScene(scene);

        stage.setAlwaysOnTop(true);
        stage.show();
        popupActive = true;

        //https://stackoverflow.com/questions/26454149/make-javafx-wait-and-continue-with-code/26454506
        //sleep thread code

        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };

        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if (close.getText().contains("wins"))
                    System.exit(0);
                pop.hide();
                stage.hide();
                popupActive = false;


                if (p1turn) {

                    //transition screen code
                    //buttonSwap( board2, board2Opp);

                    int[][] oppBoard = player1board.getOppBoard();
                    for (int y = 0; y < 8; y++) {
                        for (int x = 0; x < 8; x++) {

                            board1[y][x].setGraphic(board1ref[y][x]);


                            if (oppBoard[y][x] == 0)
                                board2[y][x].setGraphic(new ImageView(image));
                            else if (oppBoard[y][x] == 1)
                                board2[y][x].setGraphic(new ImageView(new Image("images/miss.png", 50, 50, true, true)));
                            else if (oppBoard[y][x] == 2)
                                board2[y][x].setGraphic(new ImageView(new Image("images/hit.png", 50, 50, true, true)));
                            else if (oppBoard[y][x] == 3)
                                board2[y][x].setGraphic(new ImageView(new Image("images/sunk.png", 50, 50, true, true)));

                        }


                    }


                } else if (p2turn) {

                    //transition screen code
                    //buttonSwap( board1, board1Opp);

                    int[][] oppBoard = player2board.getOppBoard();
                    for (int y = 0; y < 8; y++) {
                        for (int x = 0; x < 8; x++) {

                            board2[y][x].setGraphic(board2ref[y][x]);

                            if (oppBoard[y][x] == 0)
                                board1[y][x].setGraphic(new ImageView(image));
                            else if (oppBoard[y][x] == 1)
                                board1[y][x].setGraphic(new ImageView(new Image("images/miss.png", 50, 50, true, true)));
                            else if (oppBoard[y][x] == 2)
                                board1[y][x].setGraphic(new ImageView(new Image("images/hit.png", 50, 50, true, true)));
                            else if (oppBoard[y][x] == 3)
                                board1[y][x].setGraphic(new ImageView(new Image("images/sunk.png", 50, 50, true, true)));

                        }

                    }


                }
                if (p2selecting || p1selecting)
                    options.setCursor(new ImageCursor(ships[0],
                            ships[0].getWidth() / 2,
                            ships[0].getHeight() / 2));

            }


        });

        new Thread(sleeper).start();


    }


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

                    //System.out.println("rotate");

                    //notice the set rotate hehehe ;)
                    if (horizontal) {
                        options.setCursor(new ImageCursor(shipsVert[shipSelecting],
                                shipsVert[shipSelecting].getWidth() / (2 * (shipSelecting + 1)),
                                shipsVert[shipSelecting].getHeight() / (2)));
                    } else if (!horizontal) {
                        options.setCursor(new ImageCursor(ships[shipSelecting],
                                ships[shipSelecting].getWidth() / (2),
                                ships[shipSelecting].getHeight() / (2 * (shipSelecting + 1))));
                    }

                    horizontal = !horizontal;

                }
            }
        });

        if (p1selecting && shipSelecting < 5 && !popupActive) {


            for (int x = 0; x < cols - 1; x++) {
                for (int y = 0; y < rows - 1; y++) {
                    if (e.getSource() == board1[y][x] && shipSelecting < numOfShips && !player1board.isOccupied(x, y, shipSelecting + 1, horizontal)) {

                        placeShips(player1board, x, y, shipSelecting + 1);
                        //player1board.printBoard();

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


                        //board1[y][x].setGraphic(new ImageView(ships[shipSelecting]));
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


                        }

                    } else if (player1board.isOccupied(x, y, shipSelecting, horizontal)) {
                        //System.out.println("Invalid Spot");
                    }

                }

            }

        }

        if (p2selecting && shipSelecting < 5 && !popupActive) {

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


                            status.setText(player1name.getText() + "'s Turn");
                            flipScreen("");


                        }

                    } else if (player2board.isOccupied(x, y, shipSelecting, horizontal)) {
                        //System.out.println("Invalid Spot");
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
                            String str = player2board.fire(x, y);
                            if (str == "Miss") {
                                board2[y][x].setGraphic(new ImageView(new Image("images/miss.png", 50, 50, true, true)));

                                board2ref[y][x] = (new ImageView(new Image("images/miss.png", 50, 50, true, true)));

                                player1board.updateOppBoard(x, y, str);

                                p1turn = false;
                                p2turn = true;
                                if (player2board.gameOver()) {
                                    flipScreen(player1name.getText() + " wins!");
                                } else {
                                    status.setText(player2name.getText() + "'s Turn");
                                    flipScreen("MISSED!");
                                }


                                //you missed
                                //add transition screen code here
                            } else if (str == "Hit") {
                                board2[y][x].setGraphic(new ImageView(new Image("images/hit.png", 50, 50, true, true)));

                                board2ref[y][x] = (new ImageView(new Image("images/hit.png", 50, 50, true, true)));

                                player1board.updateOppBoard(x, y, str);

                                p1turn = false;
                                p2turn = true;
                                if (player2board.gameOver()) {
                                    flipScreen(player1name.getText() + " wins!");
                                } else {
                                    status.setText(player2name.getText() + "'s Turn");
                                    flipScreen("HIT!");
                                }


                                //you hit my battleship
                                //add transition screen code here
                            } else if (str == "Sunk") {
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
                                    flipScreen(player1name.getText() + " wins!");
                                } else {
                                    status.setText(player2name.getText() + "'s Turn");
                                    flipScreen("YOU SUNK MY BATTLESHIP");
                                }


                                //you sunk my battleship
                                //add transition screen code here
                            }


                        }
                    }
                }

            } else if (p2turn) {
                for (int x = 0; x < cols - 1; x++) {
                    for (int y = 0; y < rows - 1; y++) {
                        if (e.getSource() == board1[y][x] && player2board.getOppBoard()[y][x] == 0) {
                            String str = player1board.fire(x, y);
                            if (str == "Miss") {
                                board1[y][x].setGraphic(new ImageView(new Image("images/miss.png", 50, 50, true, true)));

                                board1ref[y][x] = (new ImageView(new Image("images/miss.png", 50, 50, true, true)));

                                player2board.updateOppBoard(x, y, str);
                                p1turn = true;
                                p2turn = false;


                                if (player1board.gameOver()) {
                                    flipScreen(player2name.getText() + " wins!");
                                } else {
                                    status.setText(player1name.getText() + "'s Turn");
                                    flipScreen("MISSED!");
                                }

                                //you missed
                                //add transition screen code here

                            } else if (str == "Hit") {
                                board1[y][x].setGraphic(new ImageView(new Image("images/hit.png", 50, 50, true, true)));

                                board1ref[y][x] = (new ImageView(new Image("images/hit.png", 50, 50, true, true)));

                                player2board.updateOppBoard(x, y, str);
                                p1turn = true;
                                p2turn = false;
                                if (player1board.gameOver()) {
                                    flipScreen(player2name.getText() + " wins!");
                                } else {
                                    status.setText(player1name.getText() + "'s Turn");
                                    flipScreen("HIT!");
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


                                p1turn = true;
                                p2turn = false;

                                if (player1board.gameOver()) {
                                    flipScreen(player2name.getText() + " wins!");
                                } else {
                                    status.setText(player1name.getText() + "'s Turn");
                                    flipScreen("YOU SUNK MY BATTLESHIP!");
                                }


                                //you sunk my battleship
                                //add transition screen code here
                            }


                        }
                    }
                }

            }
            initFire = true;

        }


    }


}
