package Well.construction;

import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/**
 *
 * @author AM
 */
public class Wall extends Group{
    private final int width, depth;
    
    private final double wallWidth;
    private final double fieldSize;
    
    private final Box[][] blocks;
    
    public Wall(int width, int depth, double wallWidth, double fieldSize) {
        this.width = width;
        this.depth = depth;
        this.wallWidth = wallWidth;
        this.fieldSize = fieldSize;
        
        blocks = new Box[width][depth];
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < depth; j++) {
                blocks[i][j] = new Box(fieldSize, wallWidth, fieldSize);
                blocks[i][j].setTranslateX(fieldSize*(-width/2d + 0.5 + i));
                blocks[i][j].setTranslateZ(fieldSize*(0.5 + j));
                
                this.getChildren().add(blocks[i][j]);
            }
        }
    }
    
    public void rotateBlocks(double angle){
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < depth; j++) {
                blocks[i][j].setRotate(angle);
            }
        }
    }
    
    public void setMaterial(PhongMaterial material){
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < depth; j++) {
                blocks[i][j].setMaterial(material);
            }
        }
    }
    
    public void setBlockMaterial(PhongMaterial material, int i, int j){
        blocks[i][j].setMaterial(material);
    }
}
