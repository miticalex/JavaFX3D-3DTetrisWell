package Well.Tetriminoes;

/**
 *
 * @author AM
 */
public class LTetrimino extends Tetrimino {
    public LTetrimino(double fieldSize) {
        super(fieldSize);
       
        addBox(0, 0, 0);
        addBox(1, 1, 0);
        addBox(0, -1, 0);
        addBox(0, 1, 0);
    }
    
    public LTetrimino() {
        this(Well.Well.FIELD_SIZE);
    }
}
