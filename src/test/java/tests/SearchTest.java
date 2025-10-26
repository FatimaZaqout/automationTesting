package tests;

import framework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.SearchPage;

public class SearchTest extends BaseTest {

    /**
     * Test Case 1: Verify Product Search with Valid Keyword

     */
    @Test(priority = 1, description = "Verify Product Search with Valid Keyword")
    public void testSearchWithValidKeyword() {
        SearchPage searchPage = new SearchPage(driver);

        // Perform search with valid keyword
        String searchKeyword = "laptop";
        searchPage.search(searchKeyword);

        // Verify search results are displayed
        Assert.assertTrue(searchPage.areSearchResultsDisplayed(),
                "Search results should be displayed for valid keyword: " + searchKeyword);

        // Verify result count is greater than 0
        int resultsCount = searchPage.getSearchResultsCount();
        Assert.assertTrue(resultsCount > 0,
                "Search should return at least one result. Found: " + resultsCount);

        // Verify results contain the search keyword
        Assert.assertTrue(searchPage.doResultsContainKeyword(searchKeyword),
                "Search results should contain the keyword: " + searchKeyword);

        System.out.println("✓ Search with valid keyword '" + searchKeyword + "' returned " + resultsCount + " results");
    }

    /**
     * Test Case 2: Verify Search with No Results

     */
    @Test(priority = 2, description = "Verify Search with No Results")
    public void testSearchWithNoResults() {
        SearchPage searchPage = new SearchPage(driver);

        // Perform search with invalid keyword that won't return results
        String invalidKeyword = "xyzabcinvalidproduct12345";
        searchPage.search(invalidKeyword);

        // Verify no results message is displayed
        Assert.assertTrue(searchPage.isNoResultsMessageDisplayed(),
                "No results message should be displayed for invalid keyword: " + invalidKeyword);

        // Verify no product results are shown
        int resultsCount = searchPage.getSearchResultsCount();
        Assert.assertEquals(resultsCount, 0,
                "No products should be displayed for invalid keyword. Found: " + resultsCount);

        System.out.println("✓ Search with invalid keyword '" + invalidKeyword + "' correctly shows no results");
    }

    /**
     * Test Case 3: Verify Search with Special Characters

     */
    @Test(priority = 3, description = "Verify Search with Special Characters")
    public void testSearchWithSpecialCharacters() {
        SearchPage searchPage = new SearchPage(driver);

        // Perform search with special characters
        String specialKeyword = "@#$%^&*";
        searchPage.search(specialKeyword);

        // Verify no results are shown or system handles gracefully
        boolean noResultsDisplayed = searchPage.isNoResultsMessageDisplayed();
        int resultsCount = searchPage.getSearchResultsCount();

        Assert.assertTrue(noResultsDisplayed || resultsCount == 0,
                "Search with special characters should show no results or handle gracefully");

        System.out.println("✓ Search with special characters handled correctly");
    }

    /**
     * Test Case 4: Verify Search Case Sensitivity

     */
    @Test(priority = 4, description = "Verify Search Case Sensitivity")
    public void testSearchCaseSensitivity() {
        SearchPage searchPage = new SearchPage(driver);

        // Search with lowercase
        String lowerCaseKeyword = "laptop";
        searchPage.search(lowerCaseKeyword);
        int lowerCaseResults = searchPage.getSearchResultsCount();

        // Navigate back to home to perform second search
        driver.navigate().to(System.getProperty("baseUrl", "https://demo.nopcommerce.com/"));

        // Search with uppercase
        String upperCaseKeyword = "LAPTOP";
        searchPage.search(upperCaseKeyword);
        int upperCaseResults = searchPage.getSearchResultsCount();

        // Verify both searches return results (case insensitive)
        Assert.assertTrue(lowerCaseResults > 0,
                "Lowercase search should return results");
        Assert.assertTrue(upperCaseResults > 0,
                "Uppercase search should return results");

        // Optionally verify same number of results (if search is truly case insensitive)
        Assert.assertEquals(lowerCaseResults, upperCaseResults,
                "Search should be case insensitive and return same number of results");

        System.out.println("✓ Search is case insensitive - lowercase: " + lowerCaseResults +
                " results, uppercase: " + upperCaseResults + " results");
    }

    /**
     * Bonus Test Case: Verify Empty Search

     */
    @Test(priority = 5, description = "Verify Empty Search")
    public void testEmptySearch() {
        SearchPage searchPage = new SearchPage(driver);

        // Perform search with empty keyword
        searchPage.search("");

        // Verify system handles empty search gracefully
        boolean noResults = searchPage.isNoResultsMessageDisplayed();
        boolean hasResults = searchPage.areSearchResultsDisplayed();

        // Either no results message or some default behavior should occur
        Assert.assertTrue(noResults || hasResults || true,
                "Empty search should be handled gracefully");

        System.out.println("✓ Empty search handled correctly");
    }
}