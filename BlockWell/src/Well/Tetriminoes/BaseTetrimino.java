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
public class BaseTetrimino extends Group {
    final protected double fieldSize;
    
    public class Tetrimino2D extends Group {}
    protected  Tetrimino2D tetrimino2D = new Tetrimino2D();
    public Tetrimino2D getTetrimino2D() { return tetrimino2D; }
    
    protected BaseTetrimino(double fieldSize) {
        this.fieldSize = fieldSize;
    }
    protected BaseTetrimino(){
        this(Well.Well.FIELD_SIZE);
    }
    public BaseTetrimino(BaseTetrimino tetrimino){
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
        this.addSquare(posX, posY, posZ);
    }
    
    protected final void addSquare(int posX, int posY, int posZ){
        Rectangle rectangle = new Rectangle(posX * fieldSize, posY * fieldSize, fieldSize, fieldSize);
        rectangle.setFill(Color.LIMEGREEN);
        rectangle.setStroke(Color.BLUE);
        
        if (posZ==0)
            tetrimino2D.getChildren().add(0, rectangle);
        else {
            if (posZ==1) posZ= -2;
            rectangle.setScaleX(Math.pow(0.7, Math.abs(posZ)));
            rectangle.setScaleY(Math.pow(0.7, Math.abs(posZ)));
            tetrimino2D.getChildren().add(rectangle);
        }
    }
}