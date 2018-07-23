package gameStats;

import Well.Updateable;
import Well.Well;
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
        setRotationButtons();
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
        
        Text pauseLabel = new Text("Pause Button:      PAUSE/BREAK");
        Text newGameLabel = new Text("Start New Game: ESCAPE");
        pauseLabel.setTranslateX(width/6);
        pauseLabel.setTranslateY(630);
        pauseLabel.setFont(Font.font(20));
        pauseLabel.setFill(Color.YELLOW);
        newGameLabel.setTranslateX(width/6);
        newGameLabel.setTranslateY(670);
        newGameLabel.setFont(Font.font(20));
        newGameLabel.setFill(Color.YELLOW);
        this.getChildren().addAll(pauseLabel, newGameLabel);
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

    private void setRotationButtons() {
        Text rotationButtonsLabel = new Text("Rotation Buttons:");
        rotationButtonsLabel.setTranslateX(width/6);
        rotationButtonsLabel.setTranslateY(400);
        rotationButtonsLabel.setFont(Font.font(20));
        rotationButtonsLabel.setFill(Color.YELLOW);
        
        Rectangle positiveZ = new Rectangle(width/12, 420, width/8, width/8);
        positiveZ.setFill(new ImagePattern(new Image("resources/positiveZ.png")));
        positiveZ.setOnMouseClicked(e-> {well.rotateFallingTetrimino(Rotate.Z_AXIS, +90);});
        
        Rectangle negativeZ = new Rectangle(width/12, 520, width/8, width/8);
        negativeZ.setFill(new ImagePattern(new Image("resources/negativeZ.png")));
        negativeZ.setOnMouseClicked(e-> {well.rotateFallingTetrimino(Rotate.Z_AXIS, -90);});
        
        Rectangle positiveY = new Rectangle(3*width/12, 420, width/8, width/8);
        positiveY.setFill(new ImagePattern(new Image("resources/positiveY.png")));
        positiveY.setOnMouseClicked(e-> {well.rotateFallingTetrimino(Rotate.Y_AXIS, +90);});
        
        Rectangle negativeY = new Rectangle(3*width/12, 520, width/8, width/8);
        negativeY.setFill(new ImagePattern(new Image("resources/negativeY.png")));
        negativeY.setOnMouseClicked(e-> {well.rotateFallingTetrimino(Rotate.Y_AXIS, -90);});
        
        Rectangle positiveX = new Rectangle(5*width/12, 420, width/8, width/8);
        positiveX.setFill(new ImagePattern(new Image("resources/positiveX.png")));
        positiveX.setOnMouseClicked(e-> {well.rotateFallingTetrimino(Rotate.X_AXIS, +90);});
        
        Rectangle negativeX = new Rectangle(5*width/12, 520, width/8, width/8);
        negativeX.setFill(new ImagePattern(new Image("resources/negativeX.png")));
        negativeX.setOnMouseClicked(e-> {well.rotateFallingTetrimino(Rotate.X_AXIS, -90);});
        
        
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
    }
}
