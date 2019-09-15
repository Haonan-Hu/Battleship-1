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

public class ShipSelectionGUI implements OverScene, EventHandler<ActionEvent>{
    
    
    public ShipSelectionGUI(String gamemode, Stage s, Font f){
        
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