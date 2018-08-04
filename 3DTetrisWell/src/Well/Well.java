package Well;

import Well.Tetriminoes.CrossHoleTetrimino;
import Well.Tetriminoes.CrossPolycube;
import Well.Tetriminoes.ITetrimino;
import Well.Tetriminoes.JCrossPolycube;
import Well.Tetriminoes.LTetrimino;
import Well.Tetriminoes.OTetrimino;
import Well.Tetriminoes.SatelitePolycube;
import Well.Tetriminoes.TTetrimino;
import Well.Tetriminoes.TargetPolycube;
import Well.Tetriminoes.Tetrimino;
import Well.Tetriminoes.TowerLeftTetrimino;
import Well.Tetriminoes.TowerRightTetrimino;
import Well.Tetriminoes.TripodTetrimino;
import Well.Tetriminoes.ZTetrimino;
import Well.construction.ConstructionMaterials;
import Well.construction.WellConstruction;
import Well.construction.WellConstruction.WellView;
import java.util.LinkedList;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import pkg3dtetriswell.Main.CameraView;

/**
 *
 * @author AM
 */
public class Well extends Group implements Updateable, EventHandler<KeyEvent>{
    public static enum Skill {ROOKIE, AMATEUR, PROFESSIONAL, GURU}
    private static final int ROOKIE = 5;
    private static final int AMATEUR = 8;
    private static final int PROFESSIONAL = 10;
    private final Skill skill;
    public String getSkill() { return skill.toString(); }
    
    public static enum State {PLAYING, CLEARING, CRITICAL_ROTATION, GAMEOVER};
    private State state;
    public State getState() { return state; }
    
    private boolean paused;
    public boolean isPaused() { return paused; }
    public void setPaused(boolean paused) { this.paused = paused; }
    
    public void setView(WellView wellView, CameraView cameraView) { 
        construction.setView(wellView, cameraView);
        setWallProjection(fallingTetrimino);
    }
    public void changeView(){
        construction.changeView();
        setWallProjection(fallingTetrimino);
    }
    
    private static enum Direction {POSITIVE, NEGATIVE};
    private static final Point3D X_AXIS = Rotate.X_AXIS;
    private static final Point3D Y_AXIS = Rotate.Y_AXIS;
    private static final Point3D Z_AXIS = Rotate.Z_AXIS;
    private static final int    MOVEMENT_DURATION = 300;
    private static final int    MINIMUM_MOVEMENT_DURATION = 100;
    private static final int    SECOND_IN_MILLIS = 1000;
    
    public static final double  FIELD_SIZE = 10.0;
    public static final double  BOX_SIZE = 0.98 * FIELD_SIZE;
    public static final double  WALL_WIDTH = 0.4;
    public static final double  BEAM_WIDTH = 0.2;
    
    public static final Random  RANDOM = new Random();
    
    public static final Tetrimino[] tetriminoes = {
        new ITetrimino(), new LTetrimino(), new OTetrimino(), new TTetrimino(), new ZTetrimino(), //list of basic 2D tetriminoes
        new TripodTetrimino(), new TowerLeftTetrimino(), new TowerRightTetrimino(), 
        new CrossPolycube(), new TargetPolycube(), 
        new CrossHoleTetrimino(), new JCrossPolycube(), new SatelitePolycube()
    };
    
    private final int initialLevel;
    private final int width, height, depth;
    public int getWidth() { return width;}
    public int getHeight() { return height;}
    public int getDepth() { return depth;}
    
    WellConstruction construction; 
    
    private Box[][][] fallenBlocks;
    
    private static final double INITIAL_FALLING_PERIOD = 10.0; // each x secs a tetrimino drops a level
    private static final double SPEEDING_UP_FACTOR = 1.30; // each level speeds up dropping speed by this factor
    private static final int    BLOCKS_TO_CLEAR_PER_LEVEL = 200;
    
    private static final double FRAME_PERIOD = 1./60.;
    private double time;
    private double fallingPeriod;
    private double timeUntilFallingTetriminoDrops;
    
    private Tetrimino fallingTetrimino;
    private Tetrimino futureTetrimino;
    private Tetrimino nextTetrimino;
    private Tetrimino savedTetrimino;
    public Tetrimino getNextTetrimino() { return nextTetrimino; }
    public Tetrimino getSavedTetrimino() { return savedTetrimino; }
    private boolean fallingTetriminoSaved = false;
    
    private int level;
    private int floorsCleared;
    private int blocksCleared;
    private int blocksUntilNextLevel;
    private int points;

    public double getTime() { return time; }
    public int getLevel() { return (level > initialLevel) ? level : initialLevel; }
    public int getFloorsCleared() { return floorsCleared; }
    public int getBlocksCleared() { return blocksCleared; }
    public int getPoints() { return points; }
    public int getFallingTetriminoFloor(){
        if ((fallingTetrimino == null) && (futureTetrimino == null)) return -1;
        
        if (futureTetrimino == null)
            return depth - 1 - getGridIndexZ(fallingTetrimino.getBoundsInParent().getMaxZ() - 0.5*BOX_SIZE);
        else
            return depth - 1 - getGridIndexZ(futureTetrimino.getBoundsInParent().getMaxZ() - 0.5*BOX_SIZE);
    }
    public int getHighestOccupiedFloor(){
        int i;
        for (i = 0; i < fallenBlocks.length; i++) {
            boolean foundOccupiedBlock = false;
            
            for (int j = 0; j < fallenBlocks[i].length; j++) {
                for (int k = 0; k < fallenBlocks[i][j].length; k++) {
                    if (fallenBlocks[i][j][k] != null) {
                        foundOccupiedBlock = true;
                        break;
                    }
                }
                if (foundOccupiedBlock) break;
            }
            if (foundOccupiedBlock) break;
        }
        
        return depth - 1 - i;
    }
    
    public Well(int initialLevel, Skill professionality,  int x, int y, int z) {
        this.skill = professionality;
        this.initialLevel = initialLevel>20 ? 20 : (initialLevel<0 ? 0 : initialLevel); 
        width = x>8 ? 8 : (x<3 ? 3 : x);
        height = y>8 ? 8 : (y<3 ? 3 : y);
        depth = z>20 ? 20 : (z<6 ? 6 : z);
        
        initialise(WellView.REALISTIC_MESH);
    }
    
    public void initialise(WellView initialView){
        this.getChildren().clear();
        
        this.level = 0; // until level reaches initialLevel points and speed will be counted for initialLevel
        setFallingTetriminoDroppingSpeed(getLevel());
        time=0;
        floorsCleared = 0;
        blocksCleared = 0;
        blocksUntilNextLevel = BLOCKS_TO_CLEAR_PER_LEVEL;
        points = 0;
        
        construction = new WellConstruction(width, height, depth, WALL_WIDTH, FIELD_SIZE, initialView);
        this.getChildren().add(construction);
                
        fallenBlocks = new Box[depth][width][height];
        
        savedTetrimino = null;
        setFallingTetrimino(randomTetrimino());
        setNextTetrimino();
        setLights();
        
        state = State.PLAYING;
    }
    
    private void setFallingTetriminoDroppingSpeed(int level){ // EACH LEVEL SPEEDS UP TETRIMINO BY SPEEDING_UP_FACTOR
        fallingPeriod = INITIAL_FALLING_PERIOD/Math.pow(SPEEDING_UP_FACTOR, level);
        timeUntilFallingTetriminoDrops = fallingPeriod;
    }
    
    private void setFallingTetrimino(Tetrimino tetrimino){
        fallingTetriminoSaved = false;
        fallingTetrimino = new Tetrimino(tetrimino);
        
        fallingTetrimino.getTransforms().add(new Rotate(180.0 * RANDOM.nextInt(2), Rotate.X_AXIS));
        fallingTetrimino.getTransforms().add(new Rotate(90.0 * RANDOM.nextInt(4), Rotate.Z_AXIS));
        this.addNodeToGridXYZ(fallingTetrimino, (width-1)/2, (height-1)/2, 0);
        
        setWallProjection(fallingTetrimino);
        
        if (collidesWithFallenBlocks(fallingTetrimino)) state = State.GAMEOVER;
        
        futureTetrimino = new Tetrimino(fallingTetrimino);
    }
    private void setNextTetrimino(){ nextTetrimino = randomTetrimino(); }
    
    private Tetrimino randomTetrimino(){ return tetriminoes[RANDOM.nextInt( 
                skill == skill.ROOKIE ? ROOKIE: 
                skill == skill.AMATEUR ? AMATEUR:
                skill == skill.PROFESSIONAL ? PROFESSIONAL : tetriminoes.length)];
    }
    private void saveTetrimino(){
        if (fallingTetriminoSaved) return;
        
        this.getChildren().remove(fallingTetrimino);
        if (savedTetrimino == null){
            savedTetrimino = fallingTetrimino;
            savedTetrimino.getTransforms().clear();
            setFallingTetrimino(nextTetrimino);
        }
        else {
            Tetrimino tempTetrimino = fallingTetrimino;
            tempTetrimino.getTransforms().clear();
            setFallingTetrimino(savedTetrimino);
            savedTetrimino = tempTetrimino;
        }
        
        fallingTetriminoSaved = true;
    }
    
    public void setWallProjection(Tetrimino tetrimino){
        construction.resetWalls();
        
        for (Node node : tetrimino.getChildren()) {
            Box box = (Box)node;
            Point3D boxCoordinatesInWell = fallingTetrimino.localToParent(box.getTranslateX(), box.getTranslateY(), box.getTranslateZ());
            
            int boxX = getGridIndexX(boxCoordinatesInWell.getX());
            int boxY = getGridIndexY(boxCoordinatesInWell.getY());
            int boxZ = getGridIndexZ(boxCoordinatesInWell.getZ());
            
            if (boxX>=0 && boxX<width && boxY>=0 && boxY<height && boxZ>=0 && boxZ<depth)
                construction.setWallIllumination(boxX, boxY, boxZ);
        }
    }
    
    private void setLights(){
        AmbientLight ambientLight = new AmbientLight(Color.color(0, 0.15, 0.0));
        
        PointLight topLight= new PointLight(Color.WHITE);
        topLight.setTranslateZ(- 1.5*FIELD_SIZE);
        this.getChildren().addAll(ambientLight, topLight);
        
        PointLight bottomLight = new PointLight(Color.DARKGREY);
        bottomLight.setTranslateZ(8*FIELD_SIZE);
        this.getChildren().add(bottomLight);
    }
    
    @Override
    public void update() {
        if ((state == State.GAMEOVER) || (state == State.CLEARING) || (paused)) return;
        
        time += FRAME_PERIOD;
        timeUntilFallingTetriminoDrops -= FRAME_PERIOD;
        
        if (timeUntilFallingTetriminoDrops <=0){
            if (moveFallingTetriminoOnGrid(Z_AXIS, Direction.POSITIVE) == false)
                integrateFallingTetrimino();
            timeUntilFallingTetriminoDrops = fallingPeriod;
        }
    }
    
    public final void moveNodeToGridXYZ(Node node, double x, double y, double z){
        if (node == null) return;
        
        node.setTranslateX(-FIELD_SIZE*width/2d + (x+0.5)*FIELD_SIZE);
        node.setTranslateY(-FIELD_SIZE*height/2d + (y+0.5)*FIELD_SIZE);
        node.setTranslateZ(FIELD_SIZE/2 + z*FIELD_SIZE);
    }
    
    public final void addNodeToGroupGridXYZ(Group group, Node node, double x, double y, double z){
        if (group==null || node==null) return;
        
        moveNodeToGridXYZ(node, x, y, z);       
        group.getChildren().add(node);
    }
    
    public final void addNodeToGridXYZ(Node node, double x, double y, double z){
        addNodeToGroupGridXYZ(this, node, x, y, z);
    }
    
    public final int getGridIndexX(double positionX){
        return (int) Math.floor(( positionX + FIELD_SIZE*width/2d )/FIELD_SIZE);
    }
    
    public final int getGridIndexY(double positionY){
        return (int) Math.floor(( positionY + FIELD_SIZE*height/2d )/FIELD_SIZE);
    }
    
    public final int getGridIndexZ(double positionZ){
        return (int) Math.floor(positionZ/FIELD_SIZE);
    }
    
    public final boolean moveFallingTetriminoOnGrid(Point3D axis, Direction direction){
        if (fallingTetrimino == null) 
            return false;
        if ((axis != X_AXIS) && (axis != Y_AXIS) && (axis != Z_AXIS))
            return false;
        if ((axis == Z_AXIS) && (direction == Direction.NEGATIVE))
            return false;
        
        int displacement = direction == direction.POSITIVE ? +1 : -1;
        
        if (axis == X_AXIS)         
            futureTetrimino.setTranslateX(futureTetrimino.getTranslateX() + displacement*FIELD_SIZE);
        else if (axis == Y_AXIS)    
            futureTetrimino.setTranslateY(futureTetrimino.getTranslateY() + displacement*FIELD_SIZE);
        else /* Z_AXIS */           
            futureTetrimino.setTranslateZ(futureTetrimino.getTranslateZ() + displacement*FIELD_SIZE);
        
        if ((getGridIndexZ(futureTetrimino.getBoundsInParent().getMaxZ() - 0.5*FIELD_SIZE) == depth) || 
                collidesWithFallenBlocks(futureTetrimino)) {
            if (axis == X_AXIS)         futureTetrimino.setTranslateX(futureTetrimino.getTranslateX() - displacement*FIELD_SIZE);
            else if (axis == Y_AXIS)    futureTetrimino.setTranslateY(futureTetrimino.getTranslateY() - displacement*FIELD_SIZE);
            else /* Z_AXIS */           futureTetrimino.setTranslateZ(futureTetrimino.getTranslateZ() - displacement*FIELD_SIZE);
            
            return false;
        }
        construction.resetWalls();
        TranslateTransition translation = new TranslateTransition(
                Duration.millis(Math.min(MOVEMENT_DURATION, Math.max(MINIMUM_MOVEMENT_DURATION, SECOND_IN_MILLIS*fallingPeriod))), fallingTetrimino);
        
        if (axis == X_AXIS)         translation.setToX(futureTetrimino.getTranslateX());
        else if (axis == Y_AXIS)    translation.setToY(futureTetrimino.getTranslateY());
        else /* Z_AXIS */           translation.setToZ(futureTetrimino.getTranslateZ());
        
        translation.play();
        translation.setOnFinished(e -> setWallProjection(fallingTetrimino));
        
        return true;
    }
    
    public void rotateFallingTetrimino(Point3D axis, double angle) {
        if ((state!= State.PLAYING) || (paused)) return;
        if ((axis!=X_AXIS && axis!=Y_AXIS && axis!=Z_AXIS) || (Math.abs(angle) !=90.0)) return;

        futureTetrimino.getTransforms().add(0, new Rotate(angle, axis));
        
        // BOUNDS OF A TETRIMINO AFTER ROTATION
        int futureMinX = getGridIndexX(futureTetrimino.getBoundsInParent().getMinX() + 0.5*FIELD_SIZE);
        int futureMaxX = getGridIndexX(futureTetrimino.getBoundsInParent().getMaxX() - 0.5*FIELD_SIZE);
        int futureMinY = getGridIndexY(futureTetrimino.getBoundsInParent().getMinY() + 0.5*FIELD_SIZE);
        int futureMaxY = getGridIndexY(futureTetrimino.getBoundsInParent().getMaxY() - 0.5*FIELD_SIZE);
        int futureMaxZ = getGridIndexZ(futureTetrimino.getBoundsInParent().getMaxZ() - 0.5*FIELD_SIZE);
        
        // PERFORM NO ROTATION IF IT CAUSES A TETRIMINO TO FALL BELOW THE BOTTOM OF THE WELL OR IF IT CAUSES A COLLISION WITH ANY OF THE FALLEN BLOCKS
        if ((futureMaxZ >= depth) || (collidesWithFallenBlocks(futureTetrimino))){
            futureTetrimino.getTransforms().remove(0);
            return;   
        }
        
        // PERFORM A TRANSLATION ALSO IF A ROTATION CAUSES COLLISIONS WITH WALLS
        double displacementX=0, displacementY=0;
        if (futureMinX < 0)
            displacementX = - futureMinX*FIELD_SIZE;
        else if (futureMaxX >= width)
            displacementX = (width-1 - futureMaxX)*FIELD_SIZE;
        if (futureMinY < 0)
            displacementY = - futureMinY*FIELD_SIZE;
        else if (futureMaxY >= height)
            displacementY = (height-1 - futureMaxY)*FIELD_SIZE;
        
        
        ParallelTransition tetriminoTransition = new ParallelTransition();
        if ((displacementX != 0) || (displacementY != 0)){
            futureTetrimino.setTranslateX(futureTetrimino.getTranslateX() + displacementX);
            futureTetrimino.setTranslateY(futureTetrimino.getTranslateY() + displacementY);
            
            // PERFORM NO ROTATION IF IT CAUSES A COLLISION WITH ANY OF THE FALLEN BLOCKS
            if (collidesWithFallenBlocks(futureTetrimino)){
                futureTetrimino.setTranslateX(futureTetrimino.getTranslateX() - displacementX);
                futureTetrimino.setTranslateY(futureTetrimino.getTranslateY() - displacementY);
                futureTetrimino.getTransforms().remove(0);
                return;   
            }
            
            TranslateTransition translateTransition = new TranslateTransition(Duration.millis(
                    Math.min(MOVEMENT_DURATION, Math.max(MINIMUM_MOVEMENT_DURATION, SECOND_IN_MILLIS*fallingPeriod))), fallingTetrimino);
            translateTransition.setToX(futureTetrimino.getTranslateX());
            translateTransition.setToY(futureTetrimino.getTranslateY()); //PAY ATTENTION TO THIS BUG
            
            state = state.CRITICAL_ROTATION;
            tetriminoTransition.getChildren().add(translateTransition);
        }
        
        construction.resetWalls();
        
        Rotate rotate = new Rotate(0, axis);
        fallingTetrimino.getTransforms().add(0, rotate);

        KeyValue endAngle = new KeyValue(rotate.angleProperty(), angle);
        
        tetriminoTransition.getChildren().add(0, new Timeline(new KeyFrame(Duration.millis(
                Math.min(MOVEMENT_DURATION, Math.max(MINIMUM_MOVEMENT_DURATION, SECOND_IN_MILLIS*fallingPeriod))), endAngle))); //ROTATION
        tetriminoTransition.setInterpolator(Interpolator.LINEAR);
        tetriminoTransition.play();
        tetriminoTransition.setOnFinished(e -> { 
            setWallProjection(fallingTetrimino);
            if (state == State.CRITICAL_ROTATION)
                state = state.PLAYING;
        });
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
        if (futureTetrimino==null || fallingTetrimino==null) return false;
        
        construction.resetWalls();
        
        for (Node node : futureTetrimino.getChildren()) {
            Box box = (Box)node;
            Point3D boxCoordinatesInWell = futureTetrimino.localToParent(box.getTranslateX(), box.getTranslateY(), box.getTranslateZ());
            
            int boxX = getGridIndexX(boxCoordinatesInWell.getX());
            int boxY = getGridIndexY(boxCoordinatesInWell.getY());
            int boxZ = getGridIndexZ(boxCoordinatesInWell.getZ());
            
            if (boxZ<0){
                state = State.GAMEOVER;
                return false;
            }
            
            if (boxX>=0 && boxX<width && boxY>=0 && boxY<height && boxZ>=0 && boxZ<depth){
                fallenBlocks[boxZ][boxX][boxY] = new Box(BOX_SIZE, BOX_SIZE, BOX_SIZE);
                fallenBlocks[boxZ][boxX][boxY].setMaterial(ConstructionMaterials.fallenBlocksMaterials[
                        (depth-1 - boxZ) % ConstructionMaterials.fallenBlocksMaterials.length]);
                
                this.addNodeToGridXYZ(fallenBlocks[boxZ][boxX][boxY], boxX, boxY, boxZ);
            }
        }
        
        points+= 10;
        dropFloorsCheck();

        this.getChildren().remove(fallingTetrimino);
        fallingTetrimino.getTransforms().setAll();
        fallingTetrimino = null;
        
        setFallingTetrimino(nextTetrimino);
        setNextTetrimino();
        timeUntilFallingTetriminoDrops = fallingPeriod;
        
        return true;
    }
    
    private void dropFloorsCheck() {
        
        LinkedList<Integer> floorsToClear = new LinkedList<>();
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
            
            if (floorFull)  
                floorsToClear.add(i); 
        }
        
        if (floorsToClear.size() > 0){
            points+= Math.round(100.0 * Math.pow(3, floorsToClear.size()-1) * Math.pow(SPEEDING_UP_FACTOR, getLevel()));
            state = State.CLEARING;
            clearFloor(floorsToClear, 0);
        }
    }
    
    private void clearFloor(LinkedList<Integer> floorsToClear, int position){
        if (position >= floorsToClear.size()){
            state = State.PLAYING;
            return;
        }
        
        int i = floorsToClear.get(position);
        
        floorsCleared ++; 
        blocksCleared += width*height;
        blocksUntilNextLevel -= width*height;
        
        if (blocksUntilNextLevel <= 0) {
            level++;
            setFallingTetriminoDroppingSpeed(getLevel());
            blocksUntilNextLevel += BLOCKS_TO_CLEAR_PER_LEVEL;
        }
        
        for (int j = 0; j < fallenBlocks[i].length; j++) {
            for (int k = 0; k < fallenBlocks[i][j].length; k++) {
                this.getChildren().remove(fallenBlocks[i][j][k]);
                fallenBlocks[i][j][k] = null;
            }
        }
        
//        state = State.PLAYING;
        ParallelTransition dropping = new ParallelTransition();
        for (int j = i; j >0; j--) {
            fallenBlocks[j] = fallenBlocks[j-1];
            for (int k = 0; k < fallenBlocks[j].length; k++) {
                for (int l = 0; l < fallenBlocks[j][k].length; l++) {
                    if (fallenBlocks[j][k][l] == null) continue;
                    
                    TranslateTransition translateTransition = new TranslateTransition(Duration.millis(SECOND_IN_MILLIS), fallenBlocks[j][k][l]);
                    translateTransition.setByZ(FIELD_SIZE);
                    translateTransition.play();
                    
                    final int j1 = j, k1 = k, l1 = l;
                    translateTransition.setOnFinished(e->{
                        fallenBlocks[j1][k1][l1].setMaterial(ConstructionMaterials.fallenBlocksMaterials[
                                (depth-1 - j1) % ConstructionMaterials.fallenBlocksMaterials.length]);
                    });
                    
                    dropping.getChildren().add(translateTransition);
                }
            }
        }
        dropping.play();
        dropping.setOnFinished(e-> {
            fallenBlocks[0] = new Box[width][height];
            clearFloor(floorsToClear, position+1);
        });
    }
    
    @Override
    public void handle(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        
        if (state == State.GAMEOVER) return;
        
        if (keyCode == KeyCode.PAUSE || keyCode == KeyCode.P || keyCode == KeyCode.F3)
            paused = !paused;
        
        if (((state == State.CLEARING)) || (paused)) return;
        
        int futureMinX = getGridIndexX(futureTetrimino.getBoundsInParent().getMinX() + 0.5*FIELD_SIZE);
        int futureMaxX = getGridIndexX(futureTetrimino.getBoundsInParent().getMaxX() - 0.5*FIELD_SIZE);
        int futureMinY = getGridIndexY(futureTetrimino.getBoundsInParent().getMinY() + 0.5*FIELD_SIZE);
        int futureMaxY = getGridIndexY(futureTetrimino.getBoundsInParent().getMaxY() - 0.5*FIELD_SIZE);
        int futureMaxZ = getGridIndexZ(futureTetrimino.getBoundsInParent().getMaxZ() - 0.5*FIELD_SIZE);
        
        switch (keyCode) {
            case LEFT: case A:
                if (futureMinX > 0) {
                    moveFallingTetriminoOnGrid(X_AXIS, Direction.NEGATIVE);
                }
                break;
            case RIGHT: case D:
                if (futureMaxX < width-1){
                    moveFallingTetriminoOnGrid(X_AXIS, Direction.POSITIVE);
                }
                break;
            case UP: case W:
                if (futureMinY > 0) {
                    moveFallingTetriminoOnGrid(Y_AXIS, Direction.NEGATIVE);
                }
                break;
            case DOWN: case S:
                if (futureMaxY < height-1) {
                    moveFallingTetriminoOnGrid(Y_AXIS, Direction.POSITIVE);
                }
                break;
            case CONTROL:
                if (futureMaxZ < depth){
                    if (moveFallingTetriminoOnGrid(Z_AXIS, Direction.POSITIVE) == false)
                        integrateFallingTetrimino();
                }
                break;
            case SPACE:
                while (moveFallingTetriminoOnGrid(Z_AXIS, Direction.POSITIVE));
                integrateFallingTetrimino();
                points+= 10;
                break;
            case ENTER: saveTetrimino(); break;
                
            case U: case INSERT:    rotateFallingTetrimino(Rotate.Z_AXIS, 90); break;
            case J: case DELETE:    rotateFallingTetrimino(Rotate.Z_AXIS, -90); break; 
            case I: case HOME:      rotateFallingTetrimino(Rotate.Y_AXIS, 90); break;
            case K: case END:       rotateFallingTetrimino(Rotate.Y_AXIS, -90); break;
            case O: case PAGE_UP:   rotateFallingTetrimino(Rotate.X_AXIS, 90); break;
            case L: case PAGE_DOWN: rotateFallingTetrimino(Rotate.X_AXIS, -90); break;
            
            default: break;
        }
    }

}
