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
    public static final double  UNIT = 10.0;
    public static final double  BOX_DIMENSION = UNIT-0.2;
    public static final double  WALL_WIDTH = 0.4;
    
    private int width, height, depth;
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
        
        PhongMaterial wallMaterial = new PhongMaterial(Color.color(0, 0, 1, 0.4));
        
        leftWall = new Box[height][depth];
        rightWall = new Box[height][depth];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < depth; j++) {
                leftWall[i][j] = new Box(WALL_WIDTH, BOX_DIMENSION, BOX_DIMENSION);
                rightWall[i][j] = new Box(WALL_WIDTH, BOX_DIMENSION, BOX_DIMENSION);
                
                leftWall[i][j].setMaterial(wallMaterial);
                rightWall[i][j].setMaterial(wallMaterial);
                
                leftWall[i][j].setTranslateX(-(UNIT*width/2 + WALL_WIDTH/2));
                leftWall[i][j].setTranslateY(-UNIT*height/2 + (i+0.5)*UNIT);
                leftWall[i][j].setTranslateZ(UNIT/2 + j*UNIT);
                
                rightWall[i][j].setTranslateX(UNIT*width/2 + WALL_WIDTH/2);
                rightWall[i][j].setTranslateY(-UNIT*height/2 + (i+0.5)*UNIT);
                rightWall[i][j].setTranslateZ(UNIT/2 + j*UNIT);
                
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
                
                frontWall[i][j].setTranslateX(-UNIT*width/2 + (i+0.5)*UNIT);
                frontWall[i][j].setTranslateY(UNIT*height/2 + WALL_WIDTH/2);
                frontWall[i][j].setTranslateZ(UNIT/2 + j*UNIT);
                
                rearWall[i][j].setTranslateX(-UNIT*width/2 + (i+0.5)*UNIT);
                rearWall[i][j].setTranslateY(-(UNIT*height/2 + WALL_WIDTH/2));
                rearWall[i][j].setTranslateZ(UNIT/2 + j*UNIT);
                
                this.getChildren().addAll(rearWall[i][j], frontWall[i][j]);
            }   
        }
        
        bottom = new Box[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                bottom[i][j] = new Box(BOX_DIMENSION, BOX_DIMENSION, WALL_WIDTH);
                bottom[i][j].setMaterial(wallMaterial);
                
                bottom[i][j].setTranslateX(-UNIT*width/2 + (i+0.5)*UNIT);
                bottom[i][j].setTranslateY(-UNIT*height/2 + (j+0.5)*UNIT);
                bottom[i][j].setTranslateZ(UNIT*depth + WALL_WIDTH/2);
                
                this.getChildren().addAll(bottom[i][j]);
            }   
        }
        
        for (int i = 0; i < width+2; i++) {
            Box rearEdge = new Box(BOX_DIMENSION, BOX_DIMENSION, WALL_WIDTH);
            rearEdge.setMaterial(wallMaterial);
            rearEdge.setTranslateX(-UNIT*(width+2)/2 + (i+0.5)*UNIT);
            rearEdge.setTranslateY(-UNIT*(height+1)/2);
            
            Box frontEdge = new Box(BOX_DIMENSION, BOX_DIMENSION, WALL_WIDTH);
            frontEdge.setMaterial(wallMaterial);
            frontEdge.setTranslateX(-UNIT*(width+2)/2 + (i+0.5)*UNIT);
            frontEdge.setTranslateY(UNIT*(height+1)/2);
            
            edge.getChildren().addAll(rearEdge, frontEdge);      
        }
        
        for (int i = 0; i < height+2; i++) {
            Box leftEdge = new Box(BOX_DIMENSION, BOX_DIMENSION, WALL_WIDTH);
            leftEdge.setMaterial(wallMaterial);
            leftEdge.setTranslateX(-UNIT*(width+1)/2);
            leftEdge.setTranslateY(-UNIT*(height+2)/2 + (i+0.5)*UNIT);
            
            Box rightEdge = new Box(BOX_DIMENSION, BOX_DIMENSION, WALL_WIDTH);
            rightEdge.setMaterial(wallMaterial);
            rightEdge.setTranslateX(UNIT*(width+1)/2);
            rightEdge.setTranslateY(-UNIT*(height+2)/2 + (i+0.5)*UNIT);
            
            edge.getChildren().addAll(leftEdge, rightEdge);      
        }
        
        this.getChildren().add(edge);
    }
}
