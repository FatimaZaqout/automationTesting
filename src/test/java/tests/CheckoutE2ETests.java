package tests;

import framework.BaseTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import pages.*;

public class CheckoutE2ETests extends BaseTest {

    @Test(description = "WF1: Guest checkout happy path")
    public void guestCheckout() {
        HomePage home = new HomePage(driver);
        assertTrue(home.isLoaded(), "Home page should load");

        // Category: Computers > Notebooks (or any category you prefer)
        CategoryPage cat = home.goToCategory("Computers", "Notebooks");
        assertTrue(cat.header().toLowerCase().contains("notebooks"));

        // Sort & open product
        cat.sortBy("Price: Low to High");
        // open first product by taking the first tile title text (simplify: choose known demo item)
        ProductPage pdp = cat.openProductByName("Asus N551JK-XO076H"); // adjust if product list changes

        // PDP
        assertTrue(pdp.getTitleText().length() > 0);
        pdp.setQuantity(2).addToCart();
        CartPage cart = pdp.goToCartFromBar();

        // Cart math & checkout
        assertTrue(cart.isLoaded(), "Cart should load");
        // lineSubtotal already reflects qty; minimal check:
        assertTrue(cart.lineSubtotal() > 0.0, "Subtotal should be > 0");
        cart.agreeTerms().checkout();

        // Checkout steps
        CheckoutPage co = new CheckoutPage(driver);
        co.checkoutAsGuest()
          .fillBilling("Fatima", "Zaqout", "fatima.auto+" + System.currentTimeMillis() + "@example.com",
                  "Jordan", "Amman", "123 Test St", "11118", "0590000000")
          .selectShippingAnyAndContinue()
          .selectPaymentCheckMoneyAndContinue()
          .continuePaymentInfo()
          .confirmOrder();

        assertTrue(co.isSuccess(), "Order success message should be visible");
        System.out.println("ORDER: " + co.getOrderNumber());
    }
}
