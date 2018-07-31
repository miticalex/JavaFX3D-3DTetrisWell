package Well.Tetriminoes;

/**
 *
 * @author AM
 */
public class TargetPolycube extends Tetrimino {
    public TargetPolycube(double fieldSize) {
        super(fieldSize);
       
        addBox(0, -1, 0);
        addBox(-1, 0, 0);
        addBox(+1, 0, 0);
        addBox(0, +1, 0);
        addBox(0, 0, 0);
        addBox(0, 0, -1);
    }
    
    public TargetPolycube() {
        this(Well.Well.FIELD_SIZE);
    }
}