package tests;

import framework.BaseTest;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.CategoryPage;

import static org.testng.Assert.assertTrue;

public class CategoryTests extends BaseTest {

    @Test(description = "TC-014 Sort by Name: A to Z on Cell phones", groups = {"smoke"})
    public void sortByNameAtoZ_CellPhones() {
        CategoryPage cat = new HomePage(driver).open()
                .goToCategory("Electronics", "Cell phones");
        cat.sortBy("Name: A to Z");
        assertTrue(cat.namesAscending(), "Names should be A → Z");
    }

    @Test(description = "TC-015 Sort by Price: Low to High on Notebooks", groups = {"smoke"})
    public void sortByPriceLowToHigh_Notebooks() {
        CategoryPage cat = new HomePage(driver).open()
                .goToCategory("Computers", "Notebooks");
        cat.sortBy("Price: Low to High");
        assertTrue(cat.pricesAscending(), "Prices should be low → high");
    }
}
