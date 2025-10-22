package pages;

import org.openqa.selenium.*;
import java.util.*;
import java.util.stream.Collectors;

public class CategoryPage extends BasePage {
    private final By heading = By.cssSelector("div.page-title h1");
    private final By sortSelect = By.id("products-orderby");
    private final By productTile = By.cssSelector(".product-item");
    private final By productTitleLink = By.cssSelector(".product-title a");
    private final By addToCartBtnInTile = By.cssSelector("button.product-box-add-to-cart-button");
    private final By addToCompareLink = By.cssSelector("button.add-to-compare-list-button, a.add-to-compare-list-button");
    private final By barSuccess = By.cssSelector("div.bar-notification.success");

    public CategoryPage(WebDriver driver) { super(driver); }

    public String header() {
        return waits.visible(heading).getText().trim();
    }

    public CategoryPage sortBy(String visibleText) {
        waits.selectByVisibleText(sortSelect, visibleText);
        // small wait for products to refresh
        waits.visible(productTile);
        return this;
    }

    public List<Double> visiblePrices() {
        List<WebElement> tiles = driver.findElements(productTile);
        return tiles.stream()
                .map(t -> t.findElement(By.cssSelector(".prices .price, .prices .actual-price")).getText())
                .map(CategoryPage::parsePrice)
                .collect(Collectors.toList());
    }

    private static Double parsePrice(String raw) {
        // strips currency and commas e.g. "$1,200.00"
        return Double.valueOf(raw.replaceAll("[^0-9.]", ""));
    }

    public ProductPage openProductByName(String name) {
        waits.click(By.xpath("//h2[@class='product-title']/a[normalize-space()='" + name + "']"));
        return new ProductPage(driver);
    }

    public CategoryPage addToCompare(String name) {
        WebElement tile = driver.findElement(By.xpath("//h2[@class='product-title']/a[normalize-space()='" + name + "']/ancestor::*[@class='product-item']"));
        tile.findElement(addToCompareLink).click();
        waits.visible(barSuccess);
        return this;
    }

    public ComparePage openCompareFromBar() {
        waits.click(By.linkText("product comparison"));
        return new ComparePage(driver);
    }

    public CategoryPage addToCartFromTile(String name) {
        WebElement tile = driver.findElement(By.xpath("//h2[@class='product-title']/a[normalize-space()='" + name + "']/ancestor::*[@class='product-item']"));
        tile.findElement(addToCartBtnInTile).click();
        waits.visible(barSuccess);
        return this;
    }
}
