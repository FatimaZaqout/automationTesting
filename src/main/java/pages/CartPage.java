package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class CartPage extends BasePage {

    // Locators - CORRECTED for nopCommerce
    private final By shoppingCartLink = By.cssSelector("a.ico-cart");
    private final By cartItemRows = By.cssSelector("table.cart tbody tr");
    private final By productNames = By.cssSelector(".product-name");
    private final By quantityInputs = By.cssSelector(".qty-input");
    private final By updateCartButton = By.cssSelector("button[name='updatecart']");
    private final By removeCheckboxes = By.cssSelector("input[name='removefromcart']");
    private final By emptyCartMessage = By.cssSelector(".no-data");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.cssSelector("button[name='continueshopping']");
    private final By subtotal = By.cssSelector(".product-subtotal");
    private final By orderTotal = By.cssSelector(".order-total");

    // Product add to cart (when on product page)
    private final By addToCartButton = By.cssSelector("button.add-to-cart-button");
    private final By productBoxes = By.cssSelector(".product-item");
    private final By productTitles = By.cssSelector(".product-title a");

    // Success message
    private final By successNotification = By.cssSelector(".bar-notification.success");
    private final By closeNotification = By.cssSelector(".close");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigate to shopping cart and wait for page to load
     */
    public CartPage navigateToCart() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        waits.click(shoppingCartLink);

        try {
            // Wait for either cart items to appear OR empty cart message
            wait.until(driver -> {
                boolean hasItems = driver.findElements(cartItemRows).size() > 0;
                boolean hasEmptyMessage = driver.findElements(emptyCartMessage).size() > 0;
                return hasItems || hasEmptyMessage;
            });

            Thread.sleep(1000); // Additional buffer for dynamic content
        } catch (Exception e) {
            System.out.println("Warning: Cart page may not have loaded completely");
        }

        return this;
    }

    /**
     * Add first product from search results to cart
     * Note: Use SearchPage.search() first to get to search results
     */
    public CartPage addFirstProductToCart() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // Wait for product titles to load
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(productTitles));
            Thread.sleep(1000);

            // Get first product title link
            List<WebElement> productLinks = driver.findElements(productTitles);
            if (!productLinks.isEmpty()) {
                // Click on first product to go to details page
                productLinks.get(0).click();

                // Wait for product page to load and add to cart button to be clickable
                wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
                Thread.sleep(500);

                driver.findElement(addToCartButton).click();

                // Wait for success notification to appear
                wait.until(ExpectedConditions.presenceOfElementLocated(successNotification));
                Thread.sleep(1500);

                // Close notification if present
                closeNotificationBar();

                System.out.println("✓ Product added to cart successfully");
            } else {
                System.out.println("⚠ No products found in search results");
            }
        } catch (Exception e) {
            System.out.println("❌ Error adding product: " + e.getMessage());
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Close notification bar
     */
    public CartPage closeNotificationBar() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            if (driver.findElements(closeNotification).size() > 0) {
                wait.until(ExpectedConditions.elementToBeClickable(closeNotification));
                driver.findElement(closeNotification).click();
                Thread.sleep(500);
            }
        } catch (Exception e) {
            // Notification not present or already closed
        }
        return this;
    }

    /**
     * Get cart items count with explicit wait
     */
    public int getCartItemsCount() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

            // Wait for page to stabilize
            wait.until(driver -> {
                int count = driver.findElements(cartItemRows).size();
                return true; // Always return true, we just want to wait a bit
            });

            Thread.sleep(500);
            return driver.findElements(cartItemRows).size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Check if cart has items
     */
    public boolean hasCartItems() {
        return getCartItemsCount() > 0;
    }

    /**
     * Get product names in cart
     */
    public List<WebElement> getProductNamesInCart() {
        return driver.findElements(productNames);
    }

    /**
     * Check if product exists in cart by name
     */
    public boolean isProductInCart(String productName) {
        List<WebElement> products = getProductNamesInCart();
        for (WebElement product : products) {
            if (product.getText().toLowerCase().contains(productName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Update quantity using increment/decrement buttons
     * Works with dynamic IDs (e.g., quantity-up-19, quantity-down-25, etc.)
     *
     * @param targetQuantity The desired quantity to reach
     * @return CartPage instance for method chaining
     */
    public CartPage updateFirstProductQuantity(int targetQuantity) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // Get current quantity from the input field
            int currentQuantity = getFirstProductQuantity();
            System.out.println("Current quantity: " + currentQuantity);
            System.out.println("Target quantity: " + targetQuantity);

            // Calculate the difference to determine how many clicks needed
            int difference = targetQuantity - currentQuantity;

            // If already at target, no action needed
            if (difference == 0) {
                System.out.println("Quantity already at target value");
                return this;
            }

            // Small wait for page stability
            Thread.sleep(500);

            if (difference > 0) {
                // Need to INCREMENT (increase quantity)
                System.out.println("Incrementing by: " + difference);

                // Use XPath to find div with ID starting with "quantity-up"
                // This works for any product ID (quantity-up-19, quantity-up-25, etc.)
                By incrementSelector = By.xpath("//div[starts-with(@id, 'quantity-up')]");

                // Verify the element exists
                if (driver.findElements(incrementSelector).isEmpty()) {
                    System.out.println("❌ Increment button not found!");
                    return this;
                }

                System.out.println("✓ Found increment button");

                // Click the increment button the required number of times
                for (int i = 0; i < difference; i++) {
                    wait.until(ExpectedConditions.elementToBeClickable(incrementSelector)).click();
                    Thread.sleep(1000); // Wait 1 second between clicks for page update
                    System.out.println("  Increment click " + (i + 1) + "/" + difference);
                }

            } else {
                // Need to DECREMENT (decrease quantity)
                int decrementClicks = Math.abs(difference);
                System.out.println("Decrementing by: " + decrementClicks);

                // Use XPath to find div with ID starting with "quantity-down"
                // This works for any product ID (quantity-down-19, quantity-down-25, etc.)
                By decrementSelector = By.xpath("//div[starts-with(@id, 'quantity-down')]");

                // Verify the element exists
                if (driver.findElements(decrementSelector).isEmpty()) {
                    System.out.println("❌ Decrement button not found!");
                    return this;
                }

                System.out.println("✓ Found decrement button");

                // Click the decrement button the required number of times
                for (int i = 0; i < decrementClicks; i++) {
                    wait.until(ExpectedConditions.elementToBeClickable(decrementSelector)).click();
                    Thread.sleep(1000); // Wait 1 second between clicks for page update
                    System.out.println("  Decrement click " + (i + 1) + "/" + decrementClicks);
                }
            }

            // Wait for final update to complete before returning
            Thread.sleep(2000);
            System.out.println("✓ Quantity update completed successfully!");

        } catch (Exception e) {
            System.out.println("❌ Error updating quantity: " + e.getMessage());
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Click update cart button and wait for update to complete
     */
    public CartPage clickUpdateCart() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Click update button
            wait.until(ExpectedConditions.elementToBeClickable(updateCartButton)).click();

            // Wait for loading/refresh
            Thread.sleep(2000);

            // Wait for fresh cart elements
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(cartItemRows));
            Thread.sleep(1000);

            System.out.println("Cart updated successfully");
        } catch (Exception e) {
            System.out.println("Error clicking update cart: " + e.getMessage());
            try {
                Thread.sleep(2000); // Fallback wait
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        return this;
    }

    /**
     * Get quantity of first product - Enhanced with better wait
     */
    public int getFirstProductQuantity() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // Wait for elements to be present and visible
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(quantityInputs));
            wait.until(ExpectedConditions.visibilityOfElementLocated(quantityInputs));

            Thread.sleep(1000); // Extra wait for page stabilization

            // Get fresh element reference
            List<WebElement> qtyInputs = driver.findElements(quantityInputs);
            if (!qtyInputs.isEmpty()) {
                String qty = qtyInputs.get(0).getAttribute("value");
                System.out.println("Retrieved quantity: " + qty);

                // Validate the value
                if (qty != null && !qty.isEmpty()) {
                    return Integer.parseInt(qty);
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting quantity: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Select remove checkbox for first product
     */
    public CartPage selectRemoveFirstProduct() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(removeCheckboxes));

            List<WebElement> checkboxes = driver.findElements(removeCheckboxes);
            if (!checkboxes.isEmpty() && !checkboxes.get(0).isSelected()) {
                checkboxes.get(0).click();
                Thread.sleep(300);
            }
        } catch (Exception e) {
            System.out.println("Error selecting remove checkbox: " + e.getMessage());
        }
        return this;
    }

    /**
     * Remove first product from cart
     */
    public CartPage removeFirstProduct() {
        selectRemoveFirstProduct();
        clickUpdateCart();
        return this;
    }

    /**
     * Check if empty cart message is displayed
     */
    public boolean isEmptyCartMessageDisplayed() {
        try {
            return waits.visible(emptyCartMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get empty cart message text
     */
    public String getEmptyCartMessage() {
        try {
            return waits.visible(emptyCartMessage).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Check if checkout button is displayed
     */
    public boolean isCheckoutButtonDisplayed() {
        try {
            return waits.visible(checkoutButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Click checkout button
     */
    public CartPage clickCheckout() {
        waits.click(checkoutButton);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Click continue shopping
     */
    public CartPage clickContinueShopping() {
        waits.click(continueShoppingButton);
        return this;
    }

    /**
     * Get order total
     */
    public String getOrderTotal() {
        try {
            return waits.visible(orderTotal).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Clear entire cart
     */
    public CartPage clearCart() {
        try {
            List<WebElement> checkboxes = driver.findElements(removeCheckboxes);
            if (checkboxes.isEmpty()) {
                System.out.println("Cart is already empty");
                return this;
            }

            for (WebElement checkbox : checkboxes) {
                if (!checkbox.isSelected()) {
                    checkbox.click();
                    Thread.sleep(200);
                }
            }
            clickUpdateCart();
        } catch (Exception e) {
            System.out.println("Error clearing cart: " + e.getMessage());
        }
        return this;
    }
}