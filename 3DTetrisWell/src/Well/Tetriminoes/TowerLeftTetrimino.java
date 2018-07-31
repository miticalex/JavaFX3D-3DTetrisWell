package Well.Tetriminoes;

/**
 *
 * @author AM
 */
public class TowerLeftTetrimino extends Tetrimino {
    public TowerLeftTetrimino(double fieldSize) {
        super(fieldSize);
       
        addBox(-1, 0, 0);
        addBox(0, +1, 0);
        addBox(0, +1, -1);
        addBox(0, 0, 0);
        
    }
    public TowerLeftTetrimino() {
        this(Well.Well.FIELD_SIZE);
    }
}
