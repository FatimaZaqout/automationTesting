package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class SearchPage extends BasePage {

    // Locators
    private final By searchBox = By.id("small-searchterms");
    private final By searchButton = By.cssSelector("button.button-1.search-box-button");
    private final By searchResults = By.cssSelector(".product-item");
    private final By noResultsMessage = By.cssSelector(".no-result");
    private final By productTitles = By.cssSelector(".product-title a");
    private final By searchPageTitle = By.cssSelector(".page-title h1");

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Enter search keyword in search box
     */
    public SearchPage enterSearchKeyword(String keyword) {
        waits.type(searchBox, keyword);
        return this;
    }

    /**
     * Click search button
     */
    public SearchPage clickSearchButton() {
        waits.click(searchButton);
        return this;
    }

    /**
     * Perform complete search operation
     */
    public SearchPage search(String keyword) {
        enterSearchKeyword(keyword);
        clickSearchButton();
        return this;
    }

    /**
     * Get all search results
     */
    public List<WebElement> getSearchResults() {
        return driver.findElements(searchResults);
    }

    /**
     * Get count of search results
     */
    public int getSearchResultsCount() {
        return getSearchResults().size();
    }

    /**
     * Check if search results are displayed
     */
    public boolean areSearchResultsDisplayed() {
        return !getSearchResults().isEmpty();
    }

    /**
     * Check if no results message is displayed
     */
    public boolean isNoResultsMessageDisplayed() {
        try {
            return waits.visible(noResultsMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get no results message text
     */
    public String getNoResultsMessageText() {
        return waits.visible(noResultsMessage).getText();
    }

    /**
     * Get all product titles from search results
     */
    public List<WebElement> getProductTitles() {
        return driver.findElements(productTitles);
    }

    /**
     * Check if product title contains keyword (case insensitive)
     */
    public boolean doResultsContainKeyword(String keyword) {
        List<WebElement> titles = getProductTitles();
        if (titles.isEmpty()) {
            return false;
        }

        for (WebElement title : titles) {
            if (title.getText().toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verify all results contain the keyword
     */
    public boolean allResultsContainKeyword(String keyword) {
        List<WebElement> titles = getProductTitles();
        if (titles.isEmpty()) {
            return false;
        }

        for (WebElement title : titles) {
            String titleText = title.getText().toLowerCase();
            if (!titleText.contains(keyword.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get search page title
     */
    public String getSearchPageTitle() {
        return waits.visible(searchPageTitle).getText();
    }

    /**
     * Get first product title text
     */
    public String getFirstProductTitle() {
        List<WebElement> titles = getProductTitles();
        return titles.isEmpty() ? "" : titles.get(0).getText();
    }
}