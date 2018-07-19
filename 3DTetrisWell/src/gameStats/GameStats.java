package gameStats;

import javafx.event.EventType;
import javafx.scene.Group;
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
    
    VBox vBox1 = new VBox(10), vBox2 = new VBox(10);
    
    private Text dimensionsLabel;
    private Text dimensions;
    private Text timeLabel;
    private Text time;
    private Text levelLabel;
    private Text level;
    private Text pointsLabel;
    private Text points;
    private Text linesClearedLabel;
    private Text linesCleared;
    private Text blocksClearedLabel;
    private Text blocksCleared;
    
    public GameStats(double width, double height) { 
        this.width = width;
        this.height = height;
        background = new Rectangle(width, height, Color.BLACK);
        this.getChildren().add(background);
        
        gameName.setFont(new Font(50));
        gameName.setTranslateX(width/2 - gameName.getBoundsInParent().getWidth()/2);
        gameName.setTranslateY(100);
        gameName.setFill(Color.WHITESMOKE);
        this.getChildren().add(gameName);
    }
    
    
    
    
}
