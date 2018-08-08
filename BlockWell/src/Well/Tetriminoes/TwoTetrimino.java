/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Well.Tetriminoes;

/**
 *
 * @author AM
 */
public class TwoTetrimino extends BaseTetrimino {
    public TwoTetrimino(double fieldSize) {
        super(fieldSize);
       
        addBox(0, 0, 0);
        addBox(-1, 0, 0);
    }
    
    public TwoTetrimino() {
        this(Well.Well.FIELD_SIZE);
    }
}
