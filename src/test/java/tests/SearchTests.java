package tests;

import framework.BaseTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import pages.*;

public class SearchTests extends BaseTest {

    @Test(description = "WF2: Simple keyword search")
    public void searchPhone() {
        HomePage home = new HomePage(driver);
        SearchResultsPage results = home.search("phone");
        assertTrue(results.hasResults(), "Results should not be empty");
        assertTrue(results.anyTitleContains("phone") || results.anyTitleContains("Phone"),
                "At least one result title should contain 'phone'");
    }
}
