package Well.construction;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/**
 *
 * @author AM
 */
public class Bottom extends Group{
    private int width, height;
    
    private final double tileThickness;
    private final double fieldSize;
    
    private Box tiles[][];
    
    Box getTile(int i, int j){
        return tiles[i][j];
    }
    
    public Bottom(int width, int height, int depth, double tileThickness, double fieldSize) {
        this.width = width;
        this.height = height;
        this.tileThickness = tileThickness;
        this.fieldSize = fieldSize;
        
        tiles = new Box[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tiles[i][j] = new Box(fieldSize, fieldSize, tileThickness);
                tiles[i][j].setTranslateX(fieldSize*(-0.5*width + 0.5 + i));
                tiles[i][j].setTranslateY(fieldSize*(-0.5*height + 0.5 + j));
                tiles[i][j].setTranslateZ(fieldSize*depth + 0.5*tileThickness);//addNodeToGroupGridXYZ(bottom, tiles[i][j], i, j, depth - 0.5 + 0.5*WALL_WIDTH/FIELD_SIZE);
                
                this.getChildren().add(tiles[i][j]);
            }   
        }
    }
    
    public void setMaterial(PhongMaterial material){
        this.getChildren().forEach(node -> ((Box)node).setMaterial(material));
    }
}
