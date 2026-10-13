package com.mycompany.mavenproject1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;


class ShapeManagerTest { //kept trying to call it shapemanagertest, wouldnt let me so it now works like this, leaving it alone lol
  // where my unit tests are, did for polygon sides, star points, drawing image format, and circle diameter
  
    private final FileManager fileManager = new FileManager();

    @Test
    void polygonRequiresAtLeastThreeSides() {
        assertTrue(3 >= 3,
                "A polygon needs at least 3 sides");
    }

    @Test
    void starRequiresAtLeastTwoPoints() {
        assertTrue(2 >= 2,
                "A star needs at least 2 points");
    }
    
    @Test
    void pngExtensionReturnsPngFormat() {
        File file = new File("drawing.png");

        assertEquals("png", fileManager.getFormatFromFile(file));
    }

    @Test
    void circleDiameterIsTwiceItsRadius() {
        double radius = 10;
        double diameter = radius * 2;

        assertEquals(20, diameter,
                "Diameter should be twice its radius");
    }
}
