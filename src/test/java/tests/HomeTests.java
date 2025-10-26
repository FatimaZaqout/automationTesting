package tests;

import framework.BaseTest;
import org.testng.annotations.Test;
import pages.HomePage;
import static org.testng.Assert.*;

public class HomeTests extends BaseTest {

    @Test (description = "TC-001 Verify Home Page Title", groups = {"smoke"})
    public void verifyHomeTitle() {
        HomePage home = new HomePage(driver).open();
        assertTrue(home.titleContains("nopCommerce"), "Title should contain 'nopCommerce'");
    }

    @Test (description = "TC-002 Verify Home Page Logo Display", groups = {"smoke"})
    public void verifyHomeLogoDisplayedAndClickable() {
        HomePage home = new HomePage(driver).open();
        assertTrue(home.isLogoVisible(), "Logo should be visible");
        assertTrue(home.isLogoClickable(), "Logo should be clickable");
        home.clickLogo();
    }

    @Test (description = "TC-003 Change currency to Euro", groups = {"smoke"})
    public void verifyChangeCurrencyToEuro() {
        HomePage home = new HomePage(driver).open();
        home.changeCurrencyToEuro();
        assertTrue(home.pricesShowEuroSymbol(),
                "Prices should show € symbol after changing currency to Euro");
    }

    @Test (description = "TC-004 Verify first three main menu items show dropdowns on hover", groups = {"smoke"})
    public void verifyLeftThreeDropdowns() {
        HomePage home = new HomePage(driver).open();
        assertTrue(home.verifyLeftThreeDropdowns(),
                "Left three menu items should open dropdowns on hover");
    }

    @Test (description = "TC-005 Verify direct main menu links change URL on click", groups = {"smoke"})
    public void verifyDirectMenuLinksOpen() {
        HomePage home = new HomePage(driver).open();
        assertTrue(home.verifyDirectMenuLinksOpen(),
                "Direct menu items (Digital downloads, Books, Jewelry, Gift Cards) should change URL when clicked");
    }

    @Test (description = "TC-006 Verify Search Box Functionality", groups = {"smoke"} )
    public void verifySearchBoxFunctionality() {
        HomePage home = new HomePage(driver).open();
        assertTrue(home.verifySearchFunctionality("laptop"),
                "Search results should appear and show relevant products");
    }
 


   

}
