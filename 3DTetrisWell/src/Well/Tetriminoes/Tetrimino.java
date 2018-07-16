package Well.Tetriminoes;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.shape.Box;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.transform.Transform;

/**
 *
 * @author AM
 */
public class Tetrimino extends Group {
    final protected double fieldSize;
    
    public Tetrimino(double fieldSize) {
        this.fieldSize = fieldSize;
    }
    public Tetrimino(){
        this(Well.Well.FIELD_SIZE);
    }
    public Tetrimino(Tetrimino tetrimino){
        this(tetrimino.fieldSize);
        
        for (Node node : tetrimino.getChildren()) {
            addBox( (int)Math.floor(node.getTranslateX()/fieldSize), 
                    (int)Math.floor(node.getTranslateY()/fieldSize), 
                    (int)Math.floor(node.getTranslateZ()/fieldSize));
        }
        
        for (Transform transform : tetrimino.getTransforms()) {
            this.getTransforms().add(transform);
        }
        
        this.setTranslateX(tetrimino.getTranslateX());
        this.setTranslateY(tetrimino.getTranslateY());
        this.setTranslateZ(tetrimino.getTranslateZ());
    }
    
    protected final void addBox(int posX, int posY, int posZ){
        PhongMaterial boxMaterial = new PhongMaterial
            (Color.color(0.3, 0.4, 0.3, 0.4));
        boxMaterial.setBumpMap(new Image("resources/cubeBumpMap.png"));
        
        Box box = new Box(0.98*fieldSize, 0.98*fieldSize, 0.98*fieldSize);
        box.setMaterial(boxMaterial);
        box.setCullFace(CullFace.BACK);
        
        box.setTranslateX(posX * fieldSize); 
        box.setTranslateY(posY * fieldSize);
        box.setTranslateZ(posZ * fieldSize);
        
        this.getChildren().add(box);
    }
}
