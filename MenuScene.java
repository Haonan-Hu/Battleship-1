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

public class MenuScene implements OverScene, EventHandler<ActionEvent> {

    private Button start, exit; // only one button in this scene
    private Label name,title; //battleship
    private Scene firstscene;


    public MenuScene(Stage s, Font f){
        s.centerOnScreen();
        
        f = new Font(f.getName(), 50);

        start = new Button();
        start.setText("START");
        start.setOnAction(this);
        start.setFont(f);

        exit = new Button();
        exit.setText("Exit");
        exit.setOnAction(e -> s.close());
        exit.setFont(f);

        name = new Label();
        name.setText("Team Poor Yorick");
        name.setFont(f);

        title = new Label();
        title.setText("Battleship");
        title.setFont(f);

        StackPane panes =  new StackPane();
        VBox buttons = new VBox(20);
        BorderPane border = new BorderPane();

        border.setStyle("-fx-background-color: lime;");
        border.setBottom(name);

        buttons.getChildren().addAll(title, start, exit, name);
        buttons.setAlignment(Pos.CENTER);

        panes.getChildren().addAll(border, buttons);
        firstscene = new Scene(panes, 500, 500);

    }

    @Override
	public Scene getScene(){
        return firstscene;
	}

	@Override
	public void handle(ActionEvent e) {
		if(e.getSource() == start){
			// this mess uses the static fields and methods from the Menu clas to set the next appropriate scene
			System.out.println("working");
            BattleshipGUI.nextScene(new PlayerOptionsGUI("pogui", BattleshipGUI.getStage(), BattleshipGUI.getFont()).getScene(), 9);
		}
	}




}
