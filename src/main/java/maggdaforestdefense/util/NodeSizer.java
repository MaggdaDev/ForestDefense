/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.util;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

/**
 *
 * @author DavidPrivat
 */
public class NodeSizer<E extends Node> {

    private E node;
    private double width, height;
    private boolean preserveRatio;

    public NodeSizer(E n, double width, double height, boolean preserveRatio) {
        this.node = n;
        this.width = width;
        this.height = height;
        this.preserveRatio = preserveRatio;
        resize();

        maggdaforestdefense.MaggdaForestDefense.getInstance().addOnSceneResize(((arg0, arg1, arg2) -> {

            resize();
        }));
    }

    private final void resize() {
        if (node instanceof Region) {
            Region region = (Region) node;
            if (preserveRatio) {
                double sizeFact = maggdaforestdefense.MaggdaForestDefense.getInstance().getSizeFact();
                region.setPrefSize(width * sizeFact, height * sizeFact);
            } else {
                region.setPrefSize(width * maggdaforestdefense.MaggdaForestDefense.getInstance().getWidthFact(), height * maggdaforestdefense.MaggdaForestDefense.getInstance().getHeightFact());
            }
        } else if(node instanceof ImageView) {
            ImageView iV = (ImageView)node;
            if (preserveRatio) {
                double sizeFact = maggdaforestdefense.MaggdaForestDefense.getInstance().getSizeFact();
                iV.setFitWidth(width * sizeFact);
                iV.setFitHeight(height * sizeFact);
            } else {
                iV.setFitWidth(width * maggdaforestdefense.MaggdaForestDefense.getInstance().getWidthFact());
                iV.setFitHeight(height * maggdaforestdefense.MaggdaForestDefense.getInstance().getHeightFact());
            }
        }

    }
   
    public static double CALC_WIDTH_FROM_HEIGHT(ImageView i) {
        return i.getImage().getWidth() * (i.getFitHeight() / i.getImage().getHeight());
    }
    
    public static double CALC_HEIGHT_FROM_WIDTH(ImageView i) {
        return i.getImage().getHeight() * (i.getFitWidth() / i.getImage().getWidth());
    }
  
}
