//https://stackoverflow.com/questions/5931933/how-to-make-timer-countdown-along-with-progress-bar
import java.awt.event.*;
import java.awt.Color;
import javax.swing.*;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class transitionScreen {

    Timer timer;
    JProgressBar progressBar;
    private Scene transition;

    public transitionScreen() {

        progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 5);
        progressBar.setValue(0);
        progressBar.setBackground(Color.white);
        progressBar.setForeground(Color.black);
        ActionListener listener = new ActionListener() {
            int counter = 0;
            public void actionPerformed(ActionEvent decreaseTime) {
                counter++;
                progressBar.setValue(counter);
                if (counter == 5) {
                    timer.stop();
                    
                }
            }
        };
        timer = new Timer(1000, listener);
        timer.start();
        JOptionPane.showMessageDialog(null, progressBar, "Get Ready", JOptionPane.DEFAULT_OPTION);
    }

    
}