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
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
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
    
    private static enum Direction {POSITIVE, NEGATIVE};
    private static final Point3D X_AXIS = Rotate.X_AXIS;
    private static final Point3D Y_AXIS = Rotate.Y_AXIS;
    private static final Point3D Z_AXIS = Rotate.Z_AXIS;
    
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
    
    private static final double framePeriod = 1./60.;
    private double time=0;
    private double timeUntilFallingTetriminoDrops;
    
    private Tetrimino fallingTetrimino = null;
    private boolean fallingTetriminoRotates = false;
    
    private boolean paused = false;
    private boolean gameOver = false;
    private int level;
    private int floorsCleared = 0;
    private int blocksCleared = 0;
    private int points = 0;

    public double getTime() { return time; }
    public boolean isPaused() { return paused; }
    public boolean isGameOver() { return gameOver; }
    public int getLevel() { return level; }
    public int getFloorsCleared() { return floorsCleared; }
    public int getBlocksCleared() { return blocksCleared; }
    public int getPoints() { return points; }
    
    public Well(int level, int x, int y, int z) {
        this.level = level>10 ? 10 : (level<1 ? 1 : level);
        timeUntilFallingTetriminoDrops = 10.0/level;
        
        width = x>8 ? 8 : (x<3 ? 3 : x);
        height = y>8 ? 8 : (y<3 ? 3 : y);
        depth = z>20 ? 20 : (z<6 ? 6 : z);
        
        makeWellWalls();
        fallenBlocks = new Box[depth][width][height];
        instantiateFallenBlockMaterials();
        setFallingTetrimino();
        setLights();
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
        this.addNodeToGridXYZ(fallingTetrimino, (width-1)/2, (height-1)/2, 0);
        
        setWallProjection(fallingTetrimino, true);
        
        if (collidesWithFallenBlocks(fallingTetrimino)) gameOver = true;
    }
    
    private void setLights(){
        AmbientLight ambientLight = new AmbientLight(Color.color(0, 0.15, 0.0));
        
        PointLight topLight= new PointLight(Color.WHITE);
        topLight.setTranslateZ(- 1.5*FIELD_SIZE);
        this.getChildren().addAll(ambientLight, topLight);
        for (int i = 1; i < 2; i++) {
            PointLight bottomLight = new PointLight(Color.DARKGRAY);
            bottomLight.setTranslateZ(8*i*FIELD_SIZE);
            this.getChildren().add(bottomLight);
        }
    }
    
    @Override
    public void update() {
        if (gameOver || paused) return;
        
        time += framePeriod;
        timeUntilFallingTetriminoDrops -= framePeriod;
        
        if (timeUntilFallingTetriminoDrops <=0){
            if (moveTetriminoOnGrid(fallingTetrimino, Z_AXIS, Direction.POSITIVE) == false)
                integrateFallingTetrimino();
            timeUntilFallingTetriminoDrops = 10.0/level;
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
                
                this.addNodeToGridXYZ(leftWall[i][j], -0.5 -0.5*WALL_WIDTH/FIELD_SIZE, i, j);
                this.addNodeToGridXYZ(rightWall[i][j], width -0.5 +0.5*WALL_WIDTH/FIELD_SIZE, i, j);
            }
        }
        
        frontWall = new Box[width][depth];
        rearWall = new Box[width][depth];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < depth; j++) {
                frontWall[i][j] = new Box(BOX_SIZE, WALL_WIDTH, BOX_SIZE);
                rearWall[i][j] = new Box(BOX_SIZE, WALL_WIDTH, BOX_SIZE);
                
                this.addNodeToGridXYZ(frontWall[i][j], i, height -0.5 +0.5*WALL_WIDTH/FIELD_SIZE, j);
                this.addNodeToGridXYZ(rearWall[i][j], i, -0.5 -0.5*WALL_WIDTH/FIELD_SIZE, j);
            }   
        }
        
        bottom = new Box[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                bottom[i][j] = new Box(BOX_SIZE, BOX_SIZE, WALL_WIDTH);
                
                this.addNodeToGridXYZ(bottom[i][j], i, j, depth - 0.5 + 0.5*WALL_WIDTH/FIELD_SIZE);
            }   
        }
        
        for (int i = 0; i < width; i++) {
            Box rearEdge = new Box(BOX_SIZE, BOX_SIZE/2, WALL_WIDTH);
            Box frontEdge = new Box(BOX_SIZE, BOX_SIZE/2, WALL_WIDTH);
            
            this.addNodeToGridXYZ(rearEdge, i, -0.75, -0.5);
            this.addNodeToGridXYZ(frontEdge, i, height - 0.25, -0.5);
        }
        
        for (int i = 0; i < height; i++) {
            Box leftEdge = new Box(BOX_SIZE/2, BOX_SIZE, WALL_WIDTH);
            Box rightEdge = new Box(BOX_SIZE/2, BOX_SIZE, WALL_WIDTH);
            
            this.addNodeToGridXYZ(leftEdge, -0.75, i, -0.5);
            this.addNodeToGridXYZ(rightEdge, width - 0.25, i, -0.5);
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
        
        this.addNodeToGridXYZ(corner0, -0.75,           -0.75,          -0.5);
        this.addNodeToGridXYZ(corner1, width - 0.25,    -0.75,          -0.5);
        this.addNodeToGridXYZ(corner2, -0.75,           height - 0.25,  -0.5);
        this.addNodeToGridXYZ(corner3, width - 0.25,    height - 0.25,  -0.5);
    }
    
    public final void moveNodeToGridXYZ(Node node, double x, double y, double z){
        if (node == null) return;
        
        node.setTranslateX(-FIELD_SIZE*width/2 + (x+0.5)*FIELD_SIZE);
        node.setTranslateY(-FIELD_SIZE*height/2 + (y+0.5)*FIELD_SIZE);
        node.setTranslateZ(FIELD_SIZE/2 + z*FIELD_SIZE);
    }
    
    public final void addNodeToGridXYZ(Node node, double x, double y, double z){
        if (node == null) return;
        
        moveNodeToGridXYZ(node, x, y, z);       
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
    
    public final boolean moveTetriminoOnGrid(Tetrimino tetrimino, Point3D axis, Direction direction){
        if (tetrimino == null) 
            return false;
        if ((axis != X_AXIS) && (axis != Y_AXIS) && (axis != Z_AXIS))
            return false;
        if ((axis == Z_AXIS) && (direction == Direction.NEGATIVE))
            return false;
        
        if ((axis == Z_AXIS) && (direction == Direction.POSITIVE) && 
            (getGridIndexZ(tetrimino.getBoundsInParent().getMaxZ()) == depth-1))
            return false;
            
        
        int displacement = direction == direction.POSITIVE ? +1 : -1;
        
        Tetrimino futureTetrimino = new Tetrimino(tetrimino);
        if (axis == X_AXIS)         futureTetrimino.setTranslateX(tetrimino.getTranslateX() + displacement*FIELD_SIZE);
        else if (axis == Y_AXIS)    futureTetrimino.setTranslateY(tetrimino.getTranslateY() + displacement*FIELD_SIZE);
        else /* Z_AXIS */           futureTetrimino.setTranslateZ(tetrimino.getTranslateZ() + displacement*FIELD_SIZE);
        
        if (collidesWithFallenBlocks(futureTetrimino)) return false;
        
        setWallProjection(tetrimino, false);
        if (axis == X_AXIS)         tetrimino.setTranslateX(tetrimino.getTranslateX() + displacement*FIELD_SIZE);
        else if (axis == Y_AXIS)    tetrimino.setTranslateY(tetrimino.getTranslateY() + displacement*FIELD_SIZE);
        else /* Z_AXIS */           tetrimino.setTranslateZ(tetrimino.getTranslateZ() + displacement*FIELD_SIZE);
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
        
        setWallProjection(fallingTetrimino, false);
        
        for (Node node : fallingTetrimino.getChildren()) {
            Box box = (Box)node;
            Point3D boxCoordinatesInWell = fallingTetrimino.localToParent(box.getTranslateX(), box.getTranslateY(), box.getTranslateZ());
            
            int boxX = getGridIndexX(boxCoordinatesInWell.getX());
            int boxY = getGridIndexY(boxCoordinatesInWell.getY());
            int boxZ = getGridIndexZ(boxCoordinatesInWell.getZ());
            
            if (boxZ<0){
                gameOver = true;
                return false;
            }
            
            if (boxX>=0 && boxX<width && boxY>=0 && boxY<height && boxZ>=0 && boxZ<depth){
                fallenBlocks[boxZ][boxX][boxY] = new Box(BOX_SIZE, BOX_SIZE, BOX_SIZE);
                fallenBlocks[boxZ][boxX][boxY].setMaterial(fallenBlocksMaterials[(depth-1 - boxZ) % fallenBlocksMaterials.length]);
                
                this.addNodeToGridXYZ(fallenBlocks[boxZ][boxX][boxY], boxX, boxY, boxZ);
            }
        }
        
        points+=10;
        dropFloors();

        this.getChildren().remove(fallingTetrimino);
        fallingTetrimino.getTransforms().setAll();
        fallingTetrimino = null;
        
        setFallingTetrimino();
        timeUntilFallingTetriminoDrops = 10.0/level;
        
        return true;
    }
    
    private void dropFloors() {
        int floorsClearedAtOnce = 0;
        
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
            
            if (floorFull) {
                clearFloor(i);
                floorsClearedAtOnce++;
            }
        }
        
        if (floorsClearedAtOnce>0)
            points += 50 * (int)Math.pow(2, floorsClearedAtOnce);
    }
    
    private void clearFloor(int i){
        floorsCleared ++; 
        blocksCleared += width*height;
        
        if (blocksCleared % 200 == 0) timeUntilFallingTetriminoDrops = 10.0/++level;
        
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
        // TODO: BUG SPOTTED - TETRIMINO PROJECTION ON THE WALL DOESN'T ALWAYS DISAPPEAR
        
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
        if (gameOver) return;
        
        if (event.getCode() == KeyCode.PAUSE)
            paused = !paused;
        
        if (paused) return;
        
        int fallingMinX = getGridIndexX(fallingTetrimino.getBoundsInParent().getMinX());
        int fallingMaxX = getGridIndexX(fallingTetrimino.getBoundsInParent().getMaxX());
        int fallingMinY = getGridIndexY(fallingTetrimino.getBoundsInParent().getMinY());
        int fallingMaxY = getGridIndexY(fallingTetrimino.getBoundsInParent().getMaxY());
        int fallingMaxZ = getGridIndexZ(fallingTetrimino.getBoundsInParent().getMaxZ());
        
        switch (event.getCode()) {
            case LEFT: case A:
                if (fallingMinX > 0) {
                    moveTetriminoOnGrid(fallingTetrimino, X_AXIS, Direction.NEGATIVE);
                }
                break;
            case RIGHT: case D:
                if (fallingMaxX < width-1){
                    moveTetriminoOnGrid(fallingTetrimino, X_AXIS, Direction.POSITIVE);
                }
                break;
            case UP: case W:
                if (fallingMinY > 0) {
                    moveTetriminoOnGrid(fallingTetrimino, Y_AXIS, Direction.NEGATIVE);
                }
                break;
            case DOWN: case S:
                if (fallingMaxY < height-1) {
                    moveTetriminoOnGrid(fallingTetrimino, Y_AXIS, Direction.POSITIVE);
                }
                break;
            case CONTROL:
                if (fallingMaxZ < depth){
                    if (moveTetriminoOnGrid(fallingTetrimino, Z_AXIS, Direction.POSITIVE) == false)
                        integrateFallingTetrimino();
                }
                break;
            case SPACE:
                while (moveTetriminoOnGrid(fallingTetrimino, Z_AXIS, Direction.POSITIVE));
                integrateFallingTetrimino();
                points+=10;
                break;
                
            case U: case INSERT:    rotateTetrimino(fallingTetrimino, Rotate.Z_AXIS, 90); break;
            case J: case DELETE:    rotateTetrimino(fallingTetrimino, Rotate.Z_AXIS, -90); break; 
            case I: case HOME:      rotateTetrimino(fallingTetrimino, Rotate.Y_AXIS, 90); break;
            case K: case END:       rotateTetrimino(fallingTetrimino, Rotate.Y_AXIS, -90); break;
            case O: case PAGE_UP:   rotateTetrimino(fallingTetrimino, Rotate.X_AXIS, 90); break;
            case L: case PAGE_DOWN: rotateTetrimino(fallingTetrimino, Rotate.X_AXIS, -90); break;
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
