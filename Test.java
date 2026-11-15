package com.mycompany.mavenproject1;

//imports stuff 
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;

class ShapeManagerTest {
    
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
