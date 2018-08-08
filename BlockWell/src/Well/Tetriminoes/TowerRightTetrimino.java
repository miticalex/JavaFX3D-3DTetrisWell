package Well.Tetriminoes;

/**
 *
 * @author AM
 */
public class TowerRightTetrimino extends BaseTetrimino {
    public TowerRightTetrimino(double fieldSize) {
        super(fieldSize);
       
        addBox(+1, 0, 0);
        addBox(0, +1, -1);
        addBox(0, +1, 0);
        addBox(0, 0, 0);
    }
    
    public TowerRightTetrimino() {
        this(Well.Well.FIELD_SIZE);
    }
}
