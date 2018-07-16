package Well;

import Well.Tetriminoes.ITetrimino;
import Well.Tetriminoes.LTetrimino;
import Well.Tetriminoes.OTetrimino;
import Well.Tetriminoes.TTetrimino;
import Well.Tetriminoes.Tetrimino;
import Well.Tetriminoes.ZTetrimino;
import java.util.Random;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
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
    
    public static final Color[] fallenBlocksColor = {
        Color.color(0.2, 0.1, 0), Color.LIME, Color.RED, Color.PURPLE, Color.color(0.6, 0.4, 0), 
        Color.YELLOW, Color.VIOLET, Color.AQUA 
    };
    
    private final int width, height, depth;
    public int getWidth() { return width;}
    public int getHeight() { return height;}
    public int getDepth() { return depth;}
    
    PhongMaterial wallMaterial; // TRANSPARENT BLUE
    PhongMaterial shiningWallMaterial; // SAME COLOR - WITH RED SELF ILLUMINATION
    
    private Box[][][] fallen;
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
        fallen = new Box[width][height][depth];
    }
    
    @Override
    public void update() {
        if (falling == null){
            falling = tetriminoes[RANDOM.nextInt(tetriminoes.length)];
            this.addNodeToXYZ(falling, (width-1)/2, (height-1)/2, 0);
            
            setWallProjection(falling, true);
        }
    }
    
    private void makeWellWalls() {
        wallMaterial = new PhongMaterial(Color.color(0, 0.1, 1, 0.5)); // TRANSPARENT BLUE
        shiningWallMaterial = new PhongMaterial(Color.color(0, 0.1, 1, 0.5));
        shiningWallMaterial.setSelfIlluminationMap(new Image("resources/red.png"));
        
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
    
    public final void addNodeToXYZ(Node node, double x, double y, double z){
        if (node == null) return;
        
        moveNodeToXYZ(node, x, y, z);       
        this.getChildren().add(node);
    }
    
    public final int getGridIndexX(double positionX){
        return (int) Math.floor(( positionX + FIELD_SIZE*width/2 )/FIELD_SIZE);
    }
    
    public final int getGridIndexY(double positionY){
        return (int) Math.floor(( positionY + FIELD_SIZE*height/2 )/FIELD_SIZE);
    }
    
    public final int getGridIndexZ(double positionZ){
        return (int) Math.floor(positionZ/FIELD_SIZE);
    }
    
    public final boolean moveTetriminoByXYZ(Tetrimino tetrimino, int x, int y, int z){
        if (tetrimino == null) 
            return false;
        if ((z==1) && (getGridIndexZ(tetrimino.getBoundsInParent().getMaxZ()) == depth-1))
            return false;
        
        Tetrimino futureTetrimino = new Tetrimino(tetrimino);
        futureTetrimino.setTranslateX(tetrimino.getTranslateX() + x*FIELD_SIZE);
        futureTetrimino.setTranslateY(tetrimino.getTranslateY() + y*FIELD_SIZE);
        futureTetrimino.setTranslateZ(tetrimino.getTranslateZ() + z*FIELD_SIZE);
        
        if (collidesWithFallen(futureTetrimino)) return false;
        
        setWallProjection(tetrimino, false);
        tetrimino.setTranslateX(tetrimino.getTranslateX() + x*FIELD_SIZE);
        tetrimino.setTranslateY(tetrimino.getTranslateY() + y*FIELD_SIZE);
        tetrimino.setTranslateZ(tetrimino.getTranslateZ() + z*FIELD_SIZE);
        setWallProjection(tetrimino, true);
        
        return true;
    }
    
    public final boolean collidesWithFallen(Tetrimino tetrimino){
        for (Node node : tetrimino.getChildren()) {
            Box box = (Box)node;
            Point3D boxCoordinatesInWell = tetrimino.localToParent(box.getTranslateX(), box.getTranslateY(), box.getTranslateZ());
            
            int boxX = getGridIndexX(boxCoordinatesInWell.getX());
            int boxY = getGridIndexY(boxCoordinatesInWell.getY());
            int boxZ = getGridIndexZ(boxCoordinatesInWell.getZ());
            
            if (boxX>=0 && boxX<width && boxY>=0 && boxY<height && boxZ>=0 && boxZ<depth &&
                    (fallen[boxX][boxY][boxZ] != null)) 
                return true; // A COLLISION EXISTS IFF AT LEAST ONE BOX OVERLAPS ONE OF THE FALLEN BOXES
        }
        
        return false;
    }
    
    public final boolean integrateFalling(){
        if (falling==null) return false;
        
        for (Node node : falling.getChildren()) {
            Box box = (Box)node;
            Point3D boxCoordinatesInWell = falling.localToParent(box.getTranslateX(), box.getTranslateY(), box.getTranslateZ());
            
            int boxX = getGridIndexX(boxCoordinatesInWell.getX());
            int boxY = getGridIndexY(boxCoordinatesInWell.getY());
            int boxZ = getGridIndexZ(boxCoordinatesInWell.getZ());
            
            if (boxX>=0 && boxX<width && boxY>=0 && boxY<height && boxZ>=0 && boxZ<depth){
                PhongMaterial blockMaterial = new PhongMaterial(fallenBlocksColor[(depth-1 - boxZ) % fallenBlocksColor.length]);
                blockMaterial.setSpecularColor(Color.color(0.25, 0.25, 0.25));
                
                fallen[boxX][boxY][boxZ] = new Box(BOX_SIZE, BOX_SIZE, BOX_SIZE);
                fallen[boxX][boxY][boxZ].setMaterial(blockMaterial);
                
                this.addNodeToXYZ(fallen[boxX][boxY][boxZ], boxX, boxY, boxZ);
            }
        }

        setWallProjection(falling, false);
        this.getChildren().remove(falling);
        falling.getTransforms().setAll();
        falling = null;
        
        return true;
    }
    
    public void setWallProjection(Tetrimino tetrimino, boolean set){
        for (Node node : tetrimino.getChildren()) {
            Box box = (Box)node;
            Point3D boxCoordinatesInWell = falling.localToParent(box.getTranslateX(), box.getTranslateY(), box.getTranslateZ());
            
            int boxX = getGridIndexX(boxCoordinatesInWell.getX());
            int boxY = getGridIndexY(boxCoordinatesInWell.getY());
            int boxZ = getGridIndexZ(boxCoordinatesInWell.getZ());
            
            if (boxX>=0 && boxX<width && boxY>=0 && boxY<height && boxZ>=0 && boxZ<depth){
                PhongMaterial material = set ? shiningWallMaterial : wallMaterial;
                leftWall[boxY][boxZ].setMaterial(material);
                rightWall[boxY][boxZ].setMaterial(material);
                frontWall[boxX][boxZ].setMaterial(material);
                rearWall[boxX][boxZ].setMaterial(material);
            }
        }
    }
    
    @Override
    public void handle(KeyEvent event) {
        int fallingMinX = getGridIndexX(falling.getBoundsInParent().getMinX());
        int fallingMaxX = getGridIndexX(falling.getBoundsInParent().getMaxX());
        int fallingMinY = getGridIndexY(falling.getBoundsInParent().getMinY());
        int fallingMaxY = getGridIndexY(falling.getBoundsInParent().getMaxY());
        int fallingMaxZ = getGridIndexZ(falling.getBoundsInParent().getMaxZ());
        
        
        switch (event.getCode()) {
            case LEFT:
                if (fallingMinX > 0) {
                        moveTetriminoByXYZ(falling, -1, 0, 0);
                }
                break;
            case RIGHT:
                if (fallingMaxX < width-1){
                        moveTetriminoByXYZ(falling, +1, 0, 0);
                }
                break;
            case UP:
                if (fallingMinY > 0) {
                        moveTetriminoByXYZ(falling, 0, -1, 0);
                }
                break;
            case DOWN:
                if (fallingMaxY < height-1) {
                        moveTetriminoByXYZ(falling, 0, +1, 0);
                }
                break;
            case CONTROL:
                if (fallingMaxZ < depth){
                    if (moveTetriminoByXYZ(falling, 0, 0, +1) == false)
                        integrateFalling();
                }
                break;
            case SPACE:
                while (moveTetriminoByXYZ(falling, 0, 0, +1));
                integrateFalling();
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
        // PERFORM NO ROTATION IF ONE IS CURRENTLY BEING PERFORMED
        if (fallingRotates) return;
        
        Tetrimino futureTetrimino = new Tetrimino(tetrimino);
        futureTetrimino.getTransforms().add(0, new Rotate(angle, axis));
        
        // BOUNDS OF A TETRIMINO AFTER ROTATION
        int futureMinX = getGridIndexX(futureTetrimino.getBoundsInParent().getMinX());
        int futureMaxX = getGridIndexX(futureTetrimino.getBoundsInParent().getMaxX());
        int futureMinY = getGridIndexY(futureTetrimino.getBoundsInParent().getMinY());
        int futureMaxY = getGridIndexY(futureTetrimino.getBoundsInParent().getMaxY());
        int futureMaxZ = getGridIndexZ(futureTetrimino.getBoundsInParent().getMaxZ()); 
        
        // PERFORM NO ROTATION IF IT CAUSES A TETRIMINO TO FALL BELOW THE BOTTOM OF THE WELL
        if (futureMaxZ >= depth) return; 
        
        // PERFORM NO ROTATION IF IT CAUSES A COLLISION WITH ANY OF THE FALLEN BLOCKS
        if (collidesWithFallen(futureTetrimino)) return;
        
        fallingRotates = true;
        setWallProjection(tetrimino, false);
        
        Rotate rotate = new Rotate(0, axis);
        tetrimino.getTransforms().add(0, rotate);

        KeyValue startAngle = new KeyValue(rotate.angleProperty(), 0);
        KeyValue endAngle = new KeyValue(rotate.angleProperty(), angle);
        Timeline rotateTimeline = new Timeline(new KeyFrame(Duration.millis(100), startAngle, endAngle));

        // PERFORM A TRANSLATION ALSO IF A ROTATION CAUSES COLLISIONS WITH WALLS
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(100), tetrimino);
        
        if (futureMinX < 0)         translateTransition.setByX(-futureMinX*FIELD_SIZE);
        if (futureMaxX >= width)    translateTransition.setByX((width-1 - futureMaxX)*FIELD_SIZE);
        if (futureMinY < 0)         translateTransition.setByY(-futureMinY*FIELD_SIZE);
        if (futureMaxY >= height)   translateTransition.setByY((height-1 - futureMaxY)*FIELD_SIZE);
        
        ParallelTransition parallelTransition = new ParallelTransition(rotateTimeline, translateTransition);
        parallelTransition.setInterpolator(Interpolator.LINEAR);
        parallelTransition.play();
        parallelTransition.setOnFinished(e -> {
            fallingRotates = false;
            setWallProjection(tetrimino, true);
        });
    }
}
