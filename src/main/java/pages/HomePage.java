package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends BasePage {

    private static final String HOME_URL = System.getProperty("baseUrl", "https://demo.nopcommerce.com/");
    private static final String EXPECTED_TITLE = "nopCommerce demo store";

    private final By loginLink        = By.cssSelector("a.ico-login");
    private final By topMenu          = By.cssSelector("ul.top-menu, ul.top-menu.notmobile");
    private final By menuItems        = By.cssSelector("ul.top-menu > li > a");
    private final By logo             = By.cssSelector("div.header-logo a, a.logo, .header-logo a");
    private final By currencyDropdown = By.id("customerCurrency");
    private final By productPrices    = By.cssSelector("span.price.actual-price, span.price");

    
    private final By featuredProducts = By.cssSelector(".product-item");
    private final By wishlistBtn      = By.cssSelector("button.add-to-wishlist-button");

    private final By searchBox        = By.id("small-searchterms");
    private final By searchSubmit     = By.cssSelector("form[action='/search'] button[type='submit']");
    private final By resultsArea      = By.cssSelector(".search-results, .product-item");

    private String lastAddedWishlistProduct;

    public HomePage(WebDriver driver) { 
    	super(driver); }

    /* */

    public HomePage open() {
        driver.get(HOME_URL);
        waits.visible(topMenu);
        return this;
    }

    public String title() {
        String t = driver.getTitle();
        return t == null ? "" : t.trim();
    }

    public boolean titleContains(String text) {
        return title().toLowerCase().contains(text.toLowerCase());
    }

    public boolean isAtExpectedTitle() {
        return EXPECTED_TITLE.equals(title());
    }

    /* */

    public boolean isLogoVisible() {
        try { return waits.visible(logo).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    public boolean isLogoClickable() {
        try {
            WebElement el = waits.visible(logo);
            return el.isDisplayed() && el.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public HomePage clickLogo() {
        waits.click(logo);
        waits.visible(topMenu);
        return this;
    }

    /* */

    public HomePage changeCurrencyToEuro() {
        waits.selectByVisibleText(currencyDropdown, "Euro");
        waits.visible(productPrices);
        return this;
    }

    public boolean pricesShowEuroSymbol() {
        for (WebElement price : driver.findElements(productPrices)) {
            if (!price.getText().contains("€")) return false;
        }
        return true;
    }

    /* */
    public List<String> getMainMenuItems() {
        List<String> items = new ArrayList<>();
        for (WebElement el : driver.findElements(menuItems)) {
            items.add(el.getText().trim());
        }
        return items;
    }

    public boolean hasExpectedMenuItems() {
        String[] expected = {
            "Computers", "Electronics", "Apparel",
            "Digital downloads", "Books", "Jewelry", "Gift Cards"
        };
        List<String> actual = getMainMenuItems();
        for (String e : expected) {
            if (!actual.contains(e)) return false;
        }
        return true;
    }

    public boolean verifyLeftThreeDropdowns() {
        String[] leftThree = { "Computers", "Electronics", "Apparel" };
        try {
            Actions actions = new Actions(driver);

            for (String label : leftThree) {
                WebElement item = waits.visible(By.linkText(label));
                ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({block:'center'});", item);

                actions.moveToElement(item).pause(Duration.ofMillis(500)).perform();

                WebElement liParent = item.findElement(By.xpath("./ancestor::li[1]"));
                List<WebElement> subLinks = liParent.findElements(By.cssSelector("ul.sublist a"));

                boolean hasVisibleSub = false;
                for (WebElement a : subLinks) {
                    if (a.isDisplayed()) { hasVisibleSub = true; break; }
                }
                if (!hasVisibleSub) {
                    System.out.println("Dropdown did not appear on hover: " + label);
                    return false;
                } else {
                    System.out.println("Dropdown appeared on hover: " + label);
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("verifyLeftThreeDropdowns Exception: " + e.getMessage());
            return false;
        }
    }

    public boolean verifyDirectMenuLinksOpen() {
        String[] directItems = { "Digital downloads", "Books", "Jewelry", "Gift Cards" };
        try {
            for (String label : directItems) {
                waits.visible(By.linkText(label));
                String currentUrl = driver.getCurrentUrl();

                waits.click(By.linkText(label));

                WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(8));
                boolean urlChanged = shortWait.until(
                    ExpectedConditions.not(ExpectedConditions.urlToBe(currentUrl))
                );

                if (!urlChanged) {
                    System.out.println("URL did not change after clicking: " + label);
                    return false;
                } else {
                    System.out.println("URL changed after clicking: " + label);
                }

                driver.navigate().back();
                waits.visible(By.linkText(label));
            }
            return true;
        } catch (Exception e) {
            System.out.println("verifyDirectMenuLinksOpen Exception: " + e.getMessage());
            return false;
        }
    }

    public boolean verifySearchFunctionality(String keyword) {
        try {
            waits.type(searchBox, keyword);
            waits.click(searchSubmit);

            waits.visible(resultsArea);

            boolean hasProducts = !driver.findElements(By.cssSelector(".product-item")).isEmpty();
            boolean pageHasTerm = driver.getPageSource().toLowerCase().contains(keyword.toLowerCase());

            if (hasProducts || pageHasTerm) {
                System.out.println("Search results visible for: " + keyword);
                return true;
            } else {
                System.out.println("No results for: " + keyword);
                return false;
            }

        } catch (Exception e) {
            System.out.println("verifySearchFunctionality Exception: " + e.getMessage());
            return false;
        }
    }

    public WishlistPage addThirdFeaturedProductToWishlist() {
        List<WebElement> products = driver.findElements(featuredProducts);
        if (products.size() < 3) {
            throw new IllegalStateException("Less than 3 featured products on the Home page.");
        }

        WebElement third = products.get(2);
        lastAddedWishlistProduct = third.findElement(By.cssSelector(".product-title a")).getText().trim();

        third.findElement(wishlistBtn).click();

        try {
            WebElement close = driver.findElement(By.cssSelector("div.bar-notification.success span.close"));
            close.click();
        } catch (Exception ignore) {}

        waits.click(By.cssSelector("a.ico-wishlist"));
        return new WishlistPage(driver);
    }

    public String getLastAddedWishlistProduct() {
        System.out.print(lastAddedWishlistProduct);
        return lastAddedWishlistProduct;
    }

    public LoginPage goToLogin() {
        waits.click(loginLink);
        return new LoginPage(driver);
    }

    public boolean isLoaded() {
        return !title().isEmpty() && !driver.findElements(topMenu).isEmpty();
    }

    public CategoryPage goToCategory(String main, String sub) {
        waits.click(By.linkText(main));
        waits.click(By.linkText(sub));
        return new CategoryPage(driver);
    }
}
