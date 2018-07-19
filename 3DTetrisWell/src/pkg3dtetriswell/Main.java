/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3dtetriswell;

import Well.Updateable;
import Well.Well;
import gameStats.GameStats;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

/**
 *
 * @author AM
 */
public class Main extends Application implements Updateable{
    private static final double WIDTH = 1250;
    private static final double HEIGHT = 750;
    private static final double MAX_WELL_SIZE = 260;
    
    private Scene gameScene;
    private SubScene gamePlayScene;
    private Group root;
    private Well well;
    private GameStats gameStats;
    
    private Camera frontCamera = new PerspectiveCamera(true);
    
    private Scale windowScale = new Scale();
    
    @Override
    public void start(Stage window) {
        well = new Well(1, 5,5,12);
        
        double wellScaleFactor = MAX_WELL_SIZE/(well.FIELD_SIZE * 
                ((well.getHeight()>well.getWidth()? well.getHeight() : well.getWidth())+1));
        well.getTransforms().setAll(new Scale(wellScaleFactor, wellScaleFactor, 1.5*wellScaleFactor));
        
        root = new Group(well);
        
        frontCamera.setFarClip(2500);       
        frontCamera.getTransforms().addAll(new Translate(0,0,-500));
        
        gamePlayScene = new SubScene(root, HEIGHT, HEIGHT, true, SceneAntialiasing.BALANCED);
        gamePlayScene.setCamera(frontCamera);
        gamePlayScene.setFill(Color.color(0.2, 0.1, 0));
        
        Pane gamePlayPane = new Pane(gamePlayScene);
        
        gameStats = new GameStats(WIDTH-HEIGHT, HEIGHT);
        
        gamePlayPane.getChildren().add(gameStats);
        gameStats.setTranslateX(HEIGHT);
        
        gameScene = new Scene(gamePlayPane, WIDTH, HEIGHT);
        
        gamePlayScene.getTransforms().add(windowScale);
        
        window.setTitle("3D Tetris Well");
        window.setScene(gameScene);
        window.show();
        gameScene.widthProperty().addListener(e -> adjustSize(window));
        gameScene.heightProperty().addListener(e -> adjustSize(window));
        
        new AnimationTimer(){
            @Override
            public void handle(long now) {
                update();
            }  
        }.start();
        
        eventHandling();
    }
    
    private void adjustSize(Stage window) {
        windowScale.setY(gameScene.getHeight()/HEIGHT);
        windowScale.setX(gameScene.getWidth()/WIDTH);
        gameStats.setTranslateX(gamePlayScene.getBoundsInParent().getWidth());
        gameStats.getBackground().setWidth(window.getWidth() - gamePlayScene.getBoundsInParent().getWidth());
        gameStats.getBackground().setHeight(window.getHeight());
    }

    @Override
    public void update() {
        root.getChildren().remove(well);
        
        well.update();
        
        root.getChildren().add(well);
    }
    
    private void eventHandling() {
        gameScene.setOnKeyPressed(well);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
