package Well.Tetriminoes;

import javafx.scene.Group;
import javafx.scene.shape.Box;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;

/**
 *
 * @author AM
 */
public abstract class Tetrimino extends Group {
    final protected double fieldSize;
    
    public Tetrimino(double fieldSize) {
        this.fieldSize = fieldSize;
    }
    public Tetrimino(){
        this(Well.Well.FIELD_SIZE);
    }
    
    protected final void addBox(int posX, int posY, int posZ){
        PhongMaterial boxMaterial = new PhongMaterial
            (Color.color(0.4, 0.5, 0.4, 0.2));
        
        Box box = new Box(0.99*fieldSize, 0.99*fieldSize, 0.99*fieldSize);
        box.setMaterial(boxMaterial);
        box.setCullFace(CullFace.NONE);
        
        box.setTranslateX(posX * fieldSize); 
        box.setTranslateY(posY * fieldSize);
        box.setTranslateZ(posZ * fieldSize);
        
        this.getChildren().add(box);
    }
}
