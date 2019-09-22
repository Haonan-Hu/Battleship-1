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





public class BoardGUI implements OverScene, EventHandler<ActionEvent>{
    
    //Declare variables for the labels and text fields
    private Label a;
    private Button start;
    private String gamemode;
    private GridPane player1, player2,gr;
    private Scene options;
    private int rows = 9, cols = 9;
    
    public BoardGUI(String gamemode, Stage s, Font f){
     
        this.gamemode = gamemode;
        
        Image image = new Image(getClass().getResourceAsStream("images/water.png"));
        
        
        
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
        
        String str = "ABCDEFGH";
        String str2 = "12345678";
        
        for(int c = 0; c < cols-1; c++){
            for(int r = 0; r < rows-1; r++){
                
                Button b1 = new Button();
                Button b2 = new Button();
                b1.setGraphic(new ImageView(image));
                b2.setGraphic(new ImageView(image));
                b1.setPrefSize(10,10);
                b2.setPrefSize(10,10);
                
                player1.add(b1, c+1, r+1);
                player2.add(b2, c+1, r+1);
                
                player1.setHalignment(b1, HPos.CENTER );
                player2.setHalignment(b2, HPos.CENTER );
                
                Text t_row1 = new Text(str2.substring(r,r+1));
                Text t_row2 = new Text(str2.substring(r,r+1));
                
                player1.add(t_row1, 0, r+1);
                player2.add(t_row2, 0, r+1);
                
                player1.setHalignment(t_row1, HPos.CENTER );
                player2.setHalignment(t_row2, HPos.CENTER );
                
                
            }
            //letters above columns
            Text t_col1 = new Text(str.substring(c,c+1));
            Text t_col2 = new Text(str.substring(c,c+1));
           
            player1.add(t_col1, c+1, 0);
            player2.add(t_col2, c+1, 0);
            
            player1.setHalignment(t_col1, HPos.CENTER );
            player2.setHalignment(t_col2, HPos.CENTER );
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
        
        player1.setHgap(0);
        player2.setHgap(0);
        
         player1.setVgap(0);
        player2.setVgap(0);
        
       
        gr.getRowConstraints().add(r1);
        
        gr.setConstraints(player1, 0,1);
        gr.setConstraints(player2, 2,1);
        
        gr.getChildren().add(player1);
        gr.getChildren().add(player2);
        
        
        gr.setStyle("-fx-background-color: lightslategray;");
        options = new Scene(gr, 1400, 800);
        
        
    }
    
    @Override
    public Scene getScene() {        
        return options;
    }
    
    @Override
	public void handle(ActionEvent e) {
        
	
	}
}