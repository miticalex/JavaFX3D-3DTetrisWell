package Well.Tetriminoes;

/**
 *
 * @author AM
 */
public class TargetPolycube extends Tetrimino {
    public TargetPolycube(double fieldSize) {
        super(fieldSize);
       
        addBox(0, 0, -1);
        addBox(0, 0, 0);
        addBox(0, -1, 0);
        addBox(-1, 0, 0);
        addBox(+1, 0, 0);
        addBox(0, +1, 0);
        
        add2DAppearance();
    }
    
    public TargetPolycube() {
        this(Well.Well.FIELD_SIZE);
    }
    private void add2DAppearance() {
        tetrimino2D = new Tetrimino2D();
        
        addSquare(0, -1, 0);
        addSquare(-1, 0, 0);
        addSquare(+1, 0, 0);
        addSquare(0, +1, 0);
        addSquare(0, 0, 0);
        addSquare(0, 0, -1);
    }
}