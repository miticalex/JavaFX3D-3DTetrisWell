package Well.Tetriminoes;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.shape.Box;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;

/**
 *
 * @author AM
 */
public class Tetrimino extends Group {
    final protected double fieldSize;
    
    private Group appearance2D = new Group();
    public Group get2DAppearance() { return appearance2D; }
    
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
            (Color.color(0.3, 0.4, 0.3, 0.5));
        boxMaterial.setBumpMap(new Image("resources/cubeBumpMap.png"));
        
        Box box = new Box(0.96*fieldSize, 0.96*fieldSize, 0.96*fieldSize);
        box.setMaterial(boxMaterial);
        box.setCullFace(CullFace.BACK);
        
        box.setTranslateX(posX * fieldSize); 
        box.setTranslateY(posY * fieldSize);
        box.setTranslateZ(posZ * fieldSize);
        
        this.getChildren().add(box);
        
        Rectangle rectangle = new Rectangle(posX * fieldSize, posY * fieldSize, fieldSize, fieldSize);
        if (posZ!=0){
            if (posZ==1) posZ= -2;
            rectangle.setScaleX(Math.pow(0.7, Math.abs(posZ)));
            rectangle.setScaleY(Math.pow(0.7, Math.abs(posZ)));
        }
        rectangle.setFill(Color.LIMEGREEN);
        rectangle.setStroke(Color.BLUE);
        appearance2D.getChildren().add(rectangle);
    }
}
