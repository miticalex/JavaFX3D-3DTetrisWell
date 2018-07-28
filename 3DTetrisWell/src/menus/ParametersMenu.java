package menus;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author AM
 */
public class ParametersMenu extends Menu{
    public static final int ENTER_PARAMETERS_AGAIN = 1;
    public static final int PARAMETERS_ENTERED = 10;
    
    private TextField widthField;
    public TextField getWidthField() { return widthField; }
    private TextField heightField;
    public TextField getHeightField() { return heightField; }
    private TextField depthField;
    public TextField getDepthField() { return depthField; }
    private TextField levelField;
    public TextField getLevelField() { return levelField; }
    
    private Text warning;
    public void setWarning() { 
        warning.setVisible(true); 
        choice = ENTER_PARAMETERS_AGAIN;
    }

    @Override
    protected void setButtons() {
        Text widthText =  new Text ("Field Width: ");
        widthText.setFont(new Font(20));
        widthText.setWrappingWidth(100);
        widthText.setFill(Color.YELLOW);
        widthField = new TextField("5");
        widthField.setFont(new Font(20));
        widthField.setMaxWidth(100);
        menuButtons.getChildren().add(new HBox(20, widthText, widthField));
        
        Text heightText =  new Text ("Field Height: ");
        heightText.setFont(new Font(20));
        heightText.setWrappingWidth(100);
        heightText.setFill(Color.YELLOW);
        heightField = new TextField("5");
        heightField.setFont(new Font(20));
        heightField.setMaxWidth(100);
        menuButtons.getChildren().add(new HBox(20, heightText, heightField));
        
        Text depthText =  new Text ("Field Depth: ");
        depthText.setFont(new Font(20));
        depthText.setWrappingWidth(100);
        depthText.setFill(Color.YELLOW);
        depthField = new TextField("12");
        depthField.setFont(new Font(20));
        depthField.setMaxWidth(100);
        menuButtons.getChildren().add(new HBox(20, depthText, depthField));
        
        Text levelText =  new Text ("Level: ");
        levelText.setFont(new Font(20));
        levelText.setWrappingWidth(100);
        levelText.setFill(Color.YELLOW);
        levelField = new TextField("0");
        levelField.setFont(new Font(20));
        levelField.setMaxWidth(100);
        menuButtons.getChildren().add(new HBox(20, levelText, levelField));
        
        Button enterButton = new Button("Start New Game");
        enterButton.setFont(new Font(20));
        enterButton.setMinWidth(100);
        enterButton.setOnMouseClicked(e -> choice = PARAMETERS_ENTERED);
        menuButtons.getChildren().add(enterButton);
        
        warning = new Text("Ranges must be:\n"
                            + "3<= height, width <=8,\n"
                            + "6<= depth <=20\n"
                            + "0<= level <=20");
        warning.setFont(new Font(20));
        warning.setWrappingWidth(250);
        warning.setTextAlignment(TextAlignment.CENTER);
        warning.setFill(Color.RED);
        menuButtons.getChildren().add(warning);
    }
    
    
}
