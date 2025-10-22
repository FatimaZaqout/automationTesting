package pages;

import org.openqa.selenium.*;

public class ComparePage extends BasePage {
    private final By table = By.cssSelector("table.compare-products-table");

    public ComparePage(WebDriver driver) { super(driver); }

    public boolean containsProduct(String name) {
        return waits.visible(table).getText().toLowerCase().contains(name.toLowerCase());
    }
}
