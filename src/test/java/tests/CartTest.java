package tests;

import framework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.SearchPage;

public class CartTest extends BaseTest {

    /**
     * TC-001: Verify Add Product to Shopping Cart
     * Priority: HIGH
     */
    @Test(priority = 1, description = "TC-001: Verify adding product to cart")
    public void testCase1_AddProductToCart() {
        SearchPage searchPage = new SearchPage(driver);
        CartPage cartPage = new CartPage(driver);

        // Search for a product using SearchPage
        searchPage.search("laptop");

        // Add first product to cart
        cartPage.addFirstProductToCart();

        // Navigate to cart
        cartPage.navigateToCart();

        // Verify product is in cart
        Assert.assertTrue(cartPage.hasCartItems(),
                "Cart should contain items after adding product");
        Assert.assertTrue(cartPage.getCartItemsCount() > 0,
                "Cart items count should be greater than 0");

        System.out.println("✓ TC-018 PASSED: Product added to cart successfully");
        System.out.println("  Cart items count: " + cartPage.getCartItemsCount());
    }

    /**
     * TC-002: Verify Update Product Quantity in Cart
     * Priority: HIGH

     */
    @Test(priority = 2, description = "TC-002: Verify updating product quantity using increment/decrement buttons")
    public void testCase2_UpdateProductQuantity() {
        CartPage cartPage = new CartPage(driver);

        // Navigate to cart (assuming product already added from previous test)
        cartPage.navigateToCart();

        // Get initial quantity
        int initialQuantity = cartPage.getFirstProductQuantity();
        System.out.println("  Initial quantity: " + initialQuantity);

        // Update quantity to 3 using increment/decrement buttons
        int targetQuantity = 3;
        cartPage.updateFirstProductQuantity(targetQuantity);

        // Wait a bit for any final updates to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify quantity updated to target value
        int finalQuantity = cartPage.getFirstProductQuantity();
        Assert.assertEquals(finalQuantity, targetQuantity,
                "Product quantity should be updated to " + targetQuantity);

        System.out.println("✓ TC-002 PASSED: Product quantity updated successfully using buttons");
        System.out.println("  Initial quantity: " + initialQuantity);
        System.out.println("  Target quantity: " + targetQuantity);
        System.out.println("  Final quantity: " + finalQuantity);
    }

    /**
     * TC-003: Verify Remove Product from Cart
     * Priority: HIGH
     */
    @Test(priority = 3, description = "TC-003: Verify removing product from cart")
    public void testCase3_RemoveProductFromCart() {
        CartPage cartPage = new CartPage(driver);

        // Navigate to cart
        cartPage.navigateToCart();

        // Get initial cart count
        int initialCount = cartPage.getCartItemsCount();
        System.out.println("  Initial cart items: " + initialCount);

        // Remove first product
        cartPage.removeFirstProduct();

        // Verify product removed
        int updatedCount = cartPage.getCartItemsCount();
        Assert.assertTrue(updatedCount < initialCount || updatedCount == 0,
                "Cart items count should decrease after removing product");

        System.out.println("✓ TC-003 PASSED: Product removed from cart successfully");
        System.out.println("  Updated cart items: " + updatedCount);
    }

    /**
     * TC-004: Verify Empty Cart Message
     * Priority: MEDIUM
     */
    @Test(priority = 4, description = "TC-004: Verify empty cart message is displayed")
    public void testCase4_EmptyCartMessage() {
        CartPage cartPage = new CartPage(driver);

        // Navigate to cart
        cartPage.navigateToCart();

        // Clear all items from cart if any exist
        if (cartPage.hasCartItems()) {
            cartPage.clearCart();
        }

        // Verify empty cart message
        Assert.assertTrue(cartPage.isEmptyCartMessageDisplayed(),
                "Empty cart message should be displayed when cart is empty");

        String emptyMessage = cartPage.getEmptyCartMessage();
        Assert.assertTrue(
                emptyMessage.toLowerCase().contains("empty") ||
                        emptyMessage.toLowerCase().contains("no items"),
                "Message should indicate cart is empty");

        System.out.println("✓ TC-021 PASSED: Empty cart message displayed correctly");
        System.out.println("  Message: " + emptyMessage);
    }


    @Test(priority = 5, description = "Setup: Add product for checkout tests")
    public void setupProductForCheckout() {
        SearchPage searchPage = new SearchPage(driver);
        CartPage cartPage = new CartPage(driver);

        // Search and add product
        searchPage.search("laptop");
        cartPage.addFirstProductToCart();

        // Verify product added
        cartPage.navigateToCart();
        Assert.assertTrue(cartPage.hasCartItems(),
                "Cart should have items for checkout tests");

        System.out.println("✓ Setup complete: Product added for checkout tests");
    }
}