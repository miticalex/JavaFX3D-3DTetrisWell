package Well.construction;

import javafx.scene.Group;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;

/**
 *
 * @author AM
 */
public class SteelFramework extends Group{
    private static final double BEAM_WIDTH = 0.2;
    
    private int width, height, depth;
    private double fieldSize;
    
    public SteelFramework(int width, int height, int depth, double fieldSize) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.fieldSize = fieldSize;
        
        addVerticalBeams();
        addHorizontalBeams();
        addBottomBeams();
    }
    
    private void addVerticalBeams() {
         for (int i = 0; i <= height; i++) {
            Cylinder leftBeam = zAxisBeam();
            leftBeam.setTranslateX(-0.5*width*fieldSize);
            leftBeam.setTranslateY(fieldSize*(-0.5*height + i));
            
            Cylinder rightBeam = zAxisBeam();
            rightBeam.setTranslateX(0.5*width*fieldSize);
            rightBeam.setTranslateY(fieldSize*(-0.5*height + i));
            
            this.getChildren().addAll(leftBeam, rightBeam);
        }
        
        for (int i = 1; i < width; i++) {
            Cylinder frontBeam = zAxisBeam();
            frontBeam.setTranslateX(fieldSize*(-0.5*width + i));
            frontBeam.setTranslateY(-0.5*height*fieldSize);
            
            Cylinder rearBeam = zAxisBeam();
            rearBeam.setTranslateX(fieldSize*(-0.5*width + i));
            rearBeam.setTranslateY(0.5*height*fieldSize);
            
            this.getChildren().addAll(frontBeam, rearBeam);
        }
    }

    private void addHorizontalBeams() {
        for (int i = 0; i < depth; i++) {
            Cylinder leftBeam = yAxisBeam();
            leftBeam.setTranslateX(-0.5*width*fieldSize);
            leftBeam.setTranslateZ(i*fieldSize);
            
            Cylinder rightBeam = yAxisBeam();
            rightBeam.setTranslateX(0.5*width*fieldSize);
            rightBeam.setTranslateZ(i*fieldSize);
            
            Cylinder frontBeam = xAxisBeam();
            frontBeam.setTranslateY(0.5*height*fieldSize);
            frontBeam.setTranslateZ(i*fieldSize);
            
            Cylinder rearBeam = xAxisBeam();
            rearBeam.setTranslateY(-0.5*height*fieldSize);
            rearBeam.setTranslateZ(i*fieldSize);
            
            this.getChildren().addAll(leftBeam, rightBeam, frontBeam, rearBeam);
        }
    }

    private void addBottomBeams() {
        for (int i = 0; i <= height; i++) {
            Cylinder xAxisBeam = xAxisBeam();
            xAxisBeam.setTranslateY(fieldSize*(-0.5*height + i));
            xAxisBeam.setTranslateZ(fieldSize*depth);
            
            this.getChildren().add(xAxisBeam);
        }
        for (int i = 0; i <= width; i++) {
            Cylinder yAxisBeam = yAxisBeam();
            yAxisBeam.setTranslateX(fieldSize*(-0.5*width + i));
            yAxisBeam.setTranslateZ(fieldSize*depth);
            
            this.getChildren().add(yAxisBeam);
        }
    }
    
    public final Cylinder xAxisBeam(){
        Cylinder beam = new Cylinder(BEAM_WIDTH, width*fieldSize);
        beam.setRotationAxis(Rotate.Z_AXIS);
        beam.setRotate(90.0);
        
        return beam;
    }
    
    public final Cylinder yAxisBeam(){ 
        return new Cylinder(BEAM_WIDTH, height*fieldSize); 
    }
    
    public final Cylinder zAxisBeam(){
        Cylinder beam = new Cylinder(BEAM_WIDTH, depth*fieldSize);
        beam.setRotationAxis(Rotate.X_AXIS);
        beam.setRotate(90.0);
        beam.setTranslateZ(0.5*depth*fieldSize);
        
        return beam;
    }
}
