package Well.construction;

import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;

/**
 *
 * @author AM
 */
public class WellConstruction extends Group{
    int width, height, depth;
    double wallWidth;
    double fieldSize;
    
    public static enum WellView {REALISTIC_MESH, REALISTIC, GAMER, MESH}
    private WellView wellView;
    public WellView getView() { return wellView; }
    public void setView(WellView view) { 
        this.wellView = view; 
        refreshMaterials();
    }
    public void changeView(){ 
        switch (wellView) {
            case REALISTIC_MESH: setView(WellView.REALISTIC); break;
            case REALISTIC: setView(WellView.GAMER); break;
            case GAMER: setView(WellView.MESH); break;
            case MESH: setView(WellView.REALISTIC_MESH); break;
            default: throw new AssertionError();
        }
    }
    
    ConstructionMaterials constructionMaterials = new ConstructionMaterials();
    
    private Group walls;
    private Wall leftWall, rightWall;
    private Wall frontWall, rearWall;
    
    Bottom bottom;
    Edges edges;
    SteelFramework steelFramework;
    
    
    public WellConstruction(int width, int height, int depth, double wallWidth, double fieldSize) {
        this.width = width; 
        this.height = height;
        this.depth = depth;
        this.wallWidth = wallWidth;
        this.fieldSize = fieldSize;
        
        addWalls();
        addEdges();
        addBottom();
        addSteelFramework();
        setView(WellView.REALISTIC_MESH);
    }
    
    private void addWalls() {
        walls = new Group();
        
        leftWall = new Wall(height, depth, wallWidth, fieldSize);
        leftWall.setRotate(90.0);
        leftWall.rotateBlocks(180.0);
        leftWall.setMaterial(constructionMaterials.realisticWallMaterial);
        leftWall.setTranslateX(-0.5*width*fieldSize - 0.5*wallWidth);
        
        frontWall = new Wall(width, depth, wallWidth, fieldSize);
        frontWall.setMaterial(constructionMaterials.realisticWallMaterial);
        frontWall.setTranslateY(-0.5*height*fieldSize - 0.5*wallWidth);
        
        rightWall = new Wall(height, depth, wallWidth, fieldSize);
        rightWall.setRotate(90.0);
        rightWall.setTranslateX(0.5*width*fieldSize + 0.5*wallWidth);
        rightWall.setMaterial(constructionMaterials.realisticWallMaterial);
        
        rearWall = new Wall(width, depth, wallWidth, fieldSize);
        rearWall.rotateBlocks(180.0);
        rearWall.setTranslateY(0.5*height*fieldSize + 0.5*wallWidth);
        rearWall.setMaterial(constructionMaterials.realisticWallMaterial);
        
        walls.getChildren().addAll(leftWall, frontWall, rightWall, rearWall);
        this.getChildren().add(walls);
    }
    
    private void addEdges(){
        edges = new Edges(width, height, wallWidth, fieldSize);
        edges.setMaterial(constructionMaterials.realisticEdgeMaterial);
        
        this.getChildren().add(edges);
    }

    private void addBottom() {
        bottom = new Bottom(width, height, depth, wallWidth, fieldSize);
        bottom.setMaterial(constructionMaterials.realisticEdgeMaterial);
        
        this.getChildren().add(bottom);
    }

    private void addSteelFramework() {
        steelFramework = new SteelFramework(width, height, depth, fieldSize);
        
        this.getChildren().add(steelFramework);
    }
    
    public void refreshMaterials(){
        walls.setVisible((wellView == WellView.MESH) ? false : true);
        edges.setVisible((wellView == WellView.MESH) ? false : true);
        bottom.setVisible((wellView == WellView.MESH) ? false : true);
        steelFramework.setVisible((wellView == WellView.MESH || wellView == WellView.REALISTIC_MESH) ? true : false);
        
        switch (wellView) {
            case MESH:
                break;
            case GAMER:
                walls.getChildren().forEach(node -> ((Wall)node).setMaterial(constructionMaterials.gamerWallMaterial));
                edges.setMaterial(constructionMaterials.gamerEdgeMaterial);
                bottom.setMaterial(constructionMaterials.gamerBottomMaterial);
                break;
            case REALISTIC: case REALISTIC_MESH:
                walls.getChildren().forEach(node -> ((Wall)node).setMaterial(constructionMaterials.realisticWallMaterial));
                edges.setMaterial(constructionMaterials.realisticEdgeMaterial);
                bottom.setMaterial(constructionMaterials.realisticBottomMaterial);
                break;
            default:
                throw new AssertionError();
        }
    }
    
    public void resetWalls(){
        if (wellView == WellView.MESH) return;
        
        switch (wellView) {
            case GAMER:
                walls.getChildren().forEach(node -> ((Wall)node).setMaterial(constructionMaterials.gamerWallMaterial));
                break;
            case REALISTIC: case REALISTIC_MESH:
                walls.getChildren().forEach(node -> ((Wall)node).setMaterial(constructionMaterials.realisticWallMaterial));
                break;
            default:
                throw new AssertionError();
        }
    }
    
    public void setWallIllumination(int x, int y, int z){
        PhongMaterial material = (wellView == WellView.REALISTIC || wellView == WellView.REALISTIC_MESH) ? 
                constructionMaterials.realisticShiningWallMaterial : ConstructionMaterials.gamerShiningWallMaterial;
        
        rightWall.setBlockMaterial(material, y, z);
        leftWall.setBlockMaterial(material, y, z);
        frontWall.setBlockMaterial(material, x, z);
        rearWall.setBlockMaterial(material, x, z);
    }
}
