

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.text.*;
import javafx.scene.image.Image;


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

public class BattleshipGUI extends Application{

    private static Stage stage1;
    private static Font font1;
    
	public static void main(String[] args){
		launch(args);
	}
    
    @Override 
    public void start(Stage primaryStage){
        
        stage1 = primaryStage;
        stage1.setTitle("Battleship!");
        
		Button btn = new Button();
		btn.setText("Play Game'");
		btn.setOnAction(new EventHandler<ActionEvent>(){
			
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Hello World");
				
			}
			
		});
        
        font1 = new Font("Georgia", 20);
        
        stage1.getIcons().add(new Image(getClass().getResourceAsStream("icon.jpg")));
        //https://media.istockphoto.com/vectors/aircraft-carrier-transportation-cartoon-character-side-view-vector-vector-id981633486
        
        
		
		StackPane root = new StackPane();
		root.getChildren().add(btn);
		stage1.setScene(new Scene(root, 300, 250));
		stage1.show();
        stage1.setAlwaysOnTop(false);
	}
    
    public static Stage getStage()
    {
    	return stage1;
    }
    
    public void changeTitle(String title)
    {
    	stage1.setTitle(title);
    }
    
    public static Font getFont()
    {
    	return font1;
    }  
    
}
