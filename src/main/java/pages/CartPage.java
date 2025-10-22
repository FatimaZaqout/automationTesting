package pages;

import org.openqa.selenium.*;

public class CartPage extends BasePage {
    private final By table = By.cssSelector("table.cart");
    private final By qtyInput = By.cssSelector("input.qty-input");
    private final By updateBtn = By.name("updatecart");
    private final By subtotalCell = By.cssSelector("span.product-subtotal");
    private final By termsCheckbox = By.id("termsofservice");
    private final By checkoutBtn = By.id("checkout");

    public CartPage(WebDriver driver) { super(driver); }

    public boolean isLoaded() {
        return driver.getTitle().toLowerCase().contains("shopping cart") || driver.findElements(table).size() > 0;
    }

    public CartPage setQuantity(int qty) {
        waits.type(qtyInput, String.valueOf(qty));
        waits.click(updateBtn);
        return this;
    }

    public double lineSubtotal() {
        String txt = waits.visible(subtotalCell).getText();
        return Double.valueOf(txt.replaceAll("[^0-9.]", ""));
    }

    public CartPage agreeTerms() {
        WebElement cb = waits.visible(termsCheckbox);
        if (!cb.isSelected()) cb.click();
        return this;
    }

    public CheckoutPage checkout() {
        waits.click(checkoutBtn);
        return new CheckoutPage(driver);
    }
}
