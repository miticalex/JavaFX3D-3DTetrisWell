package sidebars;

import Well.Tetriminoes.BaseTetrimino.Tetrimino2D;
import Well.Updateable;
import Well.Well;
import Well.Well.State;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

/**
 *
 * @author AM
 */
public class GameStatsSidebar extends Group implements Updateable {
    public static Text gameName = new Text("BlockWell");
    private static final Color RELEASED_BUTTON_COLOR = Color.color(0.6,0.6,0.6);
    private static final Color PRESSED_BUTTON_COLOR = Color.color(0.2,0.2,0.2);
    
    private final Well well;
    
    private double width;
    private double height;
    private Rectangle background;
    public Rectangle getBackground() {
        return background;
    }
    
    VBox gameStatsLabelsVBox, statsVBox;
    
    private final Text dimensionsLabel = new Text("Dimensions: ");
    private final Text dimensions = new Text("");
    public void setDimensionsText(int x, int y, int z) { 
        dimensions.setText(x + "x" + y + "x" + z); 
    }
    
    private final Text timeLabel = new Text("Time: ");
    private final Text time = new Text("");
    public void setTimeText(double time) { 
        int seconds = ((int)time) % 60;
        int minutes = ((int)time) / 60;
        
        if (minutes<60) this.time.setText(
                (minutes<10 ? "0" : "") + minutes + ":" + 
                (seconds<10 ? "0" : "") + seconds);
        else this.time.setText(minutes/60 + ":" + minutes%60 + ":" + seconds);
    }
    
    private final Text levelLabel = new Text("Level: ");
    private final Text level = new Text("");
    public void setLevelText(int level) { this.level.setText("" + level); }
    
    private final Text pointsLabel = new Text("Points: ");
    private final Text points = new Text("");
    public void setPointsText(int points) { this.points.setText("" + points); }
    
    private final Text floorsClearedLabel = new Text("Floors Cleared: ");
    private final Text floorsCleared = new Text("");
    public void setFloorsClearedText(int floorsCleared) { this.floorsCleared.setText("" + floorsCleared); }
    
    private final Text blocksClearedLabel = new Text("Blocks Cleared: ");
    private final Text blocksCleared = new Text("");
    public void setBlocksClearedText(int blocksCleared) { this.blocksCleared.setText("" + blocksCleared); }
    
    private final Text gameStateLabel = new Text("");
    private void setStateText(Well.State state) {
        gameStateLabel.setText(state.toString());
        if ((state != state.GAMEOVER) && (well.isPaused()))
            gameStateLabel.setText("PAUSED");
        
        gameStateLabel.setTranslateX(width/2 - gameStateLabel.getBoundsInParent().getWidth()/2);
        
        if (state == State.CLEARING || state == State.GAMEOVER || well.isPaused())
            gameStateLabel.setVisible(true);
        else 
            gameStateLabel.setVisible(false);
    }
    
    private final Text newGameLabel = new Text("New Game Button: \t\t\t\t\tF2");
    private final Text pauseLabel = new Text("Pause Button: \t\t\t\t\t\tPause/P/F3");
    private final Text cameraManeuvring = new Text("Camera Maneuvring: \t\t\t\tMouse Drag&Scroll");
    private final Text cameraReset = new Text("Camera Reset: \t\t\t\t\t\t0");
    private final Text adjustCameraLight = new Text("Camera Light Adjustment: \t\t\t1 & 2");
    private final Text fastDrop = new Text("Fast/Slow Drop: \t\t\t\t\tSPACE/CTRL");
    private final Text saveShapeLabel = new Text("Save/Restore Shape: \t\t\t\tENTER");
    private final Text wellViewToggle = new Text("Realistic / Gamer / Mesh View Switch: \t3");
    private final Text cameraViewToggle = new Text("Bird's Eye / Side Camera Switch: \t\tTAB");
    private final Text exitGameLabel = new Text("Exit to Main Menu: \t\t\t\t\tESCAPE");
    
    private final Text skillLabel = new Text("Skill: ");
    private final Text skill = new Text("");
    
    private Tetrimino2D nextTetrimino;
    public final void setNextTetrimino(Tetrimino2D newNextTetrimino) {
        if (nextTetrimino != null) 
            this.getChildren().remove(nextTetrimino);
        
        nextTetrimino = newNextTetrimino;
        nextTetrimino.setScaleX(3);nextTetrimino.setScaleY(3);
        nextTetrimino.setTranslateX(width/2);
        nextTetrimino.setTranslateY(330);
        
        this.getChildren().add(3, nextTetrimino);
    }
    
    public GameStatsSidebar(Well well, double width, double height) { 
        this.well = well;
        
        this.width = width;
        this.height = height;
        
        setBackGround();
        setInitialStats();
        setButtons();
        setNextTetrimino(well.getNextTetrimino().getTetrimino2D());
        setLabels();
    }
    
    private void setBackGround(){
        background = new Rectangle(width, height, Color.BLACK);
        
        gameName.setFont(new Font(50));
        gameName.setTranslateX(width/2 - gameName.getBoundsInParent().getWidth()/2);
        gameName.setTranslateY(70);
        gameName.setFill(Color.TURQUOISE);
        this.getChildren().addAll(background, gameName);
    }
    
    private void setInitialStats() {
        statsVBox = new VBox(dimensions, time, level, 
                points, floorsCleared, blocksCleared, skill);
        statsVBox.setTranslateY(100);
        statsVBox.setTranslateX(width/2);
        
        for (Node node : statsVBox.getChildren()) {
            Text label = (Text)node;
            label.setFont(Font.font(19));
            label.setFill(Color.YELLOW);
        }
        skill.setText(well.getSkill());
        
        this.getChildren().add(statsVBox);
    }
    
    private void setButtons() {
        setLabel(width/6d, 400, Font.font(20), Color.YELLOW, "Rotation Buttons:");
        setLabel(0.62*width, 400, Font.font(20), Color.YELLOW, "Alternative controls:");
        
        setRotationButton(width/12, 420, width/8, width/8, "resources/positiveZ.png", Rotate.Z_AXIS, +90.0);
        setRotationButton(width/12, 520, width/8, width/8, "resources/negativeZ.png", Rotate.Z_AXIS, -90.0);
        setRotationButton(3*width/12, 420, width/8, width/8, "resources/positiveY.png", Rotate.Y_AXIS, +90.0);
        setRotationButton(3*width/12, 520, width/8, width/8, "resources/negativeY.png", Rotate.Y_AXIS, -90.0);
        setRotationButton(5*width/12, 420, width/8, width/8, "resources/positiveX.png", Rotate.X_AXIS, +90.0);
        setRotationButton(5*width/12, 520, width/8, width/8, "resources/negativeX.png", Rotate.X_AXIS, -90.0);
        
        setLabel(7*width/12, 435, Font.font(15), Color.YELLOW, "INS \\\n U");
        setLabel(7*width/12, 535, Font.font(15), Color.YELLOW, "DEL \\\n J");
        setLabel(9*width/12, 435, Font.font(15), Color.YELLOW, "HOME \\\n I");
        setLabel(9*width/12, 535, Font.font(15), Color.YELLOW, "END \\\n K");
        setLabel(11*width/12, 435, Font.font(15), Color.YELLOW, "PGUP \\\n O");
        setLabel(11*width/12, 535, Font.font(15), Color.YELLOW, "PGDN \\\n L");
    }
    
    private void setLabels() {
        gameStatsLabelsVBox = new VBox(dimensionsLabel, timeLabel, levelLabel, 
                pointsLabel, floorsClearedLabel, blocksClearedLabel, skillLabel);
        gameStatsLabelsVBox.setTranslateY(100);
        gameStatsLabelsVBox.setTranslateX(width/6);
        
        for (Node node : gameStatsLabelsVBox.getChildren()) {
            Text label = (Text)node;
            label.setFont(Font.font(19));
            label.setFill(Color.YELLOW);
        }
        
        this.getChildren().addAll(gameStatsLabelsVBox);
        
        Text nextTetriminoLabel = new Text("Next shape:");
        nextTetriminoLabel.setFont(Font.font(20));
        nextTetriminoLabel.setFill(Color.YELLOW);
        nextTetriminoLabel.setTranslateX(width/6);
        nextTetriminoLabel.setTranslateY(330);
        this.getChildren().add(nextTetriminoLabel);
        
        VBox controlsLabelsVBox = new VBox(newGameLabel, pauseLabel, cameraManeuvring, 
                cameraReset, adjustCameraLight, fastDrop, saveShapeLabel, wellViewToggle, cameraViewToggle, exitGameLabel);
        
        controlsLabelsVBox.getChildren().forEach(node -> {
            Text label = (Text)node;
            label.setFont(Font.font(15));
            label.setFill(Color.YELLOW);
        });
        controlsLabelsVBox.setTranslateX(width/6);controlsLabelsVBox.setTranslateY(600);
        this.getChildren().addAll(controlsLabelsVBox);
        
        gameStateLabel.setTranslateY(2*height/3);
        gameStateLabel.setFont(Font.font(80));
        gameStateLabel.setFill(Color.RED);
        gameStateLabel.setStroke(Color.YELLOW);
        gameStateLabel.setVisible(false);
        this.getChildren().add(gameStateLabel);
    }
    
    private void setRotationButton(double x, double y, double width, double height, String imagePath, Point3D axis, double angle){
        Rectangle rotationButton = new Rectangle(x, y, width, height);
        rotationButton.setFill(new ImagePattern(new Image(imagePath)));
        rotationButton.setStroke(RELEASED_BUTTON_COLOR);
        rotationButton.setOnMouseClicked(e-> well.rotateFallingTetrimino(axis, angle));
        rotationButton.setOnMousePressed(e-> rotationButton.setStroke(PRESSED_BUTTON_COLOR));
        rotationButton.setOnMouseReleased(e-> rotationButton.setStroke(RELEASED_BUTTON_COLOR));
        
        this.getChildren().add(rotationButton);
    }
    
    private void setLabel(double x, double y, Font font, Color color, String text){
        Text label = new Text(text);
        label.setTranslateX(x);
        label.setTranslateY(y);
        label.setFont(font);
        label.setFill(color);
        this.getChildren().add(label);
    }

    @Override
    public void update() {
        setBlocksClearedText(well.getBlocksCleared());
        setDimensionsText(well.getWidth(), well.getHeight(), well.getDepth());
        setLevelText(well.getLevel());
        setFloorsClearedText(well.getFloorsCleared());
        setPointsText(well.getPoints());
        setTimeText(well.getTime());    
        setStateText(well.getState());
        setNextTetrimino(well.getNextTetrimino().getTetrimino2D());
    }
}
