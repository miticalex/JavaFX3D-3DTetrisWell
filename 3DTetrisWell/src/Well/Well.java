package Well;

import Well.Tetriminoes.ITetrimino;
import Well.Tetriminoes.LTetrimino;
import Well.Tetriminoes.OTetrimino;
import Well.Tetriminoes.TTetrimino;
import Well.Tetriminoes.Tetrimino;
import Well.Tetriminoes.ZTetrimino;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Shape3D;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 *
 * @author AM
 */
public class Well extends Group implements Updateable, EventHandler<KeyEvent>{
    public static final double  FIELD_SIZE = 10.0;
    public static final double  BOX_SIZE = FIELD_SIZE-0.2;
    public static final double  WALL_WIDTH = 0.4;
    public static final Random  RANDOM = new Random();
    
    public static final Tetrimino[] tetriminoes = {
        new ITetrimino(), new LTetrimino(), new OTetrimino(), new TTetrimino(), new ZTetrimino() //list of basic 2D tetriminoes
    };
    
    private final int width, height, depth;
    public int getWidth() { return width;}
    public int getHeight() { return height;}
    public int getDepth() { return depth;}
    
    //private Box[][][] stones;
    private Box[][] leftWall, rightWall;
    private Box[][] frontWall, rearWall;
    private Box[][] bottom;
    
    private Tetrimino falling = null;
    private boolean fallingRotates = false;
    
    public Well(int x, int y, int z) {
        width = x>8 ? 8 : (x<3 ? 3 : x);
        height = y>8 ? 8 : (y<3 ? 3 : y);
        depth = z>40 ? 40 : (z<6 ? 6 : z);;
        
        makeWellWalls();
    }
    
    @Override
    public void update() {
        if (falling == null){
            falling = tetriminoes[RANDOM.nextInt(tetriminoes.length)];
            this.addNodeToXYZ(falling, (width-1)/2, (height-1)/2, 0);
        }
    }
    
    private void makeWellWalls() {
        PhongMaterial wallMaterial = new PhongMaterial(Color.color(0, 0.1, 1, 0.5)); // TRANSPARENT BLUE
        
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
        if (node == null) return;
        
        node.setTranslateX(-FIELD_SIZE*width/2 + (x+0.5)*FIELD_SIZE);
        node.setTranslateY(-FIELD_SIZE*height/2 + (y+0.5)*FIELD_SIZE);
        node.setTranslateZ(FIELD_SIZE/2 + z*FIELD_SIZE);
    }
    
    public final double getNodeX(Node node){
        return ( node.getTranslateX() + FIELD_SIZE*width/2 )/FIELD_SIZE  -  0.5;
    }
    
    public final double getNodeY(Node node){
        return ( node.getTranslateY() + FIELD_SIZE*height/2 )/FIELD_SIZE  -  0.5;
    }
    
    public final double getNodeZ(Node node){
        return ( node.getTranslateZ() - FIELD_SIZE/2 )/FIELD_SIZE;
    }
    
    public final void moveNodeByXYZ(Node node, double x, double y, double z){
        if (node == null) return;
        
        node.setTranslateX(node.getTranslateX() + x*FIELD_SIZE);
        node.setTranslateY(node.getTranslateY() + y*FIELD_SIZE);
        node.setTranslateZ(node.getTranslateZ() + z*FIELD_SIZE);
    }
    
    public final void addNodeToXYZ(Node node, double x, double y, double z){
        if (node == null) return;
        
        moveNodeToXYZ(node, x, y, z);       
        this.getChildren().add(node);
    }

    @Override
    public void handle(KeyEvent event) {
        int fallingX = (int)(getNodeX(falling));
        int fallingY = (int)(getNodeY(falling));
        int fallingZ = (int)(getNodeZ(falling));
        
        switch (event.getCode()) {
            case LEFT:
                if ((falling.getBoundsInParent().getMinX() - FIELD_SIZE) >
                    (leftWall[fallingY][fallingZ].getBoundsInParent().getMaxX())) {
                        moveNodeByXYZ(falling, -1, 0, 0);
                }
                break;
            case RIGHT:
                if ((falling.getBoundsInParent().getMaxX() + FIELD_SIZE) < 
                    (rightWall[fallingY][fallingZ].getBoundsInParent().getMinX())){
                        moveNodeByXYZ(falling, +1, 0, 0);
                }
                break;
            case UP:
                if ((falling.getBoundsInParent().getMinY() - FIELD_SIZE) > 
                    (rearWall[fallingX][fallingZ].getBoundsInParent().getMaxY())){
                        moveNodeByXYZ(falling, 0, -1, 0);
                }
                break;
            case DOWN:
                if ((falling.getBoundsInParent().getMaxY() + FIELD_SIZE) < 
                    (frontWall[fallingX][fallingZ].getBoundsInParent().getMinY())){
                        moveNodeByXYZ(falling, 0, +1, 0);
                }
                break;
            case CONTROL:
                if ((falling.getBoundsInParent().getMaxZ() + FIELD_SIZE) < 
                    (bottom[fallingX][fallingY].getBoundsInParent().getMinZ())){
                        moveNodeByXYZ(falling, 0, 0, +1);
                }
                break;
            case SPACE:
                while ((falling.getBoundsInParent().getMaxZ() + FIELD_SIZE) < 
                    (bottom[fallingX][fallingY].getBoundsInParent().getMinZ())){
                        moveNodeByXYZ(falling, 0, 0, +1);
                }
                break;
                
            case U: rotateTetrimino(falling, Rotate.Z_AXIS, 90); break;
            case J: rotateTetrimino(falling, Rotate.Z_AXIS, -90); break; 
            case I: rotateTetrimino(falling, Rotate.Y_AXIS, 90); break;
            case K: rotateTetrimino(falling, Rotate.Y_AXIS, -90); break;
            case O: rotateTetrimino(falling, Rotate.X_AXIS, 90); break;
            case L: rotateTetrimino(falling, Rotate.X_AXIS, -90); break;
            default: break;
        }
    }

    private void rotateTetrimino(Tetrimino tetrimino, Point3D axis, double angle) {
        if (!fallingRotates){
            Rotate rotate = new Rotate(0, axis);
            tetrimino.getTransforms().add(0, rotate);

            KeyValue startAngle = new KeyValue(rotate.angleProperty(), 0);
            KeyValue endAngle = new KeyValue(rotate.angleProperty(), angle);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), startAngle, endAngle));

            fallingRotates = true;
            timeline.play();
            timeline.setOnFinished(e -> fallingRotates = false);
        }
    }
}
