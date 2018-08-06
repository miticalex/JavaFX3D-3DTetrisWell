package Well.Tetriminoes;

/**
 *
 * @author AM
 */
public class TripodTetrimino extends BaseTetrimino {
    public TripodTetrimino(double fieldSize) {
        super(fieldSize);
       
        addBox(-1, 0, 0);
        addBox(0, -1, 0);
        addBox(0, 0, -1);
        addBox(0, 0, 0);
    }

    public TripodTetrimino() {
        this(Well.Well.FIELD_SIZE);
    }
}

