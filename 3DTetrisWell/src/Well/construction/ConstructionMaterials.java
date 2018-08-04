package Well.construction;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

/**
 *
 * @author AM
 */
public final class ConstructionMaterials {
    private static final Color GAMER_COLOR = Color.color(0, 0.1, 1, 0.5);// alternative color (0.0, 0.1, 0.4, 1); // Greenish DARK BLUE
    private static final Color GAMER_COLOR_OPAQUE = Color.color(0, 0.1, 1, 1);
    private static final Color GAMER_COLOR_TRANSPARENT = Color.color(0.0, 0.1, 1, 0.0);
    private static final Color GAMER_BOTTOM_COLOR = Color.color(0.0, 0.04, 0.4, 1);
    
    public static final Color[] fallenBlocksColors = {
        Color.color(0.2, 0.1, 0), Color.GREEN, Color.color(0.5, 0, 0), Color.PURPLE, Color.DARKBLUE, 
        Color.color(0.7, 0.7, 0), Color.VIOLET, Color.AQUA 
    };
    public static final Color[] fallenBlocksColors2D = {
        Color.color(0.4, 0.2, 0), Color.LIMEGREEN, Color.RED, Color.PURPLE, Color.BLUE, 
        Color.YELLOW, Color.VIOLET, Color.AQUA 
    };
    
    public static final PhongMaterial realisticWallMaterial = new PhongMaterial(); 
    public static final PhongMaterial gamerWallMaterial = new PhongMaterial();
    public static final PhongMaterial realisticShiningWallMaterial = new PhongMaterial(); // SAME COLOR - WITH RED SELF ILLUMINATION
    public static final PhongMaterial gamerShiningWallMaterial = new PhongMaterial(); // SAME COLOR - WITH RED SELF ILLUMINATION
    public static final PhongMaterial realisticEdgeMaterial = new PhongMaterial();
    public static final PhongMaterial gamerEdgeMaterial = new PhongMaterial();
    public static final PhongMaterial realisticBottomMaterial = new PhongMaterial();
    public static final PhongMaterial gamerBottomMaterial = new PhongMaterial();
    
    public static final PhongMaterial[] fallenBlocksMaterials = new PhongMaterial[fallenBlocksColors.length];
    
    private static final void instantiateMaterials(){
        realisticWallMaterial.setDiffuseMap(new Image("resources/WellBricksDiffuse.png"));
        realisticWallMaterial.setBumpMap(new Image("resources/WellBricksBump.png"));
        realisticWallMaterial.setSpecularMap(new Image("resources/WellBricksSpecular.png"));
        
        gamerWallMaterial.setDiffuseColor(GAMER_COLOR);
        gamerWallMaterial.setBumpMap(new Image("resources/cubeBumpMap.png"));
        
        realisticShiningWallMaterial.setDiffuseMap(new Image("resources/WellBricksDiffuse.png"));
        realisticShiningWallMaterial.setBumpMap(new Image("resources/WellBricksBump.png"));
        realisticShiningWallMaterial.setSpecularMap(new Image("resources/WellBricksSpecular.png"));
        realisticShiningWallMaterial.setSelfIlluminationMap(new Image("resources/transparentRed.png"));
        
        gamerShiningWallMaterial.setDiffuseColor(GAMER_COLOR);
        gamerShiningWallMaterial.setSelfIlluminationMap(new Image("resources/opaqueRed.png"));
        gamerShiningWallMaterial.setBumpMap(new Image("resources/cubeBumpMap.png"));
        
        realisticEdgeMaterial.setDiffuseMap(new Image("resources/StonesDiffuse.jpg"));
        realisticEdgeMaterial.setBumpMap(new Image("resources/StonesBump.jpg"));
        realisticEdgeMaterial.setSpecularMap(new Image("resources/StonesSpecular.jpg"));
        
        gamerEdgeMaterial.setDiffuseColor(GAMER_COLOR_OPAQUE);
        gamerEdgeMaterial.setBumpMap(new Image("resources/cubeBumpMap.png"));
        
        realisticBottomMaterial.setDiffuseMap(new Image("resources/BottomDiffuse.jpg"));
        realisticBottomMaterial.setBumpMap(new Image("resources/BottomBump.jpg"));
        realisticBottomMaterial.setSpecularMap(new Image("resources/BottomSpecular.jpg"));
        
        gamerBottomMaterial.setDiffuseColor(GAMER_BOTTOM_COLOR);
        gamerBottomMaterial.setSpecularColor(Color.BLACK);
        gamerBottomMaterial.setBumpMap(new Image("resources/BottomBump.jpg"));
        gamerBottomMaterial.setSpecularMap(new Image("resources/BottomSpecular.jpg"));
        
        for (int i = 0; i < fallenBlocksMaterials.length; i++) {
            fallenBlocksMaterials[i] = new PhongMaterial(fallenBlocksColors[i]);
            fallenBlocksMaterials[i].setSpecularColor(Color.color(0.25, 0.25, 0.25));
            fallenBlocksMaterials[i].setBumpMap(new Image("resources/cubeBumpMap.png"));
        }
    }

    public ConstructionMaterials() {
        instantiateMaterials();
    }
}
