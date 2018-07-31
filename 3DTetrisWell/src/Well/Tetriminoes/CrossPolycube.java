package Well.Tetriminoes;

/**
 *
 * @author AM
 */
public class CrossPolycube extends Tetrimino {
    public CrossPolycube(double fieldSize) {
        super(fieldSize);
       
        addBox(0, -1, 0);
        addBox(-1, 0, 0);
        addBox(+1, 0, 0);
        addBox(0, +1, 0);
        addBox(0, 0, 0);
    }
    public CrossPolycube() {
        this(Well.Well.FIELD_SIZE);
    }
}
