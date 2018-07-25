/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3dtetriswell;

import Well.Updateable;
import Well.Well;
import Well.Well.View;
import gameStats.GameStats;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

/**
 *
 * @author AM
 */
public class Main extends Application implements Updateable{
    private static final double WIDTH = 1250;
    private static final double HEIGHT = 800;
    private static final double MAX_WELL_SIZE = 260;
    
    private static final double ROTATION_SPEED = 0.2;
    private static final double TRANSLATION_SPEED = 0.2;
    private static final double ALT_FACTOR = 5.0;
    
    private static final double INITIAL_CAMERA_LIGHT_INTENSITY = 0;
    
    private static final double INITIAL_CAMERA_POSITION_Y = 0;
    private static final double INITIAL_CAMERA_POSITION_Z = -500.0;
    
    private static final double SIDE_CAMERA_POSITION_Y = 1000;
    private static final double SIDE_CAMERA_POSITION_Z = 0;
    
    private double mousePositionX, mousePositionY;
    private double oldMousePositionX, oldMousePositionY;
    private double mouseMovedX, mouseMovedY, stepZ;
    
    private Scene gameScene;
    private SubScene gamePlayScene;
    private Group root;
    private Well well;
    private GameStats gameStats;
    
    private static enum CameraView {BIRDSEYE_VIEW, SIDE_VIEW};
    private CameraView cameraView;
    
    private Group cameraHolder;
    private Camera camera = new PerspectiveCamera(true);
    private double cameraLightIntensity = INITIAL_CAMERA_LIGHT_INTENSITY;
    private Color cameraLightColor = Color.BLUE;
    private PointLight cameraLight = new PointLight();
    private Rotate cameraHolderRotateX = new Rotate(0, Rotate.X_AXIS);
    private Rotate cameraHolderRotateY = new Rotate(0, Rotate.Y_AXIS);
    private Rotate cameraHolderRotateZ = new Rotate(0, Rotate.Z_AXIS);
    private Translate frontHolderTranslate;
    
    private Scale windowScale = new Scale();
    
    @Override
    public void start(Stage window) {
        well = new Well(0, 5,5,12);
        
        double wellScaleFactor = MAX_WELL_SIZE/(well.FIELD_SIZE * 
                ((well.getHeight()>well.getWidth()? well.getHeight() : well.getWidth())+1));
        well.getTransforms().setAll(new Scale(wellScaleFactor, wellScaleFactor, 1.5*wellScaleFactor));
        
        root = new Group(well);
        
        cameraHolder = new Group(camera, cameraLight);
        
        cameraHolder.setRotationAxis(Rotate.X_AXIS);
        cameraHolderRotateY.setPivotZ(SIDE_CAMERA_POSITION_Y);
        cameraHolder.getTransforms().addAll(cameraHolderRotateZ, cameraHolderRotateY, cameraHolderRotateX);
        
        root.getChildren().add(cameraHolder);
        
        camera.setFarClip(2500);       
        setBirdsEyeCamera();
        
        cameraLight.setTranslateZ(0.4 * 
                Math.min(well.getBoundsInParent().getWidth(), well.getBoundsInParent().getHeight()));
        setLightIntensity(cameraLight, cameraLightColor, cameraLightIntensity);
        
        
        gamePlayScene = new SubScene(root, HEIGHT, HEIGHT, true, SceneAntialiasing.BALANCED);
        gamePlayScene.setCamera(camera);
        gamePlayScene.setFill(Color.BLACK);//Color.color(0.2, 0.1, 0));
        
        Pane gamePlayPane = new Pane(gamePlayScene);
        
        gameStats = new GameStats(well, WIDTH-HEIGHT, HEIGHT);
        
        gamePlayPane.getChildren().add(gameStats);
        gameStats.setTranslateX(HEIGHT);
        
        gameScene = new Scene(gamePlayPane, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
        
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
    
    private void setBirdsEyeCamera(){
        cameraView = CameraView.BIRDSEYE_VIEW;
        well.setView(View.REALISTIC);
        
        cameraHolder.setTranslateY(INITIAL_CAMERA_POSITION_Y);
        cameraHolder.setTranslateZ(INITIAL_CAMERA_POSITION_Z);
        cameraHolder.setRotate(0);
        cameraHolderRotateX.setAngle(0);
        cameraHolderRotateY.setAngle(0);
        cameraHolderRotateZ.setAngle(0);
    }
    
    private void setSideCamera(){
        well.setView(View.MESHVIEW);
        cameraView = CameraView.SIDE_VIEW;
        
        cameraHolder.setTranslateY(SIDE_CAMERA_POSITION_Y);
        cameraHolder.setTranslateZ(SIDE_CAMERA_POSITION_Z);
        cameraHolder.setRotate(90.0);
        cameraHolderRotateX.setAngle(0);
        cameraHolderRotateY.setAngle(0);
        cameraHolderRotateZ.setAngle(0);
    }

    @Override
    public void update() {
        root.getChildren().remove(well);
        
        well.update();
        
        root.getChildren().add(well);
        
        gameStats.update();
    }
    
    private void eventHandling() {
        gameScene.setOnKeyPressed(well);
        gameScene.addEventHandler(KeyEvent.KEY_PRESSED, e-> onKeyPressed(e));
        
        gamePlayScene.setOnMousePressed(e -> onMousePressed(e));
        gamePlayScene.setOnMouseDragged(e -> onMouseDragged(e));
        gamePlayScene.setOnScroll(e -> onScroll(e));
    }
    
    private void setLightIntensity(PointLight light, Color color, double intensity){
        light.setColor(Color.color(intensity * color.getRed(), intensity * color.getGreen(), intensity * color.getBlue()));
    }
    
    private void onKeyPressed(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();
        
        switch (keyCode) {
            case DIGIT0: case NUMPAD0:
                setBirdsEyeCamera();
                break;
            case DIGIT1: case NUMPAD1:
                cameraLightIntensity -= 0.1;
                if (cameraLightIntensity < 0 ) cameraLightIntensity = 0;
                setLightIntensity(cameraLight, cameraLightColor, cameraLightIntensity);
                break;
            case DIGIT2: case NUMPAD2:
                cameraLightIntensity += 0.1;
                if (cameraLightIntensity > 1 ) cameraLightIntensity = 1;
                setLightIntensity(cameraLight, cameraLightColor, cameraLightIntensity);
                break;
            case DIGIT3: case NUMPAD3:
                well.changeView();
                break;
            case TAB:
                if (cameraView == CameraView.BIRDSEYE_VIEW) 
                    setSideCamera();
                else 
                    setBirdsEyeCamera();
                break; 
            default: break;
        }
    }
    
    private void onMousePressed(MouseEvent mouseEvent) {
        oldMousePositionX = mousePositionX = mouseEvent.getSceneX();
        oldMousePositionY = mousePositionY = mouseEvent.getSceneY();
    }
    
    private void onMouseDragged(MouseEvent mouseEvent) {
        oldMousePositionX = mousePositionX;
        oldMousePositionY = mousePositionY;
        mousePositionX = mouseEvent.getSceneX();
        mousePositionY = mouseEvent.getSceneY();
        
        mouseMovedX = (mousePositionX - oldMousePositionX);
        mouseMovedY = (mousePositionY - oldMousePositionY);
        
        double speedModificator = 1.0;
        if (mouseEvent.isAltDown())
            speedModificator *= ALT_FACTOR;
        
        if (mouseEvent.isPrimaryButtonDown()) {
            if (cameraView == CameraView.BIRDSEYE_VIEW){
                cameraHolderRotateZ.setAngle(cameraHolderRotateZ.getAngle() - mouseMovedX*ROTATION_SPEED*speedModificator);
                cameraHolderRotateX.setAngle(cameraHolderRotateX.getAngle() + mouseMovedY*ROTATION_SPEED*speedModificator);
            }
            else { // SIDE_VIEW
                cameraHolderRotateY.setAngle(cameraHolderRotateY.getAngle() - mouseMovedX*ROTATION_SPEED*speedModificator);
                cameraHolderRotateZ.setAngle(cameraHolderRotateZ.getAngle() - mouseMovedY*ROTATION_SPEED*speedModificator);
            }
        }
    }
    
    private void onScroll(ScrollEvent scrollEvent) {
        double speedModificator = 1.0;
        if (scrollEvent.isAltDown())
            speedModificator *= ALT_FACTOR;
        
        cameraHolder.setTranslateZ(cameraHolder.getTranslateZ() + scrollEvent.getDeltaY()*TRANSLATION_SPEED*speedModificator);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
