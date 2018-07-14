package Well;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Shape3D;

/**
 *
 * @author AM
 */
public class Well extends Group implements Updateable{
    public static final double  FIELD_SIZE = 10.0;
    public static final double  BOX_SIZE = FIELD_SIZE-0.2;
    public static final double  WALL_WIDTH = 0.4;
    
    private final int width, height, depth;
    public int getWidth() { return width;}
    public int getHeight() { return height;}
    public int getDepth() { return depth;}
    
    //private Box[][][] stones;
    private Box[][] leftWall, rightWall;
    private Box[][] frontWall, rearWall;
    private Box[][] bottom;
    
    public Well(int x, int y, int z) {
        width = x>8 ? 8 : (x<3 ? 3 : x);
        height = y>8 ? 8 : (y<3 ? 3 : y);
        depth = z>40 ? 40 : (z<6 ? 6 : z);;
        
        makeWellWalls();
    }
    
    @Override
    public void update() {
        //TODO: WRITE CODE HERE
    }
    
    private void makeWellWalls() {
        PhongMaterial wallMaterial = new PhongMaterial(Color.color(0, 0.1, 1, 0.5));
        
        leftWall = new Box[height][depth];
        rightWall = new Box[height][depth];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < depth; j++) {
                leftWall[i][j] = new Box(WALL_WIDTH, BOX_SIZE, BOX_SIZE);
                rightWall[i][j] = new Box(WALL_WIDTH, BOX_SIZE, BOX_SIZE);
                
                this.addNodeToXYZ(leftWall[i][j], -0.5 -0.5*WALL_WIDTH/FIELD_SIZE, i, j);
                this.addNodeToXYZ(rightWall[i][j], width -0.5 +0.5*WALL_WIDTH/FIELD_SIZE, i, j);
            }
        }
        
        frontWall = new Box[width][depth];
        rearWall = new Box[width][depth];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < depth; j++) {
                frontWall[i][j] = new Box(BOX_SIZE, WALL_WIDTH, BOX_SIZE);
                rearWall[i][j] = new Box(BOX_SIZE, WALL_WIDTH, BOX_SIZE);
                
                this.addNodeToXYZ(frontWall[i][j], i, height -0.5 +0.5*WALL_WIDTH/FIELD_SIZE, j);
                this.addNodeToXYZ(rearWall[i][j], i, -0.5 -0.5*WALL_WIDTH/FIELD_SIZE, j);
            }   
        }
        
        bottom = new Box[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                bottom[i][j] = new Box(BOX_SIZE, BOX_SIZE, WALL_WIDTH);
                
                this.addNodeToXYZ(bottom[i][j], i, j, depth - 0.5 + 0.5*WALL_WIDTH/FIELD_SIZE);
            }   
        }
        
        for (int i = 0; i < width; i++) {
            Box rearEdge = new Box(BOX_SIZE, BOX_SIZE/2, WALL_WIDTH);
            Box frontEdge = new Box(BOX_SIZE, BOX_SIZE/2, WALL_WIDTH);
            
            this.addNodeToXYZ(rearEdge, i, -0.75, -0.5);
            this.addNodeToXYZ(frontEdge, i, height - 0.25, -0.5);
        }
        
        for (int i = 0; i < height; i++) {
            Box leftEdge = new Box(BOX_SIZE/2, BOX_SIZE, WALL_WIDTH);
            Box rightEdge = new Box(BOX_SIZE/2, BOX_SIZE, WALL_WIDTH);
            
            this.addNodeToXYZ(leftEdge, -0.75, i, -0.5);
            this.addNodeToXYZ(rightEdge, width - 0.25, i, -0.5);
        }
        
        this.getChildren().forEach(node -> ((Shape3D) node).setMaterial(wallMaterial));
        
        Box corner0 = new Box(BOX_SIZE/2, BOX_SIZE/2, WALL_WIDTH);
        Box corner1 = new Box(BOX_SIZE/2, BOX_SIZE/2, WALL_WIDTH);
        Box corner2 = new Box(BOX_SIZE/2, BOX_SIZE/2, WALL_WIDTH);
        Box corner3 = new Box(BOX_SIZE/2, BOX_SIZE/2, WALL_WIDTH);
        
        corner0.setMaterial(new PhongMaterial(Color.BLUE));
        corner1.setMaterial(new PhongMaterial(Color.BLUE));
        corner2.setMaterial(new PhongMaterial(Color.BLUE));
        corner3.setMaterial(new PhongMaterial(Color.BLUE));
        
        this.addNodeToXYZ(corner0, -0.75,           -0.75,          -0.5);
        this.addNodeToXYZ(corner1, width - 0.25,    -0.75,          -0.5);
        this.addNodeToXYZ(corner2, -0.75,           height - 0.25,  -0.5);
        this.addNodeToXYZ(corner3, width - 0.25,    height - 0.25,  -0.5);
    }
    
    public final void moveNodeToXYZ(Node node, double x, double y, double z){
        node.setTranslateX(-FIELD_SIZE*width/2 + (x+0.5)*FIELD_SIZE);
        node.setTranslateY(-FIELD_SIZE*height/2 + (y+0.5)*FIELD_SIZE);
        node.setTranslateZ(FIELD_SIZE/2 + z*FIELD_SIZE);
    }
    
    public final void addNodeToXYZ(Node node, double x, double y, double z){
        moveNodeToXYZ(node, x, y, z);       
        this.getChildren().add(node);
    }
}
