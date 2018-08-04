package Well.Tetriminoes;

/**
 *
 * @author AM
 */
public class CornerTetrimino extends Tetrimino {
    public CornerTetrimino(double fieldSize) {
        super(fieldSize);
       
        addBox(0, -1, 0);
        addBox(-1, 0, 0);
        addBox(0, 0, 0);
    }
    
    public CornerTetrimino() {
        this(Well.Well.FIELD_SIZE);
    }
}
