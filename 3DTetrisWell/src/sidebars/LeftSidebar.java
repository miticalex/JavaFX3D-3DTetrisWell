package sidebars;

import Well.Updateable;
import Well.Well;
import Well.construction.ConstructionMaterials;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
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
    
    private Group occupiedFloorsBracket;
    Rectangle[] occupiedFloors;
    private Group savedTetrimino = new Group();
    private Circle fallingTetriminoFloorIndicator;
    
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
        
        occupiedFloorsBracket = new Group();
        addOccupiedFloors();
        addOccupiedFloorsBracket();
    }
    
    private void addOccupiedFloorsBracket() {
        occupiedFloorsBracket.setTranslateX(width/2);
        occupiedFloorsBracket.setTranslateY(background.getHeight()-20);
        this.getChildren().add(occupiedFloorsBracket);
        
        double bgHeight = background.getHeight();
        
        occupiedFloorsBracket.getChildren().add(makeLine(
                new Line(0, 0, 40, 0), Color.AQUA));
        
        for (int i = 0; i < well.getDepth(); i++) {
            occupiedFloorsBracket.getChildren().add(makeLine(new Line(
                    0, -30*(i+1), 40, -30*(i+1)), Color.GREY));
            occupiedFloorsBracket.getChildren().add(makeLine(
                    new Line(0, -30*i, 0, -30*(i+1)), Color.AQUA));
            occupiedFloorsBracket.getChildren().add(makeLine(new Line(
                    40, -30*i, 40, -30*(i+1)), Color.AQUA));
        }
        
        
    }
    
    private void addOccupiedFloors() {
        occupiedFloors = new Rectangle[well.getDepth()];
        double bgHeight = background.getHeight();
        
        for (int i = 0; i < well.getDepth(); i++){
            occupiedFloors[i] = 
                    new Rectangle(0, -30*(i+1), 40, 30);
            occupiedFloors[i].setFill(ConstructionMaterials.fallenBlocksColors2D[
                    i % ConstructionMaterials.fallenBlocksColors2D.length]);
            
            occupiedFloors[i].setVisible(false);
            occupiedFloorsBracket.getChildren().add(occupiedFloors[i]);
        }
    }
    
    private void setHighestOccupiedFloor(){
        for (int i = 0; i <= well.getHighestOccupiedFloor(); i++)
            occupiedFloors[i].setVisible(true);
        
        for (int i = well.getHighestOccupiedFloor() + 1; i < well.getDepth(); i++)
            occupiedFloors[i].setVisible(false);
    }
    
    public void setSavedTetrimino(Group newSavedTetrimino) {
        this.getChildren().remove(savedTetrimino);
        if (newSavedTetrimino == null) return;
        
        savedTetrimino = newSavedTetrimino;
        savedTetrimino.setScaleX(3);savedTetrimino.setScaleY(3);
        savedTetrimino.setTranslateX(width/2);
        savedTetrimino.setTranslateY(110);
        
        this.getChildren().add(savedTetrimino);
    }
    
    void setFallingTetriminoFloorIndicator(){
        if (fallingTetriminoFloorIndicator != null)
            occupiedFloorsBracket.getChildren().remove(fallingTetriminoFloorIndicator);
        
        double bgHeight = background.getHeight();
        
        if (well.getFallingTetriminoFloor() < well.getDepth()) {
            fallingTetriminoFloorIndicator = new Circle(
                20, -15 - 30*(well.getFallingTetriminoFloor()), 5, Color.BROWN);
            fallingTetriminoFloorIndicator.setStroke(Color.WHITE);
            fallingTetriminoFloorIndicator.setStrokeWidth(2);
            occupiedFloorsBracket.getChildren().add(fallingTetriminoFloorIndicator);
        }
    }
    
    private Line makeLine(Line line, Color color){
        if (line == null) return null;
        
        line.setStroke(color);
        line.setStrokeWidth(2);
        
        return line;
    }

    @Override
    public void update() {
        if (well.getSavedTetrimino() != null)
            setSavedTetrimino(well.getSavedTetrimino().get2DAppearance());
        else setSavedTetrimino(null);
        
        setHighestOccupiedFloor();
        setFallingTetriminoFloorIndicator();
    }
}
