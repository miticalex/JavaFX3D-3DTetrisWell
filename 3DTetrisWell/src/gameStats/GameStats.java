package gameStats;

import javafx.event.EventType;
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
    
    private double width;
    private double height;
    private Rectangle background;
    public Rectangle getBackground() {
        return background;
    }
    
    VBox labelsVBox, statsVBox;
    
    private Text dimensionsLabel = new Text("Dimensions: ");
    private Text dimensions;
    private Text timeLabel = new Text("Time: ");
    private Text time;
    private Text levelLabel = new Text("Level: ");
    private Text level;
    private Text pointsLabel = new Text("Points: ");
    private Text points;
    private Text linesClearedLabel = new Text("Lines Cleared: ");
    private Text linesCleared;
    private Text blocksClearedLabel = new Text("Blocks Cleared: ");
    private Text blocksCleared;
    
    public GameStats(double width, double height) { 
        this.width = width;
        this.height = height;
        
        setBackGround();
        setLabels();
    }
    
    private void setBackGround(){
        background = new Rectangle(width, height, Color.BLACK);
        
        gameName.setFont(new Font(50));
        gameName.setTranslateX(width/2 - gameName.getBoundsInParent().getWidth()/2);
        gameName.setTranslateY(100);
        gameName.setFill(Color.WHITESMOKE);
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
}
