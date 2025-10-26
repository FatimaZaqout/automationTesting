package tests;

import framework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.RegistrationPage;

public class RegistrationTest extends BaseTest {

    // Shared credentials for LoginTest and CheckoutTest
    public static String validEmail;
    public static String validPassword = "Test@123";

    // Helper method to generate unique email
    private String generateUniqueEmail() {
        return "testuser" + System.currentTimeMillis() + "@example.com";
    }

    // ============= Data Providers =============

    @DataProvider(name = "invalidEmails")
    public Object[][] getInvalidEmails() {
        return new Object[][] {
                {"test@"}
//                {"@test.com"},
//                {"test..test@test.com"},
//                {"test@test"}
        };
    }

    @DataProvider(name = "weakPasswords")
    public Object[][] getWeakPasswords() {
        return new Object[][] {
                {"123456"}
//                {"password"}
        };
    }

    // ============= Test Cases =============

    /**
     * TC-001: Verify User Registration with Valid Data
     * Priority: HIGH
     */
    @Test(priority = 1, description = "TC-001: Verify successful registration with valid data")
    public void testSuccessfulRegistration() {
        RegistrationPage registrationPage = new RegistrationPage(driver);

        String uniqueEmail = generateUniqueEmail();

        // Save credentials for other tests
        validEmail = uniqueEmail;
        validPassword = "Test@123";

        // Navigate directly to register page
        driver.get("https://demo.nopcommerce.com/register");

        // Fill registration form
        registrationPage
                .selectMaleGender()
                .enterFirstName("John")
                .enterLastName("Doe")
                .enterEmail(uniqueEmail)
                .enterPassword("Test@123")
                .enterConfirmPassword("Test@123")
                .clickRegisterButton();

        // Handle alert immediately
        registrationPage.handlePasswordSaveAlert();

        // Verify success
        Assert.assertTrue(registrationPage.isRegistrationSuccessful(),
                "Registration should be successful");
        Assert.assertTrue(registrationPage.getSuccessMessage()
                        .contains("Your registration completed"),
                "Success message should be displayed");

        // Complete registration (continue + logout)
        registrationPage.clickContinueButton();
        registrationPage.logout();

        System.out.println("✓ TC-001 PASSED: Registration successful for " + uniqueEmail);
        System.out.println("✓ Credentials saved for other tests: " + validEmail);
    }

    /**
     * TC-002: Verify Mandatory Fields in Registration
     */
    @Test(priority = 2, description = "TC-002: Verify registration fails with empty required fields")
    public void testRegistrationWithEmptyFields() {
        RegistrationPage registrationPage = new RegistrationPage(driver);

        // Navigate to register page
        driver.get("https://demo.nopcommerce.com/register");

        // Click register without filling any fields
        registrationPage.clickRegisterButton();

        // Verify all required field errors
        Assert.assertFalse(registrationPage.getFirstNameError().isEmpty(),
                "First name error should be displayed");
        Assert.assertFalse(registrationPage.getLastNameError().isEmpty(),
                "Last name error should be displayed");
        Assert.assertFalse(registrationPage.getEmailError().isEmpty(),
                "Email error should be displayed");
        Assert.assertFalse(registrationPage.getPasswordError().isEmpty(),
                "Password error should be displayed");
        Assert.assertFalse(registrationPage.getConfirmPasswordError().isEmpty(),
                "Confirm password error should be displayed");

        System.out.println("✓ TC-002 PASSED: Empty fields validation working");
    }

    /**
     * TC-003: Verify Registration with Invalid Email Format
     */
    @Test(priority = 3, dataProvider = "invalidEmails",
            description = "TC-003: Verify registration fails with invalid email")
    public void testRegistrationWithInvalidEmail(String invalidEmail) {
        RegistrationPage registrationPage = new RegistrationPage(driver);

        // Navigate to register page
        driver.get("https://demo.nopcommerce.com/register");

        registrationPage
                .selectMaleGender()
                .enterFirstName("John")
                .enterLastName("Doe")
                .enterEmail(invalidEmail)
                .enterPassword("Test@123")
                .enterConfirmPassword("Test@123")
                .clickRegisterButton();

        Assert.assertTrue(registrationPage.isErrorSummaryDisplayed() ||
                        !registrationPage.getEmailError().isEmpty(),
                "Email validation error should be displayed for: " + invalidEmail);

        System.out.println("✓ TC-003 PASSED: Invalid email rejected: " + invalidEmail);
    }

    /**
     * TC-004: Verify password strength validation
     */
    @Test(priority = 4, dataProvider = "weakPasswords",
            description = "TC-004: Verify registration fails with weak password")
    public void testRegistrationWithWeakPassword(String weakPassword) {
        RegistrationPage registrationPage = new RegistrationPage(driver);

        String uniqueEmail = generateUniqueEmail();

        // Navigate to register page
        driver.get("https://demo.nopcommerce.com/register");

        registrationPage
                .selectMaleGender()
                .enterFirstName("John")
                .enterLastName("Doe")
                .enterEmail(uniqueEmail)
                .enterPassword(weakPassword)
                .enterConfirmPassword(weakPassword)
                .clickRegisterButton();

        Assert.assertFalse(registrationPage.getPasswordError().isEmpty(),
                "Password validation error should be displayed for: " + weakPassword);

        System.out.println("✓ TC-004 PASSED: Weak password rejected");
    }

    /**
     * TC-005: Verify password confirmation matching
     */
    @Test(priority = 5, description = "TC-005: Verify registration fails with mismatched passwords")
    public void testRegistrationWithMismatchedPasswords() {
        RegistrationPage registrationPage = new RegistrationPage(driver);

        String uniqueEmail = generateUniqueEmail();

        // Navigate to register page
        driver.get("https://demo.nopcommerce.com/register");

        registrationPage
                .selectMaleGender()
                .enterFirstName("John")
                .enterLastName("Doe")
                .enterEmail(uniqueEmail)
                .enterPassword("Test@123")
                .enterConfirmPassword("Test@456")
                .clickRegisterButton();

        Assert.assertFalse(registrationPage.getConfirmPasswordError().isEmpty(),
                "Confirm password error should be displayed");
        Assert.assertTrue(registrationPage.getConfirmPasswordError()
                        .toLowerCase().contains("password") ||
                        registrationPage.getConfirmPasswordError()
                                .toLowerCase().contains("match"),
                "Error message should indicate password mismatch");

        System.out.println("✓ TC-005 PASSED: Password mismatch detected");
    }

    /**
     * TC-006: Verify Registration with Existing Email
     */
    @Test(priority = 6, description = "TC-006: Verify registration fails with already registered email")
    public void testRegistrationWithExistingEmail() {
        RegistrationPage registrationPage = new RegistrationPage(driver);

        // First registration
        String email = generateUniqueEmail();

        driver.get("https://demo.nopcommerce.com/register");

        registrationPage
                .selectMaleGender()
                .enterFirstName("John")
                .enterLastName("Doe")
                .enterEmail(email)
                .enterPassword("Test@123")
                .enterConfirmPassword("Test@123")
                .clickRegisterButton();

        // Handle alert
        registrationPage.handlePasswordSaveAlert();

        Assert.assertTrue(registrationPage.isRegistrationSuccessful(),
                "First registration should be successful");

        // Complete registration and logout
        registrationPage.clickContinueButton();
        registrationPage.logout();

        // Try to register again with same email
        driver.get("https://demo.nopcommerce.com/register");

        registrationPage
                .selectMaleGender()
                .enterFirstName("Jane")
                .enterLastName("Smith")
                .enterEmail(email)  // Same email
                .enterPassword("Test@123")
                .enterConfirmPassword("Test@123")
                .clickRegisterButton();

        // Verify duplicate email error
        Assert.assertTrue(registrationPage.isErrorSummaryDisplayed(),
                "Error should be displayed for duplicate email");

        String errorMessage = registrationPage.getErrorSummary();
        Assert.assertTrue(errorMessage.toLowerCase().contains("already") ||
                        errorMessage.toLowerCase().contains("exist"),
                "Error message should indicate email already exists");

        System.out.println("✓ TC-006 PASSED: Duplicate email rejected");
    }
}