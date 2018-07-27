/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3dtetriswell;

import Well.Updateable;
import Well.Well;
import Well.Well.WellView;
import gameStats.GameStats;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.util.Duration;
import menus.*;

/**
 *
 * @author AM
 */
public class Main extends Application implements Updateable{
    private static enum State {MAIN_MENU, PARAMETERS_MENU, GAMEPLAY}
    private State state;
    
    private static final double WIDTH = 1300;
    private static final double HEIGHT = 800;
    private static final double MAX_WELL_SIZE = 260;
    
    private static final double WELL_SCALE_FACTOR_Z = 1.5;
    
    private static final double ROTATION_SPEED = 0.2;
    private static final double TRANSLATION_SPEED = 0.2;
    private static final double ALT_FACTOR = 5.0;
    
    private static final double INITIAL_CAMERA_LIGHT_INTENSITY = 0.1;
    
    private static final double BIRDSEYE_CAMERA_POSITION_X = 0;
    private static final double BIRDSEYE_CAMERA_POSITION_Y = 0;
    private static final double BIRDSEYE_CAMERA_POSITION_Z = -500.0;
    private static final double BIRDSEYE_CAMERA_ROTATE = 0;
    
    private static final double SIDE_CAMERA_POSITION_X = 0;
    private static final double SIDE_CAMERA_POSITION_Y = 1000;
    private static final double SIDE_CAMERA_POSITION_Z = 0;
    private static final double SIDE_CAMERA_ROTATE = 90;
    
    private static final int CAMERA_ANIMATION_DURATION = 3000;
    
    private double mousePositionX, mousePositionY;
    private double oldMousePositionX, oldMousePositionY;
    private double mouseMovedX, mouseMovedY;
    
    private Stage window;
    private Scene gameScene;
    private SubScene gamePlayScene;
    private Group root;
    private Well well;
    private GameStats gameStats;
    
    MainMenu mainMenu;
    ParametersMenu parametersMenu;
    
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
        this.window = window;
        
        window.widthProperty().addListener(e -> adjustSize(window));
        window.heightProperty().addListener(e -> adjustSize(window));
        
        new AnimationTimer(){
            @Override
            public void handle(long now) {
                update();
            }  
        }.start();
        
        openMainMenu();
    }
    
    private void openMainMenu(){
        state = State.MAIN_MENU;
        mainMenu = new MainMenu();
        gameScene = new Scene(mainMenu, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
        mainMenu.adjustPositions(gameScene.getWidth(), gameScene.getHeight());
        
        window.setTitle("3D Tetris Well");
        window.setScene(gameScene);
        window.show();
        
        eventHandling();
    }
    
    private void openParametersMenu(){
        state = State.PARAMETERS_MENU;
        parametersMenu = new ParametersMenu();
        gameScene = new Scene(parametersMenu, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
        parametersMenu.adjustPositions(gameScene.getWidth(), gameScene.getHeight());
        
        window.setTitle("3D Tetris Well");
        window.setScene(gameScene);
        window.show();
        
        eventHandling();
    }
    
    private void startGamePlay(int level, int width, int height, int depth){
        state = State.GAMEPLAY;
        gameScene = null;
        gamePlayScene = null;
        root = null;
        well = null;
        gameStats = null;
        
        well = new Well(level, width, height, depth);
        
        double wellScaleFactor = MAX_WELL_SIZE/(well.FIELD_SIZE * 
                (Math.max(well.getHeight(), well.getWidth()) + 1));
        well.getTransforms().setAll(new Scale(wellScaleFactor, wellScaleFactor, 1.5*wellScaleFactor));
        
        root = new Group(well);
        
        camera = new PerspectiveCamera(true);
        cameraHolder = new Group(camera, cameraLight);
        
        cameraHolder.setRotationAxis(Rotate.X_AXIS);
        cameraHolderRotateY.setPivotZ(SIDE_CAMERA_POSITION_Y);
        cameraHolder.getTransforms().addAll(cameraHolderRotateZ, cameraHolderRotateY, cameraHolderRotateX);
        
        root.getChildren().add(cameraHolder);
        
        camera.setFarClip(2500);       
        setCamera(BIRDSEYE_CAMERA_POSITION_X, BIRDSEYE_CAMERA_POSITION_Y, BIRDSEYE_CAMERA_POSITION_Z, 
                BIRDSEYE_CAMERA_ROTATE, CameraView.BIRDSEYE_VIEW, WellView.REALISTIC);
        
        
        double smallerDimension = Math.min(well.getHeight(), well.getWidth());
        cameraLight.setTranslateZ(
                0.49 * (smallerDimension / (smallerDimension+1)) * 
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
        
        eventHandling();
    }
    
    private void adjustSize(Stage window) {
        switch (state) {
            case GAMEPLAY:
                windowScale.setY(gameScene.getHeight()/HEIGHT);
                windowScale.setX(gameScene.getWidth()/WIDTH);
                gameStats.setTranslateX(gamePlayScene.getBoundsInParent().getWidth());
                gameStats.getBackground().setWidth(window.getWidth() - gamePlayScene.getBoundsInParent().getWidth());
                gameStats.getBackground().setHeight(window.getHeight());

                double smallerDimension = Math.min(well.getHeight(), well.getWidth());

                // NOTE: this is a workaround. It should not work like this. The environment should scale the light position by default
                cameraLight.setTranslateZ(
                        Math.min(gameScene.getHeight()/HEIGHT, gameScene.getWidth()/WIDTH) * 0.49 * (smallerDimension / (smallerDimension+1)) * 
                        Math.min(well.getBoundsInParent().getWidth(), well.getBoundsInParent().getHeight()));
                break;
            case PARAMETERS_MENU:
                parametersMenu.adjustPositions(gameScene.getWidth(), gameScene.getHeight());
                break;
            case MAIN_MENU:
                mainMenu.adjustPositions(gameScene.getWidth(), gameScene.getHeight());
                break;
            default:
                throw new AssertionError();
        }       
    }
    
    private void setCamera(double endCameraTramslateX, double endCameraTramslateY, double endCameraTramslateZ, 
                            double endCameraRotate, CameraView endCameraView, WellView endWellView)
    {
        well.setPaused(true);
        cameraView = endCameraView;
        
        KeyValue endCameraHolderRotateX = new KeyValue(cameraHolderRotateX.angleProperty(), 0);
        KeyValue endCameraHolderRotateY = new KeyValue(cameraHolderRotateY.angleProperty(), 0);
        KeyValue endCameraHolderRotateZ = new KeyValue(cameraHolderRotateZ.angleProperty(), 0);
        KeyValue endCameraHolderRotate = new KeyValue(cameraHolder.rotateProperty(), endCameraRotate);
        KeyValue endCammeraHolderTranslateX = new KeyValue(cameraHolder.translateXProperty(), endCameraTramslateX);
        KeyValue endCammeraHolderTranslateY = new KeyValue(cameraHolder.translateYProperty(), endCameraTramslateY);
        KeyValue endCammeraHolderTranslateZ = new KeyValue(cameraHolder.translateZProperty(), endCameraTramslateZ);
        
        ParallelTransition parallelTransition = new ParallelTransition(
            new Timeline(new KeyFrame(Duration.millis(CAMERA_ANIMATION_DURATION), 
                    endCammeraHolderTranslateX, endCammeraHolderTranslateY, endCammeraHolderTranslateZ, 
                    endCameraHolderRotateX, endCameraHolderRotateY, endCameraHolderRotateZ, endCameraHolderRotate)));
        parallelTransition.play();
        parallelTransition.setOnFinished(e-> {
            well.setView(endWellView);
            well.setPaused(false);
        });
    }
    
    @Override
    public void update() {
        switch (state) {
            case MAIN_MENU:
                mainMenu.adjustPositions(gameScene.getWidth(), gameScene.getHeight());
                switch (mainMenu.getChoice()) {
                    case MainMenu.NEW_GAME:
                        openParametersMenu();
                        break;
                    case MainMenu.EXIT:
                        Platform.exit();
                        break;
                    default:
                        break;
                }
                break;
            case PARAMETERS_MENU: 
                parametersMenu.adjustPositions(gameScene.getWidth(), gameScene.getHeight());
                switch (parametersMenu.getChoice()) {
                    case ParametersMenu.PARAMETERS_ENTERED:
                        int width=0, height=0, depth=0, level=0;
                        try {
                            width = Integer.parseInt(parametersMenu.getWidthField().getText());
                            height = Integer.parseInt(parametersMenu.getHeightField().getText());
                            depth = Integer.parseInt(parametersMenu.getDepthField().getText());
                            level = Integer.parseInt(parametersMenu.getLevelField().getText());
                            
                            if ((width<3) || (width>8) || (height<3) || (height>8) ||
                               (depth<6) || (depth>20) || (level<0) || (level>15)){
                                throw new NumberFormatException();
                            }
                        }
                        catch (NumberFormatException e){
                            parametersMenu.setWarning(); 
                        }
                        
                        if (parametersMenu.getChoice() == ParametersMenu.PARAMETERS_ENTERED)
                            startGamePlay(level, width, height, depth);
                        
                        break;
                    case ParametersMenu.ENTER_PARAMETERS_AGAIN:
                        break;
                    case Menu.NO_CHOICE_MADE:
                        break;
                    default:
                        break;
                }
                break;
            case GAMEPLAY:
                root.getChildren().remove(well);
                well.update();
                root.getChildren().add(well);

                gameStats.update();
                break;
            default:
                throw new AssertionError();
        }
    }
    
    private void eventHandling() {
        switch (state) {
            case MAIN_MENU:
                break;
            case PARAMETERS_MENU:
                gameScene.setOnKeyPressed(keyEvent-> {
                    if (keyEvent.getCode() == KeyCode.ENTER){
                        parametersMenu.setChoice(ParametersMenu.PARAMETERS_ENTERED);
                    }
                });
                break;
            case GAMEPLAY:
                gameScene.setOnKeyPressed(well);
                gameScene.addEventHandler(KeyEvent.KEY_PRESSED, e-> onKeyPressed(e));

                gamePlayScene.setOnMousePressed(e -> onMousePressed(e));
                gamePlayScene.setOnMouseDragged(e -> onMouseDragged(e));
                gamePlayScene.setOnScroll(e -> onScroll(e));
                break;
            default: 
                throw new AssertionError();
        }
    }
    
    private void setLightIntensity(PointLight light, Color color, double intensity){
        light.setColor(Color.color(intensity * color.getRed(), intensity * color.getGreen(), intensity * color.getBlue()));
    }
    
    private void onKeyPressed(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();
        
        switch (keyCode) {
            case DIGIT0: case NUMPAD0:
                setCamera(BIRDSEYE_CAMERA_POSITION_X, BIRDSEYE_CAMERA_POSITION_Y, BIRDSEYE_CAMERA_POSITION_Z, 
                        BIRDSEYE_CAMERA_ROTATE, CameraView.BIRDSEYE_VIEW, WellView.REALISTIC);
                break;
            case DIGIT1: case NUMPAD1:
                cameraLightIntensity -= 0.1;
                if (cameraLightIntensity <= 0.01 ) cameraLightIntensity = 0.01;
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
                    setCamera(SIDE_CAMERA_POSITION_X, SIDE_CAMERA_POSITION_Y, SIDE_CAMERA_POSITION_Z, 
                            SIDE_CAMERA_ROTATE, CameraView.SIDE_VIEW, WellView.MESHVIEW);
                else 
                    setCamera(BIRDSEYE_CAMERA_POSITION_X, BIRDSEYE_CAMERA_POSITION_Y, BIRDSEYE_CAMERA_POSITION_Z, 
                            BIRDSEYE_CAMERA_ROTATE, CameraView.BIRDSEYE_VIEW, WellView.REALISTIC);
                break; 
            case ESCAPE:
                openMainMenu();
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
                cameraHolderRotateX.setAngle(cameraHolderRotateX.getAngle() - mouseMovedY*ROTATION_SPEED*speedModificator);
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
