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
    
    public static final Color[] fallenBlocksColors = {
        Color.color(0.2, 0.1, 0), Color.LIME, Color.RED, Color.PURPLE, Color.color(0.6, 0.4, 0), 
        Color.YELLOW, Color.VIOLET, Color.AQUA 
    };
    public static final PhongMaterial[] fallenBlocksMaterials = new PhongMaterial[fallenBlocksColors.length];
    
    private final int width, height, depth;
    public int getWidth() { return width;}
    public int getHeight() { return height;}
    public int getDepth() { return depth;}
    
    PhongMaterial wallMaterial; // TRANSPARENT BLUE
    PhongMaterial shiningWallMaterial; // SAME COLOR - WITH RED SELF ILLUMINATION
    
    private Box[][][] fallenBlocks;
    private Box[][] leftWall, rightWall;
    private Box[][] frontWall, rearWall;
    private Box[][] bottom;
    
    private double framePeriod = 1./60.;
    private double time;
    private double timeUntilFallingTetriminoDrops;
    
    private Tetrimino fallingTetrimino = null;
    private boolean fallingTetriminoRotates = false;
    
    private int level;
    private int linesCleared = 0;
    
    public Well(int level, int x, int y, int z) {
        time=0;
        
        this.level = level>10 ? 10 : (level<1 ? 1 : level);
        timeUntilFallingTetriminoDrops = 10/level;
        
        width = x>8 ? 8 : (x<3 ? 3 : x);
        height = y>8 ? 8 : (y<3 ? 3 : y);
        depth = z>20 ? 20 : (z<6 ? 6 : z);
        
        makeWellWalls();
        fallenBlocks = new Box[depth][width][height];
        instantiateFallenBlockMaterials();
        setFallingTetrimino();
    }
    
    private static void instantiateFallenBlockMaterials(){
        for (int i = 0; i < fallenBlocksMaterials.length; i++) {
            fallenBlocksMaterials[i] = new PhongMaterial(fallenBlocksColors[i]);
            fallenBlocksMaterials[i].setSpecularColor(Color.color(0.25, 0.25, 0.25));
            fallenBlocksMaterials[i].setBumpMap(new Image("resources/cubeBumpMap.png"));
        }
    }
    
    private void setFallingTetrimino(){
        fallingTetrimino = tetriminoes[RANDOM.nextInt(tetriminoes.length)];
        fallingTetrimino.getTransforms().add(new Rotate(180.0 * RANDOM.nextInt(2), Rotate.X_AXIS));
        fallingTetrimino.getTransforms().add(new Rotate(90.0 * RANDOM.nextInt(4), Rotate.Z_AXIS));
        this.addNodeToXYZ(fallingTetrimino, (width-1)/2, (height-1)/2, 0);
        
        setWallProjection(fallingTetrimino, true);
    }
    
    @Override
    public void update() {
        time += framePeriod;
        timeUntilFallingTetriminoDrops -= framePeriod;
        
        if (timeUntilFallingTetriminoDrops <=0){
            if (moveTetriminoByXYZ(fallingTetrimino, 0, 0, +1) == false)
                integrateFallingTetrimino();
            timeUntilFallingTetriminoDrops = 10/level;
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
        
        if (collidesWithFallenBlocks(futureTetrimino)) return false;
        
        setWallProjection(tetrimino, false);
        tetrimino.setTranslateX(tetrimino.getTranslateX() + x*FIELD_SIZE);
        tetrimino.setTranslateY(tetrimino.getTranslateY() + y*FIELD_SIZE);
        tetrimino.setTranslateZ(tetrimino.getTranslateZ() + z*FIELD_SIZE);
        setWallProjection(tetrimino, true);
        
        return true;
    }
    
    public final boolean collidesWithFallenBlocks(Tetrimino tetrimino){
        for (Node node : tetrimino.getChildren()) {
            Box box = (Box)node;
            Point3D boxCoordinatesInWell = tetrimino.localToParent(box.getTranslateX(), box.getTranslateY(), box.getTranslateZ());
            
            int boxX = getGridIndexX(boxCoordinatesInWell.getX());
            int boxY = getGridIndexY(boxCoordinatesInWell.getY());
            int boxZ = getGridIndexZ(boxCoordinatesInWell.getZ());
            
            if (boxX>=0 && boxX<width && boxY>=0 && boxY<height && boxZ>=0 && boxZ<depth &&
                    (fallenBlocks[boxZ][boxX][boxY] != null)) 
                return true; // A COLLISION EXISTS IFF AT LEAST ONE BOX OVERLAPS ONE OF THE FALLEN BOXES
        }
        
        return false;
    }
    
    public final boolean integrateFallingTetrimino(){
        if (fallingTetrimino==null) return false;
        
        for (Node node : fallingTetrimino.getChildren()) {
            Box box = (Box)node;
            Point3D boxCoordinatesInWell = fallingTetrimino.localToParent(box.getTranslateX(), box.getTranslateY(), box.getTranslateZ());
            
            int boxX = getGridIndexX(boxCoordinatesInWell.getX());
            int boxY = getGridIndexY(boxCoordinatesInWell.getY());
            int boxZ = getGridIndexZ(boxCoordinatesInWell.getZ());
            
            if (boxX>=0 && boxX<width && boxY>=0 && boxY<height && boxZ>=0 && boxZ<depth){
                fallenBlocks[boxZ][boxX][boxY] = new Box(BOX_SIZE, BOX_SIZE, BOX_SIZE);
                fallenBlocks[boxZ][boxX][boxY].setMaterial(fallenBlocksMaterials[(depth-1 - boxZ) % fallenBlocksMaterials.length]);
                
                this.addNodeToXYZ(fallenBlocks[boxZ][boxX][boxY], boxX, boxY, boxZ);
            }
        }
        
        dropFloors();

        setWallProjection(fallingTetrimino, false);
        this.getChildren().remove(fallingTetrimino);
        fallingTetrimino.getTransforms().setAll();
        fallingTetrimino = null;
        
        setFallingTetrimino();
        timeUntilFallingTetriminoDrops = 10/level;
        
        return true;
    }
    
    private void dropFloors() {
        for (int i=0; i<fallenBlocks.length; i++) {
            boolean floorFull = true; // SET TO TRUE UNTIL PROVEN FALSE
            
            for (int j = 0; j < fallenBlocks[i].length; j++) {
                for (int k = 0; k < fallenBlocks[i][j].length; k++) {
                    if (fallenBlocks[i][j][k] == null){
                        floorFull = false;
                        break;
                    }
                }
                
                if (!floorFull) break;
            }
            
            if (floorFull) clearFloor(i);
        }
    }
    
    private void clearFloor(int i){
        if (++linesCleared % 10 == 0) timeUntilFallingTetriminoDrops = 10/++level;
        
        for (int j = 0; j < fallenBlocks[i].length; j++) {
            for (int k = 0; k < fallenBlocks[i][j].length; k++) {
                this.getChildren().remove(fallenBlocks[i][j][k]);
                fallenBlocks[i][j][k] = null;
            }
        }
        
        for (int j = i; j >0; j--) {
            fallenBlocks[j] = fallenBlocks[j-1];
            for (int k = 0; k < fallenBlocks[j].length; k++) {
                for (int l = 0; l < fallenBlocks[j][k].length; l++) {
                    if (fallenBlocks[j][k][l] == null) continue;
                    
                    fallenBlocks[j][k][l].setTranslateZ(fallenBlocks[j][k][l].getTranslateZ() + FIELD_SIZE);
                    fallenBlocks[j][k][l].setMaterial(fallenBlocksMaterials[(depth-1 - j) % fallenBlocksColors.length]);
                    
                    //TODO: Try to implement translate transition
//                    TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), fallenBlocks[j][k][l]);
//                    translateTransition.setByZ(FIELD_SIZE);
//                    translateTransition.play();
//                    
//                    final int j1 = j, k1 = k, l1 = l;
//                    translateTransition.setOnFinished(e->{
//                        fallenBlocks[j1][k1][l1].setMaterial(fallenBlocksMaterials[(depth-1 - j1) % fallenBlocksColors.length]);
//                    });
                }
            }
        }
        fallenBlocks[0] = new Box[width][height];
    }
    
    public void setWallProjection(Tetrimino tetrimino, boolean set){
        for (Node node : tetrimino.getChildren()) {
            Box box = (Box)node;
            Point3D boxCoordinatesInWell = fallingTetrimino.localToParent(box.getTranslateX(), box.getTranslateY(), box.getTranslateZ());
            
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
        int fallingMinX = getGridIndexX(fallingTetrimino.getBoundsInParent().getMinX());
        int fallingMaxX = getGridIndexX(fallingTetrimino.getBoundsInParent().getMaxX());
        int fallingMinY = getGridIndexY(fallingTetrimino.getBoundsInParent().getMinY());
        int fallingMaxY = getGridIndexY(fallingTetrimino.getBoundsInParent().getMaxY());
        int fallingMaxZ = getGridIndexZ(fallingTetrimino.getBoundsInParent().getMaxZ());
        
        
        switch (event.getCode()) {
            case LEFT:
                if (fallingMinX > 0) {
                        moveTetriminoByXYZ(fallingTetrimino, -1, 0, 0);
                }
                break;
            case RIGHT:
                if (fallingMaxX < width-1){
                        moveTetriminoByXYZ(fallingTetrimino, +1, 0, 0);
                }
                break;
            case UP:
                if (fallingMinY > 0) {
                        moveTetriminoByXYZ(fallingTetrimino, 0, -1, 0);
                }
                break;
            case DOWN:
                if (fallingMaxY < height-1) {
                        moveTetriminoByXYZ(fallingTetrimino, 0, +1, 0);
                }
                break;
            case CONTROL:
                if (fallingMaxZ < depth){
                    if (moveTetriminoByXYZ(fallingTetrimino, 0, 0, +1) == false)
                        integrateFallingTetrimino();
                }
                break;
            case SPACE:
                while (moveTetriminoByXYZ(fallingTetrimino, 0, 0, +1));
                integrateFallingTetrimino();
                break;
                
            case U: rotateTetrimino(fallingTetrimino, Rotate.Z_AXIS, 90); break;
            case J: rotateTetrimino(fallingTetrimino, Rotate.Z_AXIS, -90); break; 
            case I: rotateTetrimino(fallingTetrimino, Rotate.Y_AXIS, 90); break;
            case K: rotateTetrimino(fallingTetrimino, Rotate.Y_AXIS, -90); break;
            case O: rotateTetrimino(fallingTetrimino, Rotate.X_AXIS, 90); break;
            case L: rotateTetrimino(fallingTetrimino, Rotate.X_AXIS, -90); break;
            default: break;
        }
    }

    private void rotateTetrimino(Tetrimino tetrimino, Point3D axis, double angle) {
        // PERFORM NO ROTATION IF ONE IS CURRENTLY BEING PERFORMED
        if (fallingTetriminoRotates) return;
        
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
        if (collidesWithFallenBlocks(futureTetrimino)) return;
        
        fallingTetriminoRotates = true;
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
            fallingTetriminoRotates = false;
            setWallProjection(tetrimino, true);
        });
    }
}
