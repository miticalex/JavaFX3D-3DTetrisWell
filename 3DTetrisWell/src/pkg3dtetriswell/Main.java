/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3dtetriswell;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author AM
 */
public class Main extends Application {
    private static final double WIDTH = 800;
    private static final double HEIGHT = 600;
    
    private Group root;
    
    @Override
    public void start(Stage primaryStage) {
        root = new Group();
        
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
