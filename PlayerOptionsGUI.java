import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;



public class PlayerOptionsGUI implements OverScene, EventHandler<ActionEvent>{
    
    
    private Label player1, player2, numOfShip, themeText;
    private TextField name1, name2;
    private Button start;
    
    public PlayerOptionsGUI(String gamemode, Stage s, Font f){
     
        this.gamemode = gamemode;
        
        player1 = new Label("Player 1");
        player1.setFont(f);
        name1 = new TextField("Player 1 Name");
        
        player2 = new Label("Player 2");
        player2.setFont(f);
        name2 = new TextField("Player 2 Name");
        
        numofShip = new Label("Choose number of ships:");
        ObservableList<String> listOfShip = FXCollections.observeableArrayList(
                                            "1",
                                            "2",
                                            "3",
                                            "4",
                                            "5");
        final ComboBox shipNum = new ComboBox(listofShip);
        
        themeText = new Label("Choose theme:");
        ObservableList<String> themeList  = FXCollections.observeableArrayList(
                                            "Default",
                                            "Temp 2",
                                            "Temp 3",
                                            "Star Wars");
        final ComboBox theme = new themeList(listofShip);
        
    }
    
    @Override
    public Scene getScene() {        
        return null;
    }
    
    @Override
	public void handle(ActionEvent e) {
		
        if(e.getSource() == start)
	}
}