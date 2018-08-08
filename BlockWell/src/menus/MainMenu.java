package menus;

import javafx.scene.control.Button;
import javafx.scene.text.Font;

/**
 *
 * @author AM
 */
public class MainMenu extends Menu {
    public static final int NEW_GAME = 0;
    public static final int EXIT = 10;

    public MainMenu() { }
    
    @Override
    protected void setButtons() {
        Button newGame = new Button("New Game");
        newGame.setFont(new Font(20));
        newGame.setMinWidth(150);
        newGame.setOnMouseClicked(e-> choice = NEW_GAME);
        
        Button exit = new Button("Exit");
        exit.setFont(new Font(20));
        exit.setMinWidth(150);
        exit.setOnMouseClicked(e-> choice = EXIT);
        
        menuButtons.getChildren().addAll(newGame, exit);
    }
    
    public void adjustPositions(double width, double height){
        gameName.setTranslateX(width/2 - gameName.getBoundsInParent().getWidth()/2);
        
        menuButtons.setTranslateX(width/2 - 75/*menuButtons.getBoundsInParent().getWidth()/2*/);
    }
}
