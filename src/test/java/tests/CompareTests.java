package tests;

import framework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;

public class CompareTests extends BaseTest {

	@Test(description = "TC-041 Verify Add Products to Compare List", groups = {"smoke"})
	public void add_two_cellphones_then_open_compare() {
	    new HomePage(driver).open()
	                        .goToCategory("Electronics", "Cell phones");

	    pages.ComparePage compare = new pages.CategoryPage(driver)
	    		.addFirstNToCompare(2)
	            .openComparePageFromFooter();

	    Assert.assertTrue(driver.getCurrentUrl().contains("/compareproducts"),
	            "Expected to be on compare products page.");

	    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(8))
	            .until(org.openqa.selenium.support.ui.ExpectedConditions
	                    .numberOfElementsToBeMoreThan(
	                            org.openqa.selenium.By.cssSelector("tr.product-name td a"), 1));
	    java.util.List<org.openqa.selenium.WebElement> names =
	            driver.findElements(org.openqa.selenium.By.cssSelector("tr.product-name td a"));
	    org.testng.Assert.assertTrue(names.size() >= 2,
	            "Expected at least 2 products in compare grid.");
	}

    
}
