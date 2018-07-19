package gameStats;

import Well.Well;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

/**
 *
 * @author AM
 */
public class GameStats extends Group{
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
    private Text rotationButtonsLabel = new Text("Rotation Buttons:");

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
        gameName.setTranslateY(100);
        gameName.setFill(Color.TURQUOISE);
        this.getChildren().addAll(background, gameName);
    }
    
    private void setLabels() {
        labelsVBox = new VBox(dimensionsLabel, timeLabel, levelLabel, 
                pointsLabel, linesClearedLabel, blocksClearedLabel);
        labelsVBox.setTranslateY(200);
        labelsVBox.setTranslateX(width/6);
        
        for (Node node : labelsVBox.getChildren()) {
            Text label = (Text)node;
            label.setFont(Font.font(20));
            label.setFill(Color.YELLOW);
        }
        
        rotationButtonsLabel.setTranslateX(width/6);
        rotationButtonsLabel.setTranslateY(500);
        rotationButtonsLabel.setFont(Font.font(20));
        rotationButtonsLabel.setFill(Color.YELLOW);
        
        this.getChildren().addAll(labelsVBox, rotationButtonsLabel);
    }
    
    private void setInitialStats() {
        statsVBox = new VBox(dimensions, time, level, 
                points, linesCleared, blocksCleared);
        statsVBox.setTranslateY(200);
        statsVBox.setTranslateX(width/2);
        
        for (Node node : statsVBox.getChildren()) {
            Text label = (Text)node;
            label.setFont(Font.font(20));
            label.setFill(Color.YELLOW);
        }
        
        this.getChildren().add(statsVBox);
    }

    private void setRotationButtons() {
        Rectangle positiveZ = new Rectangle(width/6, 520, width/8, width/8);
        positiveZ.setFill(new ImagePattern(new Image("resources/positiveZ.png")));
        positiveZ.setOnMouseClicked(e-> {well.rotateFallingTetrimino(Rotate.Z_AXIS, +90);});
        Text positiveZLabel = new Text("or \nINS\nU");
        positiveZLabel.setTranslateX(width/12);positiveZLabel.setTranslateY(535);
        positiveZLabel.setFont(Font.font(15));
        positiveZLabel.setFill(Color.YELLOW);
        
        Rectangle negativeZ = new Rectangle(width/6, 620, width/8, width/8);
        negativeZ.setFill(new ImagePattern(new Image("resources/negativeZ.png")));
        negativeZ.setOnMouseClicked(e-> {well.rotateFallingTetrimino(Rotate.Z_AXIS, -90);});
        Text negativeZLabel = new Text("or \nDEL\nJ");
        negativeZLabel.setTranslateX(width/12);negativeZLabel.setTranslateY(635);
        negativeZLabel.setFont(Font.font(15));
        negativeZLabel.setFill(Color.YELLOW);
        
        Rectangle positiveY = new Rectangle(3*width/6, 520, width/8, width/8);
        positiveY.setFill(new ImagePattern(new Image("resources/positiveY.png")));
        positiveY.setOnMouseClicked(e-> {well.rotateFallingTetrimino(Rotate.Y_AXIS, +90);});
        Text positiveYLabel = new Text("or \nHOME\nI");
        positiveYLabel.setTranslateX(5*width/12-15);positiveYLabel.setTranslateY(535);
        positiveYLabel.setFont(Font.font(15));
        positiveYLabel.setFill(Color.YELLOW);
        
        Rectangle negativeY = new Rectangle(3*width/6, 620, width/8, width/8);
        negativeY.setFill(new ImagePattern(new Image("resources/negativeY.png")));
        negativeY.setOnMouseClicked(e-> {well.rotateFallingTetrimino(Rotate.Y_AXIS, -90);});
        Text negativeYLabel = new Text("or \nEND\nK");
        negativeYLabel.setTranslateX(5*width/12-15);negativeYLabel.setTranslateY(635);
        negativeYLabel.setFont(Font.font(15));
        negativeYLabel.setFill(Color.YELLOW);
        
        Rectangle positiveX = new Rectangle(5*width/6, 520, width/8, width/8);
        positiveX.setFill(new ImagePattern(new Image("resources/positiveX.png")));
        positiveX.setOnMouseClicked(e-> {well.rotateFallingTetrimino(Rotate.X_AXIS, +90);});
        Text positiveXLabel = new Text("or \nPGUP\nO");
        positiveXLabel.setTranslateX(9*width/12-15);positiveXLabel.setTranslateY(535);
        positiveXLabel.setFont(Font.font(15));
        positiveXLabel.setFill(Color.YELLOW);
        
        Rectangle negativeX = new Rectangle(5*width/6, 620, width/8, width/8);
        negativeX.setFill(new ImagePattern(new Image("resources/negativeX.png")));
        negativeX.setOnMouseClicked(e-> {well.rotateFallingTetrimino(Rotate.X_AXIS, -90);});
        Text negativeXLabel = new Text("or \nPGDN\nL");
        negativeXLabel.setTranslateX(9*width/12-15);negativeXLabel.setTranslateY(635);
        negativeXLabel.setFont(Font.font(15));
        negativeXLabel.setFill(Color.YELLOW);
        
        this.getChildren().addAll(positiveZ, negativeZ, positiveY, negativeY, positiveX, negativeX, 
            positiveZLabel, negativeZLabel, positiveYLabel, negativeYLabel, positiveXLabel, negativeXLabel);
    }
}
