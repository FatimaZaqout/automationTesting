package pages;

import org.openqa.selenium.*;

public class ProductPage extends BasePage {
    private final By title = By.cssSelector("div.product-name h1");
    private final By price = By.cssSelector("div.product-price span");
    private final By qty = By.cssSelector("input.qty-input, input#product_enteredQuantity_1, input#product_enteredQuantity_*");
    private final By addToCartBtn = By.cssSelector("button#add-to-cart-button-1, button.add-to-cart-button");

    private final By barSuccess = By.cssSelector("div.bar-notification.success");
    private final By shoppingCartLinkInBar = By.xpath("//div[contains(@class,'bar-notification') and contains(@class,'success')]//a[contains(.,'shopping cart')]");

    public ProductPage(WebDriver driver) { super(driver); }

    public String getTitleText() { return waits.visible(title).getText().trim(); }
    public String getPriceText() { return waits.visible(price).getText().trim(); }

    public ProductPage setQuantity(int value) {
        // supports different qty inputs across products
        WebElement input = driver.findElements(qty).isEmpty() ? waits.visible(By.cssSelector("input.qty-input")) : driver.findElements(qty).get(0);
        input.clear();
        input.sendKeys(String.valueOf(value));
        return this;
    }

    public ProductPage addToCart() {
        waits.click(addToCartBtn);
        waits.visible(barSuccess);
        return this;
    }

    public CartPage goToCartFromBar() {
        waits.click(shoppingCartLinkInBar);
        return new CartPage(driver);
    }
}
