package Well;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/**
 *
 * @author AM
 */
public class Well extends Group{
    public static final double  FIELD_DIMENSION = 10.0;
    public static final double  BOX_DIMENSION = FIELD_DIMENSION-0.2;
    public static final double  WALL_WIDTH = 0.4;
    
    private final int width, height, depth;
    public int getWidth() { return width;}
    public int getHeight() { return height;}
    public int getDepth() { return depth;}
    
    //private Box[][][] stones;
    private Box[][] leftWall, rightWall;
    private Box[][] frontWall, rearWall;
    private Box[][] bottom;
    
    private Group edge = new Group();
    

    public Well(int x, int y, int z) {
        width = x>8 ? 8 : (x<3 ? 3 : x);
        height = y>8 ? 8 : (y<3 ? 3 : y);
        depth = z>40 ? 40 : (z<6 ? 6 : z);;
        //stones = new Box[x][y][z];
        
        PhongMaterial wallMaterial = new PhongMaterial(Color.color(0, 0.1, 1, 0.5));
        
        leftWall = new Box[height][depth];
        rightWall = new Box[height][depth];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < depth; j++) {
                leftWall[i][j] = new Box(WALL_WIDTH, BOX_DIMENSION, BOX_DIMENSION);
                rightWall[i][j] = new Box(WALL_WIDTH, BOX_DIMENSION, BOX_DIMENSION);
                
                leftWall[i][j].setMaterial(wallMaterial);
                rightWall[i][j].setMaterial(wallMaterial);
                
                leftWall[i][j].setTranslateX(-(FIELD_DIMENSION*width/2 + WALL_WIDTH/2));
                leftWall[i][j].setTranslateY(-FIELD_DIMENSION*height/2 + (i+0.5)*FIELD_DIMENSION);
                leftWall[i][j].setTranslateZ(FIELD_DIMENSION/2 + j*FIELD_DIMENSION);
                
                rightWall[i][j].setTranslateX(FIELD_DIMENSION*width/2 + WALL_WIDTH/2);
                rightWall[i][j].setTranslateY(-FIELD_DIMENSION*height/2 + (i+0.5)*FIELD_DIMENSION);
                rightWall[i][j].setTranslateZ(FIELD_DIMENSION/2 + j*FIELD_DIMENSION);
                
                this.getChildren().addAll(leftWall[i][j], rightWall[i][j]);
            }
        }
        
        frontWall = new Box[width][depth];
        rearWall = new Box[width][depth];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < depth; j++) {
                frontWall[i][j] = new Box(BOX_DIMENSION, WALL_WIDTH, BOX_DIMENSION);
                rearWall[i][j] = new Box(BOX_DIMENSION, WALL_WIDTH, BOX_DIMENSION);
                
                frontWall[i][j].setMaterial(wallMaterial);
                rearWall[i][j].setMaterial(wallMaterial);
                
                frontWall[i][j].setTranslateX(-FIELD_DIMENSION*width/2 + (i+0.5)*FIELD_DIMENSION);
                frontWall[i][j].setTranslateY(FIELD_DIMENSION*height/2 + WALL_WIDTH/2);
                frontWall[i][j].setTranslateZ(FIELD_DIMENSION/2 + j*FIELD_DIMENSION);
                
                rearWall[i][j].setTranslateX(-FIELD_DIMENSION*width/2 + (i+0.5)*FIELD_DIMENSION);
                rearWall[i][j].setTranslateY(-(FIELD_DIMENSION*height/2 + WALL_WIDTH/2));
                rearWall[i][j].setTranslateZ(FIELD_DIMENSION/2 + j*FIELD_DIMENSION);
                
                this.getChildren().addAll(rearWall[i][j], frontWall[i][j]);
            }   
        }
        
        bottom = new Box[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                bottom[i][j] = new Box(BOX_DIMENSION, BOX_DIMENSION, WALL_WIDTH);
                bottom[i][j].setMaterial(wallMaterial);
                
                bottom[i][j].setTranslateX(-FIELD_DIMENSION*width/2 + (i+0.5)*FIELD_DIMENSION);
                bottom[i][j].setTranslateY(-FIELD_DIMENSION*height/2 + (j+0.5)*FIELD_DIMENSION);
                bottom[i][j].setTranslateZ(FIELD_DIMENSION*depth + WALL_WIDTH/2);
                
                this.getChildren().addAll(bottom[i][j]);
            }   
        }
        
        for (int i = 1; i <= width; i++) {
            Box rearEdge = new Box(BOX_DIMENSION, BOX_DIMENSION/2, WALL_WIDTH);
            rearEdge.setMaterial(wallMaterial);
            rearEdge.setTranslateX(-FIELD_DIMENSION*(width+2)/2 + (i+0.5)*FIELD_DIMENSION);
            rearEdge.setTranslateY(-FIELD_DIMENSION*(height+0.5)/2);
            
            Box frontEdge = new Box(BOX_DIMENSION, BOX_DIMENSION/2, WALL_WIDTH);
            frontEdge.setMaterial(wallMaterial);
            frontEdge.setTranslateX(-FIELD_DIMENSION*(width+2)/2 + (i+0.5)*FIELD_DIMENSION);
            frontEdge.setTranslateY(FIELD_DIMENSION*(height+0.5)/2);
            
            edge.getChildren().addAll(rearEdge, frontEdge);      
        }
        
        for (int i = 1; i <= height; i++) {
            Box leftEdge = new Box(BOX_DIMENSION/2, BOX_DIMENSION, WALL_WIDTH);
            leftEdge.setMaterial(wallMaterial);
            leftEdge.setTranslateX(-FIELD_DIMENSION*(width+0.5)/2);
            leftEdge.setTranslateY(-FIELD_DIMENSION*(height+2)/2 + (i+0.5)*FIELD_DIMENSION);
            
            Box rightEdge = new Box(BOX_DIMENSION/2, BOX_DIMENSION, WALL_WIDTH);
            rightEdge.setMaterial(wallMaterial);
            rightEdge.setTranslateX(FIELD_DIMENSION*(width+0.5)/2);
            rightEdge.setTranslateY(-FIELD_DIMENSION*(height+2)/2 + (i+0.5)*FIELD_DIMENSION);
            
            edge.getChildren().addAll(leftEdge, rightEdge);      
        }
        
        Box corner0 = new Box(BOX_DIMENSION/2, BOX_DIMENSION/2, WALL_WIDTH);
        corner0.setMaterial(new PhongMaterial(Color.BLUE));
        corner0.setTranslateX(-FIELD_DIMENSION*(width+0.5)/2);
        corner0.setTranslateY(-FIELD_DIMENSION*(height+0.5)/2);
        
        Box corner1 = new Box(BOX_DIMENSION/2, BOX_DIMENSION/2, WALL_WIDTH);
        corner1.setMaterial(new PhongMaterial(Color.BLUE));
        corner1.setTranslateX(-FIELD_DIMENSION*(width+0.5)/2);
        corner1.setTranslateY(FIELD_DIMENSION*(height+0.5)/2);
        
        Box corner2 = new Box(BOX_DIMENSION/2, BOX_DIMENSION/2, WALL_WIDTH);
        corner2.setMaterial(new PhongMaterial(Color.BLUE));
        corner2.setTranslateX(FIELD_DIMENSION*(width+0.5)/2);
        corner2.setTranslateY(-FIELD_DIMENSION*(height+0.5)/2);
        
        Box corner3 = new Box(BOX_DIMENSION/2, BOX_DIMENSION/2, WALL_WIDTH);
        corner3.setMaterial(new PhongMaterial(Color.BLUE));
        corner3.setTranslateX(FIELD_DIMENSION*(width+0.5)/2);
        corner3.setTranslateY(FIELD_DIMENSION*(height+0.5)/2);
        
        edge.getChildren().addAll(corner0, corner1, corner2, corner3);
        
        
        this.getChildren().add(edge);
    }
}
