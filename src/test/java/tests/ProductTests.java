//package tests;
//
//import framework.BaseTest;
//import org.testng.annotations.Test;
//import pages.*;
//
//import static org.testng.Assert.*;
//
//public class ProductTests extends BaseTest {
//
//    private final String email = "Fatima@gmail.com";
//    private final String password = "YourPass123";
//
//    /** فتح صفحة منتج معروفة (HTC) من: Electronics > Cell phones */
//    private ProductPage openHTCProductFromCategory() {
//        HomePage home = new HomePage(driver).open();
//        CategoryPage cat = home.goToCategory("Electronics", "Cell phones");
//        return cat.openProductByName("HTC One M8 Android L 5.0 Lollipop");
//    }
//
//    /* ===================== أساسي: التايتل والسعر ===================== */
//
//    @Test(description = "PDP-001: Verify product title is visible on PDP (HTC)")
//    public void verifyProductTitleVisible() {
//        ProductPage pdp = openHTCProductFromCategory();
//        assertTrue(pdp.name().toLowerCase().contains("htc"), "Product title should contain 'HTC'");
//    }
//
//    @Test(description = "PDP-002: Verify product price is visible on PDP (HTC)")
//    public void verifyProductPriceVisible() {
//        ProductPage pdp = openHTCProductFromCategory();
//        assertFalse(pdp.priceText().isEmpty(), "Product price should be visible");
//    }
//
//    /* =========== TC-026-B: Guest يضيف للمفضلة من صفحة المنتج =========== */
//
//    @Test(description = "TC-026-B (Guest): Add product to wishlist from Product Page (HTC)")
//    public void addToWishlist_FromProduct_AsGuest() {
//        ProductPage pdp = openHTCProductFromCategory();
//        String expectedName = pdp.name();
//
//        WishlistPage wl = pdp.addToWishlist();
//
//        assertTrue(wl.isLoaded(), "Wishlist page should load");
//        assertTrue(wl.hasProduct(expectedName), "Wishlist should contain: " + expectedName);
//    }
//
//    /* =========== TC-025-B: User يضيف للمفضلة من صفحة المنتج =========== */
//
//    @Test(description = "TC-025-B (User): Add product to wishlist from Product Page (HTC)")
//    public void addToWishlist_FromProduct_AsUser() {
//        // Login أولاً
//        HomePage home = new HomePage(driver).open();
//        home.goToLogin().login(email, password);
//
//        // افتح المنتج ثم أضفه للمفضلة
//        CategoryPage cat = home.goToCategory("Electronics", "Cell phones");
//        ProductPage pdp = cat.openProductByName("HTC One M8 Android L 5.0 Lollipop");
//        String expectedName = pdp.name();
//
//        WishlistPage wl = pdp.addToWishlist();
//
//        assertTrue(wl.isLoaded(), "Wishlist page should load (user)");
//        assertTrue(wl.hasProduct(expectedName), "Wishlist should contain (user): " + expectedName);
//    }
//}
