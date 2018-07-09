/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3dtetriswell;

import Well.Well;
import javafx.application.Application;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

/**
 *
 * @author AM
 */
public class Main extends Application {
    private static final double WIDTH = 1000;
    private static final double HEIGHT = 600;
    
    private Group root;
    private Well well;
    
    private Camera frontCamera = new PerspectiveCamera(true);
    
    @Override
    public void start(Stage window) {
        root = new Group();
        
        well = new Well(5,5,10);

        well.getTransforms().setAll(new Scale(3, 3, 4.5));
        
        root.getChildren().add(well);
        
        PointLight topLight = new PointLight(Color.WHITE);
        topLight.setTranslateZ(-30);
        PointLight bottomLight = new PointLight(Color.DARKGREY);
        bottomLight.setTranslateZ(300);
        
        
        root.getChildren().addAll(topLight, bottomLight);
        
        frontCamera.setFarClip(20000);       
        frontCamera.getTransforms().addAll(new Translate(0,0,-500));
        
        Scene scene = new Scene(root, WIDTH, HEIGHT, false, SceneAntialiasing.BALANCED);
        scene.setCamera(frontCamera);
        scene.setFill(Color.BROWN);
        
        window.setTitle("3D Tetris Well");
        window.setScene(scene);
        window.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
