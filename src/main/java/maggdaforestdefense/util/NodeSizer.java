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
    
    public static void setCenterOfImageView(ImageView im, double layoutCenterX, double layoutCenterY) {
        double wOld = im.getImage().getWidth();
        double hOld = im.getImage().getHeight();
        
        double ratio = 0;
        if(hOld != 0) {
            ratio = wOld / hOld;
        }
        
        if(im.getFitWidth() != 0) {
            double wNew = im.getFitWidth();
            double hNew = wNew / ratio;
            im.setLayoutX(layoutCenterX - wNew/2.0d);
            im.setLayoutY(layoutCenterY - hNew/2.0d);
        } else if(im.getFitHeight() != 0) {
            double hNew = im.getFitHeight();
            double wNew = hNew * ratio;
            im.setLayoutX(layoutCenterX - wNew/2.0d);
            im.setLayoutY(layoutCenterY - hNew/2.0d);
        } else {
            im.setLayoutX(layoutCenterX - wOld/2.0d);
            im.setLayoutY(layoutCenterY - hOld/2.0d);
        }
    }
  
}
