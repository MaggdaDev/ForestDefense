/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.menues;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author DavidPrivat
 */
public abstract class DefaultMenu extends VBox {

    private Label headingLabel;
    private Button backBtn;
    private VBox content;

    public DefaultMenu() {
        
        headingLabel = new Label();
        headingLabel.setFont(MenuManager.headingFont);
        backBtn = new Button("Back");
        backBtn.setOnAction((ActionEvent e) -> {
            MenuManager.getInstance().showPreviousScreen();
        });
        
        content = new VBox();
        content.setSpacing(50);
        
        
        getChildren().addAll(headingLabel, new Separator(), content, new Separator(), backBtn);
    }
    
    public VBox getContentParent() {
        return content;
    }
    
    public void setHeading(String string) {
        headingLabel.setText(string);
    }
}
