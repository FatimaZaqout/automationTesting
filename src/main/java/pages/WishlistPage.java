package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class WishlistPage extends BasePage {

    // ---------- Locators ----------
    private final By title                 = By.cssSelector("div.page-title h1");
    private final By table                 = By.cssSelector(".wishlist-content");
    private final By emptyMessage          = By.cssSelector(".wishlist-content .no-data, .no-data");

    private final By rows                  = By.cssSelector(".wishlist-content table tbody tr");
    private final By productNameInRow      = By.cssSelector(".product-name, .product-name a, td.product, td.product a");

    private final By selectCheckboxInRow   = By.cssSelector("input[type='checkbox'][name^='addtocart']");
    private final By addSelectedToCartBtn  = By.cssSelector("button[name='addtocartbutton']");
    private final By successBar            = By.cssSelector("div.bar-notification.success");

    private final By removeCheckbox        = By.cssSelector("input[name^='removefromcart']");
    private final By removeCheckboxInRow   = By.cssSelector("input[name='removefromcart']");
    private final By removeBtnInRow        = By.cssSelector("button.remove-btn");
    private final By updateBtn             = By.name("updatecart");
    private final By updateBtnById         = By.id("updatecart");
    private final By updateBtnByName       = By.name("updatecart");

    public WishlistPage(WebDriver driver) { 
    	super(driver); }

    // ---------- Internal helpers ----------
    private WebDriverWait wait10() {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private void waitForWishlistReady() {
        wait10().until(d ->
            !d.findElements(table).isEmpty() || !d.findElements(emptyMessage).isEmpty()
        );
    }

    private String rowProductText(WebElement row) {
        List<WebElement> els = row.findElements(productNameInRow);
        for (WebElement e : els) {
            String t = e.getText().trim();
            if (!t.isEmpty()) return t;
        }
        return row.getText().trim(); // fallback
    }

    private boolean isEmptyStateVisible() {
        return !driver.findElements(emptyMessage).isEmpty();
    }

    // ---------- Public API (kept as-is) ----------

    /** Page is considered loaded if title is visible and either table or empty-state is present. */
    public boolean isLoaded() {
        waits.visible(title);
        boolean hasTable = !driver.findElements(table).isEmpty();
        boolean hasEmpty = isEmptyStateVisible();
        return hasTable || hasEmpty;
    }

    /** Remove first item by checking the global remove checkbox + Update. */
    public WishlistPage removeFirstItem() {
        waits.visible(removeCheckbox).click();
        waits.click(updateBtn);
        return this;
    }

    /** Remove a product by name (or partial) using row remove button if present, otherwise checkbox + update. */
    public WishlistPage removeProductByRowButton(String nameOrPartial) {
        List<WebElement> initialRows = driver.findElements(rows);
        int before = initialRows.size();

        // If empty already, nothing to remove
        if (before == 0 && isEmptyStateVisible()) return this;

        // Find target row
        WebElement targetRow = null;
        for (WebElement r : initialRows) {
            if (rowProductText(r).toLowerCase().contains(nameOrPartial.toLowerCase())) {
                targetRow = r; break;
            }
        }
        if (targetRow == null) {
            if (isEmptyStateVisible()) return this; // empty list, nothing to remove
            throw new NoSuchElementException("Product not found in wishlist: " + nameOrPartial);
        }

        // Prefer row remove button; fallback to checkbox + update
        List<WebElement> removeBtns = targetRow.findElements(removeBtnInRow);
        if (!removeBtns.isEmpty()) {
            removeBtns.get(0).click();
        } else {
            WebElement cb = targetRow.findElement(removeCheckboxInRow);
            if (!cb.isSelected()) cb.click();

            if (!driver.findElements(updateBtnById).isEmpty()) {
                waits.click(updateBtnById);
            } else if (!driver.findElements(updateBtnByName).isEmpty()) {
                waits.click(updateBtnByName);
            } else {
                throw new NoSuchElementException("Update button not found (#updatecart or name=updatecart)");
            }
        }

        // Wait until: empty state appears OR rows decrease OR product disappears
        wait10().until(d -> {
            if (!d.findElements(emptyMessage).isEmpty()) return true;
            List<WebElement> nowRows = d.findElements(rows);
            if (nowRows.size() < before) return true;
            for (WebElement r : nowRows) {
                if (rowProductText(r).toLowerCase().contains(nameOrPartial.toLowerCase())) {
                    return false;
                }
            }
            return true;
        });

        return this;
    }

    /** Check if wishlist contains a product by (partial) name. */
    public boolean hasProduct(String expectedName) {
        waitForWishlistReady();
        if (isEmptyStateVisible()) return false;

        for (WebElement r : driver.findElements(rows)) {
            if (rowProductText(r).toLowerCase().contains(expectedName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /** Hard refresh + wait for table (kept same signature/behavior). */
    public WishlistPage refresh() {
        driver.navigate().refresh();
        waits.visible(table);
        return this;
    }

    /** Select a product row by exact name (case-insensitive) and tick its "add to cart" checkbox. */
    public WishlistPage selectProduct(String name) {
        waits.visible(table);
        for (WebElement r : driver.findElements(rows)) {
            if (rowProductText(r).equalsIgnoreCase(name.trim())) {
                WebElement cb = r.findElement(selectCheckboxInRow);
                if (!cb.isSelected()) cb.click();
                return this;		
            }
        }
        throw new NoSuchElementException("Product not found: " + name);
    }

    /** Click "Add selected to cart" and wait for success bar. */
    public WishlistPage clickAddSelectedToCart() {
        waits.click(addSelectedToCartBtn);
        waits.visible(successBar);
        return this;
    }
}
