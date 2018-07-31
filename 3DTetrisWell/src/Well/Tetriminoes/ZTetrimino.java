package Well.Tetriminoes;

/**
 *
 * @author AM
 */
public class ZTetrimino extends Tetrimino {
    public ZTetrimino(double fieldSize) {
        super(fieldSize);
        
        addBox(-1, 0, 0);
        addBox(0, 0, 0);
        addBox(0, 1, 0);
        addBox(1, 1, 0);
    }
    
    public ZTetrimino() {
        this(Well.Well.FIELD_SIZE);
    }
}

