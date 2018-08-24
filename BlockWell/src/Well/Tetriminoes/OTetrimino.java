package Well.Tetriminoes;

/**
 *
 * @author AM
 */
public class OTetrimino extends BaseTetrimino {
    public OTetrimino(double fieldSize) {
        super(fieldSize);
       
        addBox(0, 0, 0);
        addBox(1, 0, 0);
        addBox(0, 1, 0);
        addBox(1, 1, 0);
    }
    
    public OTetrimino() {
        this(Well.Well.FIELD_SIZE);
    }
}
