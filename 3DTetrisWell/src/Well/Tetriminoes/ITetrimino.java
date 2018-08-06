package Well.Tetriminoes;

/**
 *
 * @author AM
 */
public class ITetrimino extends BaseTetrimino {
    public ITetrimino(double fieldSize) {
        super(fieldSize);
       
        addBox(0, 0, 0);
        addBox(-1, 0, 0);
        addBox(1, 0, 0);
    }
    
    public ITetrimino() {
        this(Well.Well.FIELD_SIZE);
    }
}

