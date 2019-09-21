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





public class BoardGUI implements OverScene, EventHandler<ActionEvent>{
    
    //Declare variables for the labels and text fields
    private Label a;
    private Button start;
    private String gamemode;
    private GridPane player1, player2,gr;
    private Scene options;
    private int rows = 8, cols = 8;
    
    public BoardGUI(String gamemode, Stage s, Font f){
     
        this.gamemode = gamemode;
        
        
        //Define their placement in the grid
        
        gr = new GridPane();
            
        player1 = new GridPane();
        
        player2 = new GridPane();
        
        //make columns for each board

        for(int x = 0; x < cols; x++){
            ColumnConstraints c = new ColumnConstraints();
            c.setPercentWidth(100.0/cols);
            player1.getColumnConstraints().add(c);
            player2.getColumnConstraints().add(c);
        }
        
        //rows for each board
        
        for(int y = 0; y< rows; y++){
            RowConstraints r = new RowConstraints();
            r.setPercentHeight(100.0/rows);
            player1.getRowConstraints().add(r);
            player2.getRowConstraints().add(r);
        }
        
        
        for(int c = 0; c < cols; c++){
            for(int r = 0; r < rows; r++){
                player1.add(new Rectangle(20, 20), c, r);
                player2.add(new Rectangle(20, 20), c, r);
            }
        }
        
        /*
        for(int cc = 0; cc < 3; cc++){
            ColumnConstraints c = new ColumnConstraints();
            c.setPercentWidth(100.0/3);
            gr.getColumnConstraints().add(c);
        }       
        */
        
        ColumnConstraints c1 = new ColumnConstraints();
            c1.setPercentWidth(46);
            gr.getColumnConstraints().add(c1);
         ColumnConstraints c2 = new ColumnConstraints();
            c2.setPercentWidth(8);
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
        
        player1.setHgap(10);
        player2.setHgap(10);
        
         player1.setVgap(10);
        player2.setVgap(20);
        
       
        gr.getRowConstraints().add(r1);
        
        gr.setConstraints(player1, 0,1);
        gr.setConstraints(player2, 2,1);
        
        gr.getChildren().add(player1);
        gr.getChildren().add(player2);
        
        
        gr.setStyle("-fx-background-color: lightslategray;");
        options = new Scene(gr);
        
        
    }
    
    @Override
    public Scene getScene() {        
        return options;
    }
    
    @Override
	public void handle(ActionEvent e) {

	
	}
}