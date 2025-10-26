package tests;

import framework.BaseTest;
import org.testng.annotations.Test;
import pages.*;
import static org.testng.Assert.*;
 import pages.HomePage;
import pages.LoginPage;
import pages.ProductPage;
import pages.WishlistPage;

public class WishlistTests extends BaseTest {

    private final String email = "Fatima@gmail.com";
    private final String password = "YourPass123";

    @Test(description = "TC-026 (Guest): Add third featured product to wishlist from Home Page", groups = {"smoke"})
    public void addWishlistFromHome_AsGuest() {
        HomePage home = new HomePage(driver).open();
        WishlistPage wl = home.addThirdFeaturedProductToWishlist();

        assertTrue(wl.isLoaded(), "Wishlist page should load");
        assertTrue(wl.hasProduct("HTC"), "Product should be listed in wishlist");
    }

    @Test(description = "TC-025 (User): Add third featured product to wishlist from Home Page", groups = {"smoke"})
    public void addWishlistFromHome_AsUser() {
        HomePage home = new HomePage(driver).open();
        LoginPage login = home.goToLogin();
        login.login(email, password);

        WishlistPage wl = home.addThirdFeaturedProductToWishlist();
        assertTrue(wl.isLoaded());
        assertTrue(wl.hasProduct("HTC"));
    }
    

    @Test(description = "TC-027: Verify user can remove product from wishlist", groups = {"smoke"})
    public void removeFromWishlist() {
        HomePage home = new HomePage(driver).open();
        home.goToLogin().login(email, password);

        WishlistPage wl = home.addThirdFeaturedProductToWishlist();
        assertTrue(wl.hasProduct("HTC"));

        wl.removeProductByRowButton("HTC");    
        
    }
}
