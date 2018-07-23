package gameStats;

import Well.Updateable;
import Well.Well;
import Well.Well.State;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
public class GameStats extends Group implements Updateable {
    public static Text gameName = new Text("BlockWell");
    
    private Well well;
    
    private double width;
    private double height;
    private Rectangle background;
    public Rectangle getBackground() {
        return background;
    }
    
    VBox labelsVBox, statsVBox;
    
    private Text dimensionsLabel = new Text("Dimensions: ");
    private Text dimensions = new Text("");
    private Text timeLabel = new Text("Time: ");
    private Text time = new Text("");
    private Text levelLabel = new Text("Level: ");
    private Text level = new Text("");
    private Text pointsLabel = new Text("Points: ");
    private Text points = new Text("");
    private Text linesClearedLabel = new Text("Lines Cleared: ");
    private Text linesCleared = new Text("");
    private Text blocksClearedLabel = new Text("Blocks Cleared: ");
    private Text blocksCleared = new Text("");
    
    private Text gameStateLabel = new Text("Pera");

    public void setDimensionsText(int x, int y, int z) { 
        dimensions.setText(x + "x" + y + "x" + z); 
    }
    public void setTimeText(double time) { 
        int seconds = ((int)time) % 60;
        int minutes = ((int)time) / 60;
        
        if (minutes<60) this.time.setText(
                (minutes<10 ? "0" : "") + minutes + ":" + 
                (seconds<10 ? "0" : "") + seconds);
        else this.time.setText(minutes/60 + ":" + minutes%60 + ":" + seconds);
    }
    private void setStateText(Well.State state) {
        gameStateLabel.setText(state.toString());
        gameStateLabel.setTranslateX(width/2 - gameStateLabel.getBoundsInParent().getWidth()/2);
        
        if (state == State.CLEARING || state == State.PAUSED || 
                state == State.GAMEOVER){
            gameStateLabel.setVisible(true);
        }
        else 
            gameStateLabel.setVisible(false);
    }
    public void setLevelText(int level) { this.level.setText("" + level); }
    public void setPointsText(int points) { this.points.setText("" + points); }
    public void setLinesClearedText(int linesCleared) { this.linesCleared.setText("" + linesCleared); }
    public void setBlocksClearedText(int blocksCleared) { this.blocksCleared.setText("" + blocksCleared); }
    
    public GameStats(Well well, double width, double height) { 
        this.well = well;
        
        this.width = width;
        this.height = height;
        
        setBackGround();
        setLabels();
        setInitialStats();
        setButtons();
    }
    
    private void setBackGround(){
        background = new Rectangle(width, height, Color.BLACK);
        
        gameName.setFont(new Font(50));
        gameName.setTranslateX(width/2 - gameName.getBoundsInParent().getWidth()/2);
        gameName.setTranslateY(70);
        gameName.setFill(Color.TURQUOISE);
        this.getChildren().addAll(background, gameName);
    }
    
    private void setLabels() {
        labelsVBox = new VBox(dimensionsLabel, timeLabel, levelLabel, 
                pointsLabel, linesClearedLabel, blocksClearedLabel);
        labelsVBox.setTranslateY(100);
        labelsVBox.setTranslateX(width/6);
        
        for (Node node : labelsVBox.getChildren()) {
            Text label = (Text)node;
            label.setFont(Font.font(20));
            label.setFill(Color.YELLOW);
        }
        
        this.getChildren().addAll(labelsVBox);
        
        Text pauseLabel = new Text("Pause Button:          PAUSE/P/F3");
        Text newGameLabel = new Text("New Game Button:  F2");
        
        newGameLabel.setFont(Font.font(15));
        newGameLabel.setFill(Color.YELLOW);
        pauseLabel.setFont(Font.font(15));
        pauseLabel.setFill(Color.YELLOW);
        
        VBox otherControls = new VBox(10, newGameLabel, pauseLabel);
        otherControls.setTranslateX(width/12);otherControls.setTranslateY(610);
        this.getChildren().addAll(otherControls);
        
        gameStateLabel.setTranslateY(height/2);
        gameStateLabel.setFont(Font.font(80));
        gameStateLabel.setFill(Color.RED);
        gameStateLabel.setStroke(Color.YELLOW);
        gameStateLabel.setVisible(false);
        this.getChildren().add(gameStateLabel);
    }
    
    private void setInitialStats() {
        statsVBox = new VBox(dimensions, time, level, 
                points, linesCleared, blocksCleared);
        statsVBox.setTranslateY(100);
        statsVBox.setTranslateX(width/2);
        
        for (Node node : statsVBox.getChildren()) {
            Text label = (Text)node;
            label.setFont(Font.font(20));
            label.setFill(Color.YELLOW);
        }
        
        this.getChildren().add(statsVBox);
    }

    private void setButtons() {
        Text rotationButtonsLabel = new Text("Rotation Buttons:");
        rotationButtonsLabel.setTranslateX(width/6);
        rotationButtonsLabel.setTranslateY(400);
        rotationButtonsLabel.setFont(Font.font(20));
        rotationButtonsLabel.setFill(Color.YELLOW);
        
        Rectangle positiveZ = new Rectangle(width/12, 420, width/8, width/8);
        positiveZ.setFill(new ImagePattern(new Image("resources/positiveZ.png")));
        positiveZ.setStroke(Color.color(0.6,0.6,0.6));
        positiveZ.setOnMouseClicked(e-> well.rotateFallingTetrimino(Rotate.Z_AXIS, +90));
        positiveZ.setOnMousePressed(e-> positiveZ.setStroke(Color.color(0.2,0.2,0.2)));
        positiveZ.setOnMouseReleased(e-> positiveZ.setStroke(Color.color(0.6,0.6,0.6)));
        
        Rectangle negativeZ = new Rectangle(width/12, 520, width/8, width/8);
        negativeZ.setFill(new ImagePattern(new Image("resources/negativeZ.png")));
        negativeZ.setStroke(Color.color(0.6,0.6,0.6));
        negativeZ.setOnMouseClicked(e-> well.rotateFallingTetrimino(Rotate.Z_AXIS, -90));
        negativeZ.setOnMousePressed(e-> negativeZ.setStroke(Color.color(0.2,0.2,0.2)));
        negativeZ.setOnMouseReleased(e-> negativeZ.setStroke(Color.color(0.6,0.6,0.6)));
        
        Rectangle positiveY = new Rectangle(3*width/12, 420, width/8, width/8);
        positiveY.setFill(new ImagePattern(new Image("resources/positiveY.png")));
        positiveY.setStroke(Color.color(0.6,0.6,0.6));
        positiveY.setOnMouseClicked(e-> well.rotateFallingTetrimino(Rotate.Y_AXIS, +90));
        positiveY.setOnMousePressed(e-> positiveY.setStroke(Color.color(0.2,0.2,0.2)));
        positiveY.setOnMouseReleased(e-> positiveY.setStroke(Color.color(0.6,0.6,0.6)));
        
        Rectangle negativeY = new Rectangle(3*width/12, 520, width/8, width/8);
        negativeY.setFill(new ImagePattern(new Image("resources/negativeY.png")));
        negativeY.setStroke(Color.color(0.6,0.6,0.6));
        negativeY.setOnMouseClicked(e-> well.rotateFallingTetrimino(Rotate.Y_AXIS, -90));
        negativeY.setOnMousePressed(e-> negativeY.setStroke(Color.color(0.2,0.2,0.2)));
        negativeY.setOnMouseReleased(e-> negativeY.setStroke(Color.color(0.6,0.6,0.6)));
        
        Rectangle positiveX = new Rectangle(5*width/12, 420, width/8, width/8);
        positiveX.setFill(new ImagePattern(new Image("resources/positiveX.png")));
        positiveX.setStroke(Color.color(0.6,0.6,0.6));
        positiveX.setOnMouseClicked(e-> well.rotateFallingTetrimino(Rotate.X_AXIS, +90));
        positiveX.setOnMousePressed(e-> positiveX.setStroke(Color.color(0.2,0.2,0.2)));
        positiveX.setOnMouseReleased(e-> positiveX.setStroke(Color.color(0.6,0.6,0.6)));
        
        Rectangle negativeX = new Rectangle(5*width/12, 520, width/8, width/8);
        negativeX.setFill(new ImagePattern(new Image("resources/negativeX.png")));
        negativeX.setStroke(Color.color(0.6,0.6,0.6));
        negativeX.setOnMouseClicked(e-> well.rotateFallingTetrimino(Rotate.X_AXIS, -90));
        negativeX.setOnMousePressed(e-> negativeX.setStroke(Color.color(0.2,0.2,0.2)));
        negativeX.setOnMouseReleased(e-> negativeX.setStroke(Color.color(0.6,0.6,0.6)));
        
        Text alternativeRotationButtonsLabel = new Text("Alternative controls:");
        alternativeRotationButtonsLabel.setTranslateX(0.62*width);
        alternativeRotationButtonsLabel.setTranslateY(400);
        alternativeRotationButtonsLabel.setFont(Font.font(20));
        alternativeRotationButtonsLabel.setFill(Color.YELLOW);
        
        Text positiveZLabel = new Text("INS \\\n U");
        positiveZLabel.setTranslateX(7*width/12);positiveZLabel.setTranslateY(435);
        positiveZLabel.setFont(Font.font(15));
        positiveZLabel.setFill(Color.YELLOW);
        
        Text negativeZLabel = new Text("DEL \\\n J");
        negativeZLabel.setTranslateX(7*width/12);negativeZLabel.setTranslateY(535);
        negativeZLabel.setFont(Font.font(15));
        negativeZLabel.setFill(Color.YELLOW);
        
        Text positiveYLabel = new Text("HOME \\\n I");
        positiveYLabel.setTranslateX(9*width/12-15);positiveYLabel.setTranslateY(435);
        positiveYLabel.setFont(Font.font(15));
        positiveYLabel.setFill(Color.YELLOW);
        
        Text negativeYLabel = new Text("END \\\n K");
        negativeYLabel.setTranslateX(9*width/12-15);negativeYLabel.setTranslateY(535);
        negativeYLabel.setFont(Font.font(15));
        negativeYLabel.setFill(Color.YELLOW);
        
        Text positiveXLabel = new Text("PGUP \\\n O");
        positiveXLabel.setTranslateX(11*width/12-15);positiveXLabel.setTranslateY(435);
        positiveXLabel.setFont(Font.font(15));
        positiveXLabel.setFill(Color.YELLOW);
        
        Text negativeXLabel = new Text("PGDN \\\n L");
        negativeXLabel.setTranslateX(11*width/12-15);negativeXLabel.setTranslateY(535);
        negativeXLabel.setFont(Font.font(15));
        negativeXLabel.setFill(Color.YELLOW);
        
        
        this.getChildren().addAll(rotationButtonsLabel, alternativeRotationButtonsLabel, 
            positiveZ, negativeZ, positiveY, negativeY, positiveX, negativeX, 
            positiveZLabel, negativeZLabel, positiveYLabel, negativeYLabel, positiveXLabel, negativeXLabel);
    }

    @Override
    public void update() {
        setBlocksClearedText(well.getBlocksCleared());
        setDimensionsText(well.getWidth(), well.getHeight(), well.getDepth());
        setLevelText(well.getLevel());
        setLinesClearedText(well.getFloorsCleared());
        setPointsText(well.getPoints());
        setTimeText(well.getTime());    
        setStateText(well.getState());
    }
}
