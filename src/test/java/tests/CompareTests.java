package tests;

import framework.BaseTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import pages.*;

public class CompareTests extends BaseTest {

    @Test(description = "WF2: Add two items to compare and validate table")
    public void compareTwoProducts() {
        HomePage home = new HomePage(driver);
        CategoryPage cat = home.goToCategory("Electronics", "Cell phones");

        cat.addToCompare("HTC One M8 Android L 5.0 Lollipop")
           .addToCompare("Nokia Lumia 1020");
        ComparePage cmp = cat.openCompareFromBar();

        assertTrue(cmp.containsProduct("HTC One M8"), "Compare should include HTC");
        assertTrue(cmp.containsProduct("Nokia Lumia"), "Compare should include Nokia");
    }
}
