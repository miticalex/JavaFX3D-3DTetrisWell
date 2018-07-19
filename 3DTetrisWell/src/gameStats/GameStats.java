package gameStats;

import Well.Well;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

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
    
    public GameStats(double width, double height) { 
        this.width = width;
        this.height = height;
        
        setBackGround();
        setLabels();
        setInitialStats();
//        setRotationButtons();
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
        
        this.getChildren().add(labelsVBox);
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

//    private void setRotationButtons() {
//        Rectangle positiveZ = new Rectangle(width/4-20, 600, 40, 40);
//        positiveZ.setOnMouseClicked(value);
//    }
}
