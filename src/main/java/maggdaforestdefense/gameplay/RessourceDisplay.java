/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderImage;
import javafx.scene.layout.BorderRepeat;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public class RessourceDisplay extends HBox{
    public final static double ICON_SIZE = 40;
    
    protected Label valueLabel;
    private ImageView icon;
    private int value;
    
    
    public RessourceDisplay(GameImage iconGameImage, int value) {
        this.value = value;
        valueLabel = new Label(String.valueOf(value));
        
        icon = new ImageView(iconGameImage.getImage());
        icon.setPreserveRatio(true);
        maggdaforestdefense.MaggdaForestDefense.bindToHeight(icon.fitHeightProperty(), ICON_SIZE);

        
        setAlignment(Pos.CENTER);
        maggdaforestdefense.MaggdaForestDefense.bindToWidth(spacingProperty(), 10);
        super.setPadding(new Insets(0, 20, 0, 20));
        
        
        
  
      

        
        setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(5), Insets.EMPTY)));
        setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
        getChildren().addAll(icon, valueLabel);
        

    }
    
    public void setValue(int d) {
        value = d;
        valueLabel.setText(String.valueOf(d));
    }
    
    public static class EssenceDisplay extends RessourceDisplay {
        private int essence, maxEssence;
        public EssenceDisplay(GameImage iconGameImage, int essence, int maxEssence) {
            super(iconGameImage, essence);
            this.essence = essence;
            this.maxEssence = maxEssence;
        }
        
        @Override
        public void setValue(int v) {
            essence = v;
            valueLabel.setText(essence + " / " + maxEssence);
        }
        
        public void setMaxEssence(int m) {
            maxEssence = m;
            valueLabel.setText(essence + " / " + maxEssence);
        }
        
        
        
    }
    
    public static class LorbeerDisplay extends RessourceDisplay {
        private int lorbeer, maxLorbeer;
        public LorbeerDisplay(GameImage iconGameImage, int lorbeer, int maxLorbeer) {
            super(iconGameImage, lorbeer);
            this.lorbeer = lorbeer;
            this.maxLorbeer = maxLorbeer;
        }
        
        @Override
        public void setValue(int v) {
            lorbeer = v;
            valueLabel.setText(lorbeer + " / " + maxLorbeer);
        }
        
        public void setMaxLorbeer(int m) {
            maxLorbeer = m;
            valueLabel.setText(lorbeer + " / " + maxLorbeer);
        }
        
        
        
    }
}
