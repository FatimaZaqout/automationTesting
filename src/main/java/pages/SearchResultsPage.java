package pages;

import org.openqa.selenium.*;

public class SearchResultsPage extends BasePage {
    private final By itemTitleLinks = By.cssSelector(".product-item .details h2.product-title a");
    private final By noResult = By.cssSelector(".no-result");

    public SearchResultsPage(WebDriver driver) { super(driver); }

    public boolean hasResults() {
        return driver.findElements(itemTitleLinks).size() > 0 && driver.findElements(noResult).isEmpty();
    }

    public boolean anyTitleContains(String keyword) {
        String lower = keyword.toLowerCase();
        return driver.findElements(itemTitleLinks).stream()
                .anyMatch(a -> a.getText().toLowerCase().contains(lower));
    }
}
