package tests;

import framework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.SearchPage;
import tests.RegistrationTest;


public class CheckoutTest extends BaseTest {

    /**
     * TC-001: Verify Checkout as Guest User
     * Priority: HIGH
     */
    @Test(priority = 1, description = "TC-001: Verify checkout as guest user")
    public void testCase1_CheckoutAsGuest() {
        SearchPage searchPage = new SearchPage(driver);
        CartPage cartPage = new CartPage(driver);
        CheckoutPage checkoutPage = new CheckoutPage(driver);

        // Navigate to cart
        cartPage.navigateToCart();

        // Verify cart has items
        if (!cartPage.hasCartItems()) {
            searchPage.search("laptop");
            cartPage.addFirstProductToCart();
            cartPage.navigateToCart();
        }

        // Handle terms modal, checkbox, and click checkout
        checkoutPage.handleTermsAndCheckout();

        // Checkout as guest
        checkoutPage.checkoutAsGuest();

        // Complete checkout with valid details
        String timestamp = String.valueOf(System.currentTimeMillis());
        checkoutPage.completeCheckout(
                "John",
                "Doe",
                "guest" + timestamp + "@test.com",
                "New York",
                "123 Main Street",
                "10001",
                "1234567890"
        );

        // Verify order completed
        Assert.assertTrue(checkoutPage.isOrderCompleted(),
                "Order should be completed successfully as guest");

        String successMessage = checkoutPage.getOrderSuccessMessage();
        Assert.assertTrue(successMessage.toLowerCase().contains("completed") ||
                        successMessage.toLowerCase().contains("success"),
                "Success message should be displayed");

        System.out.println("✓ TC-022 PASSED: Checkout as guest completed successfully");
        System.out.println("  " + successMessage);
    }

    /**
     * TC-002: Verify Checkout as Registered User
     * Priority: HIGH
     */
    @Test(priority = 2, description = "TC-002: Verify checkout as registered user")
    public void testCase2_CheckoutAsRegisteredUser() {
        LoginPage loginPage = new LoginPage(driver);
        SearchPage searchPage = new SearchPage(driver);
        CartPage cartPage = new CartPage(driver);
        CheckoutPage checkoutPage = new CheckoutPage(driver);

        // Check if valid credentials are available, if not skip
        if (RegistrationTest.validEmail == null || RegistrationTest.validPassword == null) {
            System.out.println("⚠ TC-002 SKIPPED: No valid user credentials available");
            System.out.println("  Please run RegistrationTest first to create a user");
            throw new org.testng.SkipException("Valid user credentials not available");
        }

        // Login with registered user
        loginPage.navigateToLogin();
        loginPage.login(RegistrationTest.validEmail, RegistrationTest.validPassword);

        Assert.assertTrue(loginPage.isLoginSuccessful(),
                "User should be logged in before checkout");

        // Add product to cart
        searchPage.search("laptop");
        cartPage.addFirstProductToCart();
        cartPage.navigateToCart();

        // Handle terms modal, checkbox, and click checkout
        checkoutPage.handleTermsAndCheckout();

        // Complete checkout (no need to checkout as guest since user is logged in)
        checkoutPage.completeCheckout(
                "Ahmad",
                "Ali",
                RegistrationTest.validEmail,
                "New York",
                "456 Second Avenue",
                "10002",
                "9876543210"
        );

        // Verify order completed
        Assert.assertTrue(checkoutPage.isOrderCompleted(),
                "Order should be completed successfully as registered user");

        String successMessage = checkoutPage.getOrderSuccessMessage();
        Assert.assertTrue(successMessage.toLowerCase().contains("completed") ||
                        successMessage.toLowerCase().contains("success"),
                "Success message should be displayed");

        System.out.println("✓ TC-023 PASSED: Checkout as registered user completed successfully");
        System.out.println("  " + successMessage);

        // Logout
        loginPage.logout();
    }

    /**
     * TC-003: Verify Mandatory Fields in Checkout
     * Priority: MEDIUM
     */
    @Test(priority = 3, description = "TC-003: Verify mandatory fields validation in checkout")
    public void testCase3_MandatoryFieldsInCheckout() {
        SearchPage searchPage = new SearchPage(driver);
        CartPage cartPage = new CartPage(driver);
        CheckoutPage checkoutPage = new CheckoutPage(driver);

        // Navigate to cart
        cartPage.navigateToCart();

        // Verify cart has items
        if (!cartPage.hasCartItems()) {
            searchPage.search("laptop");
            cartPage.addFirstProductToCart();
            cartPage.navigateToCart();
        }

        // Handle terms modal, checkbox, and click checkout
        checkoutPage.handleTermsAndCheckout();

        // Checkout as guest
        checkoutPage.checkoutAsGuest();

        // Try to continue without filling mandatory fields
        checkoutPage.clickContinueBilling();

        // Verify error messages for mandatory fields
        Assert.assertTrue(checkoutPage.hasMandatoryFieldErrors(),
                "Validation errors should be displayed for empty mandatory fields");

        System.out.println("✓ TC-003 PASSED: Mandatory fields validation working correctly");

        // Print validation notification if exists
        String notification = checkoutPage.getValidationNotification();
        if (!notification.isEmpty()) {
            System.out.println("  Validation Notification: " + notification);
        }

        // Check and print individual field errors
        String firstNameErr = checkoutPage.getFirstNameError();
        String lastNameErr = checkoutPage.getLastNameError();
        String emailErr = checkoutPage.getEmailError();
        String cityErr = checkoutPage.getCityError();
        String addressErr = checkoutPage.getAddress1Error();
        String zipErr = checkoutPage.getZipCodeError();
        String phoneErr = checkoutPage.getPhoneError();

        if (!firstNameErr.isEmpty()) System.out.println("  First Name Error: " + firstNameErr);
        if (!lastNameErr.isEmpty()) System.out.println("  Last Name Error: " + lastNameErr);
        if (!emailErr.isEmpty()) System.out.println("  Email Error: " + emailErr);
        if (!cityErr.isEmpty()) System.out.println("  City Error: " + cityErr);
        if (!addressErr.isEmpty()) System.out.println("  Address Error: " + addressErr);
        if (!zipErr.isEmpty()) System.out.println("  Zip Code Error: " + zipErr);
        if (!phoneErr.isEmpty()) System.out.println("  Phone Error: " + phoneErr);
    }
}