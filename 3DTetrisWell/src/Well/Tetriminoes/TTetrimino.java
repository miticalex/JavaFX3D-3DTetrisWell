package Well.Tetriminoes;

/**
 *
 * @author AM
 */
public class TTetrimino extends Tetrimino {
    public TTetrimino(double fieldSize) {
        super(fieldSize);
       
        addBox(0, -1, 0);
        addBox(-1, 0, 0);
        addBox(0, 0, 0);
        addBox(0, 1, 0);
    }
    
    public TTetrimino() {
        this(Well.Well.FIELD_SIZE);
    }
}
