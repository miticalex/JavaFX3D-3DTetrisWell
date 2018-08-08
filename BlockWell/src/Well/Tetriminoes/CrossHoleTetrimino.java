package Well.Tetriminoes;

/**
 *
 * @author AM
 */
public class CrossHoleTetrimino extends BaseTetrimino {
    public CrossHoleTetrimino(double fieldSize) {
        super(fieldSize);
       
        addBox(0, -1, 0);
        addBox(-1, 0, 0);
        addBox(+1, 0, 0);
        addBox(0, +1, 0);
    }
    
    public CrossHoleTetrimino() {
        this(Well.Well.FIELD_SIZE);
    }
}
