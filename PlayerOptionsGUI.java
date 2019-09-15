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




public class PlayerOptionsGUI implements OverScene, EventHandler<ActionEvent>{
    
    //Declare variables for the labels and text fields
    private Label player1, player2, numOfShip, themeText;
    private TextField name1, name2;
    private Button start;
    private String gamemode;
    private GridPane gr;
    private Scene options;
    
    public PlayerOptionsGUI(String gamemode, Stage s, Font f){
     
        this.gamemode = gamemode;
        
        //Define the variables
        player1 = new Label("Player 1");
        player1.setFont(f);
        name1 = new TextField("Player 1 Name");
        
        player2 = new Label("Player 2");
        player2.setFont(f);
        name2 = new TextField("Player 2 Name");
        
        numOfShip = new Label("Choose number of ships:");
        ObservableList<String> listOfShip = FXCollections.observableArrayList(
                                            "1",
                                            "2",
                                            "3",
                                            "4",
                                            "5");
        final ComboBox shipNum = new ComboBox(listOfShip);
        shipNum.setValue("1");
        
        themeText = new Label("Choose theme:");
        ObservableList<String> themeList  = FXCollections.observableArrayList(
                                            "Default",
                                            "Temp 2",
                                            "Temp 3",
                                            "Star Wars");
        final ComboBox theme = new ComboBox(themeList);
        theme.setValue("Default");
        
        start = new Button("Start");
        size.setFont(f);
        
        start.setOnAction(this);
        
        
        //Define their placement in the grid
        gr = new GridPane();
        
        gr.add(player1 ,1, 1);
        gr.add(name1, 1, 3);
        
        gr.add(player2, 5, 1);
        gr.add(name2, 3, 3);
        
        gr.add(numOfShip,1, 5);
        gr.add(shipNum, 5, 5);
        gr.add(themeText,1, 10);
        gr.add(theme,10,10);
        gr.add(start, 10, 15);
        
        options = new Scene(gr);
        
    }
    
    @Override
    public Scene getScene() {        
        return options;
    }
    
    @Override
	public void handle(ActionEvent e) {
        
        if(e.getSource() == start){
            
            BattleshipGUI.nextScene( )
        }
	
	}
}