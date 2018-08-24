package Well.Tetriminoes;

/**
 *
 * @author AM
 */
public class JCrossPolycube extends BaseTetrimino {
    public JCrossPolycube(double fieldSize) {
        super(fieldSize);
       
        addBox(0, 0, 0);
        addBox(0, -1, 0);
        addBox(-1, 0, 0);
        addBox(+1, 0, -1);
        addBox(+1, 0, 0);
        addBox(0, +1, 0);
    }
    
    public JCrossPolycube() {
        this(Well.Well.FIELD_SIZE);
    }
}