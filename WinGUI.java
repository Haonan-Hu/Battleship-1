

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/*

Battleship!

KU EECS 448 project 1

Team Name:
Poor Yorick

Team members:
Max Goad
Jace Bayless
Tri Pham
Apurva Rai
Meet Kapadia
*/

//This class serves as a the executive class of Battleship as well as making basic gui elements.

public class WinGUI implements OverScene, EventHandler<ActionEvent>{

    private Button playAgain, exit;
    private Label playerName;
    private Scene lastScene;

    public WinGUI(Stage s, Font f){

        s.centerOnScreen();

        f = new Font(f.getName(), 50);

        playAgain = new Button();
        playAgain.setText("Play Again");
        playAgain.setOnAction(this);
        playAgain.setFont(f);

        exit = new Button();
        exit.setText("Exit");
        exit.setOnAction(e -> s.close());
        exit.setFont(f);

        playerName = new Label();
        playerName.setText("Player _ wins"); // will call a method that retrieves winning player's name from the previous scene
        playerName.setFont(f);

        StackPane panes =  new StackPane();
        VBox buttons = new VBox(20);
        BorderPane border = new BorderPane();

        border.setStyle("-fx-background-color: lime;");
        border.setTop(playerName);

        buttons.getChildren().addAll(playAgain, exit, playerName);
        buttons.setAlignment(Pos.CENTER);

        panes.getChildren().addAll(border, buttons);
        lastScene = new Scene(panes, 500, 500);

    }

    @Override
    public Scene getScene(){
        return lastScene;
    }

    @Override
    public void handle(ActionEvent e) {
        if(e.getSource() == playAgain){
            // this mess uses the static fields and methods from the Menu clas to set the next appropriate scene
            System.out.println("working");
            BattleshipGUI.nextScene(new PlayerOptionsGUI("pogui", BattleshipGUI.getStage(), BattleshipGUI.getFont()).getScene(), 9);
        }
    }
    
}
