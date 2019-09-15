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
    
    
    public PlayerOptionsGUI(String gamemode, Stage s, Font f){
        
    }
    
    @Override
    public Scene getScene() {        
        return null;
    }
    
    @Override
	public void handle(ActionEvent e) {
		System.out.println("");		
	}
}