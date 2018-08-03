package sidebars;

import Well.Updateable;
import Well.Well;
import Well.construction.ConstructionMaterials;
import javafx.scene.Group;
import javafx.scene.paint.Color;
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
        
        addOccupiedFloors();
        addOccupiedFloorsBracket();
    }
    
    private void addOccupiedFloorsBracket() {
        occupiedFloorsBracket = new Group();
        
        double bgHeight = background.getHeight();
        
        occupiedFloorsBracket.getChildren().add(makeLine(
                new Line(0, bgHeight - 20, 40, bgHeight - 20), Color.AQUA));
        
        for (int i = 0; i < well.getDepth(); i++) {
            occupiedFloorsBracket.getChildren().add(makeLine(new Line(
                    0, bgHeight - 20 - 30*(i+1), 40, bgHeight - 20 - 30*(i+1)), Color.GREY));
            occupiedFloorsBracket.getChildren().add(makeLine(
                    new Line(0, bgHeight - 20 - 30*i, 0, bgHeight - 20 - 30*(i+1)), Color.AQUA));
            occupiedFloorsBracket.getChildren().add(makeLine(new Line(
                    40, bgHeight - 20 - 30*i, 40, bgHeight - 20 - 30*(i+1)), Color.AQUA));
        }
        
        occupiedFloorsBracket.setTranslateX(width/2);
        this.getChildren().add(occupiedFloorsBracket);
    }
    
    private void addOccupiedFloors() {
        occupiedFloors = new Rectangle[well.getDepth()];
        double bgHeight = background.getHeight();
        
        for (int i = 0; i < well.getDepth(); i++){
            occupiedFloors[i] = 
                    new Rectangle(width/2, bgHeight - 20 - 30*(i+1), 40, 30);
            occupiedFloors[i].setFill(ConstructionMaterials.fallenBlocksColors2D[
                    i % ConstructionMaterials.fallenBlocksColors2D.length]);
            
            occupiedFloors[i].setVisible(false);
            this.getChildren().add(occupiedFloors[i]);
        }
    }
    
    private void setHighestOccupiedFloor(){
        int hof = well.getHighestOccupiedFloor();
        
        for (int i = 0; i <= well.getHighestOccupiedFloor(); i++)
            occupiedFloors[i].setVisible(true);
        
        for (int i = well.getHighestOccupiedFloor() + 1; i < well.getDepth(); i++)
            occupiedFloors[i].setVisible(false);
    }
    
    public void setSavedTetrimino(Group newSavedTetrimino) {
        this.getChildren().remove(savedTetrimino);
        
        savedTetrimino = newSavedTetrimino;
        savedTetrimino.setScaleX(3);savedTetrimino.setScaleY(3);
        savedTetrimino.setTranslateX(width/2);
        savedTetrimino.setTranslateY(110);
        
        this.getChildren().add(savedTetrimino);
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
        
        setHighestOccupiedFloor();
    }
}
