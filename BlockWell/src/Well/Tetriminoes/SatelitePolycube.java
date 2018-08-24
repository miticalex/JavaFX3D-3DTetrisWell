package Well.Tetriminoes;

/**
 *
 * @author AM
 */
public class SatelitePolycube extends BaseTetrimino {
    public SatelitePolycube(double fieldSize) {
        super(fieldSize);
       
        addBox(0, 0, 0);
        addBox(0, 0, -1);
        addBox(0, 0, +1);
        addBox(0, -1, 0);
        addBox(-1, 0, 0);
        addBox(+1, 0, 0);
        addBox(0, +1, 0);
    }
    
    public SatelitePolycube() {
        this(Well.Well.FIELD_SIZE);
    }
}