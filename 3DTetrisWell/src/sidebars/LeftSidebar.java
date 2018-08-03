package sidebars;

import Well.Updateable;
import Well.Well;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import static sidebars.GameStatsSidebar.gameName;

/**
 *
 * @author AM
 */
public class LeftSidebar extends Group implements Updateable{
    private double width;
    private double height;
    private Rectangle background;

    public Rectangle getBackground() {
        return background;
    }
    
    
    private Well well;
    
    Rectangle[] occupiedFloors;
    private Group savedTetrimino = new Group();
    
    public LeftSidebar(Well well, double width, double height) {
        this.well = well; 
        this.width = width;
        this.height = height;
        setBackGround();
        setSavedTetrimino(savedTetrimino);
    }
    
    private void setBackGround(){
        
        background = new Rectangle(width, height, Color.BLACK);
        
        Text savedTetriminoLabel = new Text("Saved shape:");
        savedTetriminoLabel.setFont(Font.font(20));
        savedTetriminoLabel.setFill(Color.YELLOW);
        savedTetriminoLabel.setStroke(Color.YELLOW);
        savedTetriminoLabel.setTranslateX(width/2 - savedTetriminoLabel.getBoundsInParent().getWidth()/2);
        savedTetriminoLabel.setTranslateY(50);
        
        this.getChildren().addAll(background, savedTetriminoLabel);
    }
    
    public void setSavedTetrimino(Group newSavedTetrimino) {
        this.getChildren().remove(savedTetrimino);
        
        savedTetrimino = newSavedTetrimino;
        savedTetrimino.setScaleX(3);savedTetrimino.setScaleY(3);
        savedTetrimino.setTranslateX(width/2);
        savedTetrimino.setTranslateY(120);
        
        this.getChildren().add(savedTetrimino);
    }

    @Override
    public void update() {
        if (well.getSavedTetrimino() != null)
            setSavedTetrimino(well.getSavedTetrimino().get2DAppearance());
    }
}
