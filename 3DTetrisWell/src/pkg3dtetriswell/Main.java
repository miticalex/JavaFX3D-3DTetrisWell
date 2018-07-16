/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3dtetriswell;

import Well.Updateable;
import Well.Well;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

/**
 *
 * @author AM
 */
public class Main extends Application implements Updateable{
    private static final double WIDTH = 1000;
    private static final double HEIGHT = 600;
    private static final double MAX_WELL_SIZE = 260;
    
    private Scene gameScene;
    private Group root;
    private Well well;
    
    private Camera frontCamera = new PerspectiveCamera(true);
    
    @Override
    public void start(Stage window) {
        root = new Group();
        
        well = new Well(5,5,10);
        
        double scaleFactor = MAX_WELL_SIZE/(well.FIELD_SIZE * 
                ((well.getHeight()>well.getWidth()? well.getHeight() : well.getWidth())+1));

        well.getTransforms().setAll(new Scale(scaleFactor, scaleFactor, 1.5*scaleFactor));
        
        root.getChildren().add(well);
        
        AmbientLight ambientLight = new AmbientLight(Color.color(0.1, 0.1, 0.1));
        PointLight topLight = new PointLight(Color.WHITE);
        topLight.setTranslateZ(-40);
        PointLight bottomLight = new PointLight(Color.DARKGREY);
        bottomLight.setTranslateZ(500);
        
        
        root.getChildren().addAll(topLight, bottomLight);
        
        frontCamera.setFarClip(20000);       
        frontCamera.getTransforms().addAll(new Translate(0,0,-500));
        
        gameScene = new Scene(root, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
        gameScene.setCamera(frontCamera);
        gameScene.setFill(Color.color(0.2, 0.1, 0));
        
        window.setTitle("3D Tetris Well");
        window.setScene(gameScene);
        window.show();
        
        new AnimationTimer(){
            @Override
            public void handle(long now) {
                update();
            }  
        }.start();
        
        eventHandling();
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
