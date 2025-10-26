package tests;

import framework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginTest extends BaseTest {

    // ============= Data Providers =============

    @DataProvider(name = "invalidCredentials")
    public Object[][] getInvalidCredentials() {
        return new Object[][] {
                {"invalid@email.com", "WrongPass@123", "Invalid email"},
                {"notregistered@test.com", "Test@123", "Not registered email"},
                {RegistrationTest.validEmail, "WrongPassword@456", "Wrong password"}
        };
    }

    // ============= Test Cases =============

    /**
     * TC-001: Verify Successful Login with Valid Credentials
     * Priority: HIGH
     */
    @Test(priority = 1, description = "TC-001: Verify login with valid credentials")
    public void testCase1_SuccessfulLogin() {
        LoginPage loginPage = new LoginPage(driver);

        // Check if valid credentials are available from RegistrationTest
        if (RegistrationTest.validEmail == null || RegistrationTest.validPassword == null) {
            System.out.println("⚠ TC-001 SKIPPED: No valid user credentials available");
            System.out.println("  Please run RegistrationTest first to create a user");
            throw new org.testng.SkipException("Valid user credentials not available");
        }

        // Navigate to login page
        loginPage.navigateToLogin();

        // Perform login with credentials from RegistrationTest
        loginPage.login(RegistrationTest.validEmail, RegistrationTest.validPassword);

        // Verify login success
        Assert.assertTrue(loginPage.isLoginSuccessful(),
                "Login should be successful with valid credentials");
        Assert.assertTrue(loginPage.isMyAccountVisible(),
                "My Account link should be visible after login");
        Assert.assertTrue(loginPage.isLogoutLinkVisible(),
                "Logout link should be visible after login");

        // Cleanup - logout
        loginPage.logout();

        System.out.println("✓ TC-001 PASSED: Login successful with " + RegistrationTest.validEmail);
    }

    /**
     * TC-002: Verify Login with Invalid Credentials
     * Priority: HIGH
     */
    @Test(priority = 2, dataProvider = "invalidCredentials",
            description = "TC-002: Verify login fails with invalid credentials")
    public void testCase2_LoginWithInvalidCredentials(String email, String password, String scenario) {
        LoginPage loginPage = new LoginPage(driver);

        // Navigate to login page
        loginPage.navigateToLogin();

        // Attempt login with invalid credentials
        loginPage.login(email, password);

        // Verify login failed
        Assert.assertFalse(loginPage.isLoginSuccessful(),
                "Login should fail with invalid credentials: " + scenario);
        Assert.assertTrue(loginPage.isErrorSummaryDisplayed(),
                "Error message should be displayed for: " + scenario);

        String errorMessage = loginPage.getErrorSummary();
        Assert.assertTrue(errorMessage.toLowerCase().contains("unsuccessful") ||
                        errorMessage.toLowerCase().contains("invalid") ||
                        errorMessage.toLowerCase().contains("wrong"),
                "Error message should indicate login failure");

        System.out.println("✓ TC-002 PASSED: Login failed correctly - " + scenario);
    }

    /**
     * TC-003: Verify Login with Empty Fields
     * Priority: MEDIUM
     */
    @Test(priority = 3, description = "TC-003: Verify login with empty fields")
    public void testCase3_LoginWithEmptyFields() {
        LoginPage loginPage = new LoginPage(driver);

        // Navigate to login page
        loginPage.navigateToLogin();

        // Click login without entering credentials
        loginPage.clickLoginButton();

        // Verify validation errors
        Assert.assertFalse(loginPage.isLoginSuccessful(),
                "Login should fail with empty fields");

        // Check for email error message
        String emailError = loginPage.getEmailError();
        Assert.assertFalse(emailError.isEmpty(),
                "Email error should be displayed when fields are empty");

        System.out.println("✓ TC-003 PASSED: Empty fields validation working");
    }

    /**
     * TC-004: Verify Remember Me Functionality
     * Priority: LOW
     */
    @Test(priority = 4, description = "TC-004: Verify remember me checkbox functionality")
    public void testCase4_RememberMeFunctionality() {
        LoginPage loginPage = new LoginPage(driver);

        // Check if valid credentials are available
        if (RegistrationTest.validEmail == null || RegistrationTest.validPassword == null) {
            System.out.println("⚠ TC-004 SKIPPED: No valid user credentials available");
            throw new org.testng.SkipException("Valid user credentials not available");
        }

        // Navigate to login page
        loginPage.navigateToLogin();

        // Login with remember me checked
        loginPage.loginWithRememberMe(RegistrationTest.validEmail, RegistrationTest.validPassword);

        // Verify login success
        Assert.assertTrue(loginPage.isLoginSuccessful(),
                "Login with remember me should be successful");
        Assert.assertTrue(loginPage.isMyAccountVisible(),
                "My Account link should be visible");
        Assert.assertTrue(loginPage.isLogoutLinkVisible(),
                "Logout link should be visible");

        // Logout
        loginPage.logout();

        System.out.println("✓ TC-004 PASSED: Remember me functionality working");
    }

    /**
     * TC-005: Verify Logout Functionality
     * Priority: HIGH
     */
    @Test(priority = 5, description = "TC-005: Verify logout functionality")
    public void testCase5_LogoutFunctionality() {
        LoginPage loginPage = new LoginPage(driver);

        // Check if valid credentials are available
        if (RegistrationTest.validEmail == null || RegistrationTest.validPassword == null) {
            System.out.println("⚠ TC-005 SKIPPED: No valid user credentials available");
            throw new org.testng.SkipException("Valid user credentials not available");
        }

        // Login first
        loginPage.navigateToLogin();
        loginPage.login(RegistrationTest.validEmail, RegistrationTest.validPassword);

        // Verify login success
        Assert.assertTrue(loginPage.isLoginSuccessful(),
                "User should be logged in before testing logout");

        // Perform logout
        loginPage.logout();

        // Verify logout success (My Account and Logout links should not be visible together)
        Assert.assertFalse(loginPage.isMyAccountVisible() && loginPage.isLogoutLinkVisible(),
                "User should be logged out successfully");

        System.out.println("✓ TC-005 PASSED: Logout functionality working");
    }
}