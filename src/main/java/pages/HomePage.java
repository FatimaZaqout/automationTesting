package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {
    private final By topMenu = By.cssSelector("ul.top-menu, ul.top-menu.notmobile");
    private final By searchBox = By.id("small-searchterms");
    private final By searchBtn = By.cssSelector("form[action='/search'] button[type='submit']");

    public HomePage(WebDriver driver) { super(driver); }

    public boolean isLoaded() {
        return driver.getTitle() != null && !driver.getTitle().isEmpty()
               && driver.findElements(topMenu).size() > 0;
    }

    public CategoryPage goToCategory(String main, String sub) {
        // navigate via link texts (works well on nopCommerce demo)
        waits.click(By.linkText(main));
        waits.click(By.linkText(sub));
        return new CategoryPage(driver);
    }

    public SearchResultsPage search(String term) {
        waits.type(searchBox, term);
        waits.click(searchBtn);
        return new SearchResultsPage(driver);
    }

    public CartPage openCartFromHeader() {
        waits.click(By.cssSelector("a.ico-cart"));
        return new CartPage(driver);
    }
}
