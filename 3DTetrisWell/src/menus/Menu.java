package menus;

import static gameStats.GameStats.gameName;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;

/**
 *
 * @author AM
 */
abstract public class Menu extends Group{
    public static final int NO_CHOICE_MADE = -1;
    
    protected int choice = NO_CHOICE_MADE;
    public int getChoice() { return choice; }
    public void setChoice(int choice) { this.choice = choice; }
    
    protected Rectangle background;
    protected static Text gameName = new Text("BlockWell");
    public static Text getGameName() { return gameName; }
    
    protected VBox menuButtons;
    
    public Menu() {
        setBackground();
        setMenuButtonsContainer();
        setButtons();
    }
    
    private final void setBackground(){
        background = new Rectangle(
            Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight(), Color.BLACK);
        
        gameName.setFont(new Font(50));
        gameName.setTranslateX(background.getWidth()/2 - gameName.getBoundsInParent().getWidth()/2);
        gameName.setTranslateY(70);
        gameName.setFill(Color.TURQUOISE);
        this.getChildren().addAll(background, gameName);
    }
    
    private void setMenuButtonsContainer() {
        menuButtons = new VBox(20);
        menuButtons.setTranslateX(background.getWidth()/2);
        menuButtons.setTranslateY(background.getHeight()/2);
        menuButtons.setAlignment(Pos.CENTER);
        menuButtons.setTranslateY(150);
        this.getChildren().add(menuButtons);
    }
    
    public void adjustPositions(double width, double height){
        gameName.setTranslateX(width/2 - gameName.getBoundsInParent().getWidth()/2);
        
        menuButtons.setTranslateX(width/2 - menuButtons.getBoundsInParent().getWidth()/2);
    }
    
    abstract protected void setButtons();
}
