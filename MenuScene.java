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
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;

public class MenuScene implements OverScene, EventHandler<ActionEvent> {

    private Button startPVP, exit; // only one button in this scene
    private Button startAI;
    private Label name, title; //battleship
    private Scene firstscene;

    /*
     * @ pre none
     *	@ param a stage and a font
     *	@ post constuctor
     * @ return none
     */
    public MenuScene(Stage s, Font f) {
        s.centerOnScreen();

        // f = new Font(f.getName(), 50);

        startPVP = new Button();
        startPVP.setText("VERSUS PLAYER");
        startPVP.setOnAction(this);
        startPVP.setFont(Font.font ("Verdana", 25));

        startAI = new Button();
        startAI.setText("VERSUS AI");
        startAI.setOnAction(this);
        startAI.setFont(Font.font ("Verdana", 25));

        exit = new Button();
        exit.setText("Exit");
        exit.setOnAction(e -> s.close());
        exit.setFont(Font.font ("Verdana", 25));

        name = new Label();
        name.setText("Team Poor Yorick\nTeam BigSegfaultEnergy");
        name.setFont(Font.font ("Verdana", 25));

        title = new Label();
        title.setText("Battleship");
        title.setFont(Font.font ("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 75));

        StackPane panes = new StackPane();
        VBox buttons = new VBox(25);
        BorderPane border = new BorderPane();

        border.setStyle("-fx-background-color: lime;");
        border.setBottom(name);

        buttons.getChildren().addAll(title, startPVP, startAI, exit, name);
        buttons.setAlignment(Pos.CENTER);

        panes.getChildren().addAll(border, buttons);
        firstscene = new Scene(panes, 500, 500);
      //  AIscene = new Scene(panes, 500, 500);

    }

    /*
     * @ pre none
     *	@ param none
     *	@ post gets the first scene
     * @ return returns the first scene
     */
    @Override
    public Scene getScene() {
        return firstscene;
        //return AIscene;
    }

    /*
     * @ pre none
     *	@ param action event / button pressed
     *	@ post start button pressed/starts next scene
     * @ return none
     */
    @Override
    public void handle(ActionEvent e) {
        if (e.getSource() == startPVP)
            // this mess uses the static fields and methods from the Menu class to set the next appropriate scene
            BattleshipGUI.nextScene(new PlayerOptionsGUI("pogui", BattleshipGUI.getStage(), BattleshipGUI.getFont()).getScene(), 9);
        else if(e.getSource() == startAI) //TODO have this take you to the ship setup scene for just player versus AI.
        {
          BattleshipGUI.nextScene(new VersusAIGUI("pogui", BattleshipGUI.getStage(), BattleshipGUI.getFont()).getScene(), 9);
        }
    }


}
