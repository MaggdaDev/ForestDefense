/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.ingamemenus;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.gameplay.GameScreen;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author David
 */
public class GrowingWaitingMenu extends VBox {
    private ContentBox treeBox, timerBox;
    private Timer timer;
    public GrowingWaitingMenu(GameImage treeImage, String treeName, double duration) {
        
        ImageView treeView = new ImageView(treeImage.getImage());
        treeView.setPreserveRatio(true);
        treeView.setFitHeight(100);
        
        Label treeLabel = new Label(treeName);
        treeLabel.setFont(UpgradeMenu.font);
        
        treeBox = new ContentBox();
        treeBox.getChildren().addAll(treeLabel, treeView);
        
        timer = new Timer(duration);
        timerBox = new ContentBox();
        timerBox.getChildren().add(timer);
        
        setAlignment(Pos.CENTER);
        setSpacing(10);
        getChildren().addAll(treeBox, timerBox);
    }
    
    public void update(double timeLeft) {
        timer.update(timeLeft);
    }
    
    class Timer extends Label {
        private final Font font = new Font(60);
        
        public Timer(double duration) {
            setFont(font);
            setBackground(new Background(new BackgroundFill(Color.SADDLEBROWN, new CornerRadii(10), Insets.EMPTY)));
            setTextFill(Color.LIGHTGREEN);
            setBorder(new Border(new BorderStroke(Color.DARKGREEN, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(5))));
            
            update(duration);
        }
        
        public void update(double timeLeft) {
            int timeLeftSeconds = (int) timeLeft;

            int minutes = getMinutes(timeLeftSeconds);
            int seconds = getSeconds(timeLeftSeconds);
            
            setText(" " + toNumberString(minutes) + " : " + toNumberString(seconds) + " ");
        }
        
        private int getMinutes(int seconds) {
            return (int)(seconds/60);
        }
        
        private int getSeconds(int seconds) {
            return seconds % 60;
        }
        
        private String toNumberString(int num) {
            String ret = String.valueOf(num);
            while(ret.length() < 2) {
                ret = "0" + ret;
            }
            return ret;
        }
    }
}
