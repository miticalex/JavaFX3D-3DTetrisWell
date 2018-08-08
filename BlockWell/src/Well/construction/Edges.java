package Well.construction;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/**
 *
 * @author AM
 */
public class Edges extends Group{
    private int width, height;
    
    private final double edgeThickness;
    private final double fieldSize;
    
    public Edges(int width, int height, double edgeThickness, double fieldSize) {
        this.width = width;
        this.height = height;
        this.edgeThickness = edgeThickness;
        this.fieldSize = fieldSize;
        
        for (int i = -1; i < 2*width+1; i++) {
            Box rearEdge = new Box(fieldSize/2, fieldSize/2, edgeThickness);
            rearEdge.setTranslateX(fieldSize*(-0.5*width + 0.25 + 0.5*i));
            rearEdge.setTranslateY(fieldSize*(-0.5*height - 0.25)); // addNodeToGroupGridXYZ(edges, rearEdge, 0.5*i - 0.25, -0.75, -0.5);
            rearEdge.setTranslateZ(-edgeThickness/2);
            
            Box frontEdge = new Box(fieldSize/2, fieldSize/2, edgeThickness);
            frontEdge.setTranslateX(fieldSize*(-0.5*width + 0.25 + 0.5*i));
            frontEdge.setTranslateY(fieldSize*(0.5*height + 0.25)); // addNodeToGroupGridXYZ(edges, frontEdge, 0.5*i - 0.25, height - 0.25, -0.5);
            frontEdge.setTranslateZ(-edgeThickness/2);
            
            this.getChildren().addAll(rearEdge, frontEdge);
        }
        
        for (int i = 0; i < 2*height; i++) {
            Box leftEdge = new Box(fieldSize/2, fieldSize/2, edgeThickness);
            leftEdge.setTranslateX(fieldSize*(-0.5*width - 0.25));
            leftEdge.setTranslateY(fieldSize*(-0.5*height + 0.25 + 0.5*i)); // addNodeToGroupGridXYZ(edges, leftEdge, -0.75, 0.5*i - 0.25, -0.5);
            leftEdge.setTranslateZ(-edgeThickness/2);
            
            Box rightEdge = new Box(fieldSize/2, fieldSize/2, edgeThickness);
            rightEdge.setTranslateX(fieldSize*(0.5*width + 0.25));
            rightEdge.setTranslateY(fieldSize*(-0.5*height + 0.25 + 0.5*i)); //addNodeToGroupGridXYZ(edges, rightEdge, width - 0.25, 0.5*i - 0.25, -0.5);
            rightEdge.setTranslateZ(-edgeThickness/2);
            
            this.getChildren().addAll(leftEdge, rightEdge);
        }
    }
    
    public void setMaterial(PhongMaterial material){
        this.getChildren().forEach(node -> ((Box)node).setMaterial(material));
    }
}
