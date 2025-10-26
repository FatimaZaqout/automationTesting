package pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class RegistrationPage extends BasePage {

    // Locators
    private final By maleGenderRadio = By.id("gender-male");
    private final By femaleGenderRadio = By.id("gender-female");
    private final By firstNameField = By.id("FirstName");
    private final By lastNameField = By.id("LastName");
    private final By emailField = By.id("Email");
    private final By companyField = By.id("Company");
    private final By newsletterCheckbox = By.id("Newsletter");
    private final By passwordField = By.id("Password");
    private final By confirmPasswordField = By.id("ConfirmPassword");
    private final By registerButton = By.id("register-button");
    private final By successMessage = By.cssSelector(".result");
    private final By errorSummary = By.cssSelector(".message-error");
    private final By firstNameError = By.id("FirstName-error");
    private final By lastNameError = By.id("LastName-error");
    private final By emailError = By.id("Email-error");
    private final By passwordError = By.id("Password-error");
    private final By confirmPasswordError = By.id("ConfirmPassword-error");
    private final By continueButton = By.cssSelector(".button-1.register-continue-button");
    private final By logoutLink = By.linkText("Log out");

    public RegistrationPage(WebDriver driver) {
        super(driver);
    }

    // ============= Basic Actions =============

    public RegistrationPage selectMaleGender() {
        waits.click(maleGenderRadio);
        return this;
    }

    public RegistrationPage selectFemaleGender() {
        waits.click(femaleGenderRadio);
        return this;
    }

    public RegistrationPage enterFirstName(String firstName) {
        waits.type(firstNameField, firstName);
        return this;
    }

    public RegistrationPage enterLastName(String lastName) {
        waits.type(lastNameField, lastName);
        return this;
    }

    public RegistrationPage enterEmail(String email) {
        waits.type(emailField, email);
        return this;
    }

    public RegistrationPage enterCompany(String company) {
        waits.type(companyField, company);
        return this;
    }

    public RegistrationPage checkNewsletter() {
        waits.click(newsletterCheckbox);
        return this;
    }

    public RegistrationPage enterPassword(String password) {
        waits.type(passwordField, password);
        return this;
    }

    public RegistrationPage enterConfirmPassword(String confirmPassword) {
        waits.type(confirmPasswordField, confirmPassword);
        return this;
    }

    public RegistrationPage clickRegisterButton() {
        waits.click(registerButton);
        return this;
    }

    public RegistrationPage clickContinueButton() {
        waits.click(continueButton);
        return this;
    }

    // ============= Alert & Logout Handling =============

    /**
     * Handle browser password save alert
     * Uses short timeout (800ms) to avoid blocking
     */
    public RegistrationPage handlePasswordSaveAlert() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofMillis(800));
            Alert alert = shortWait.until(org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent());
            alert.dismiss();
            Thread.sleep(300);
            System.out.println("✓ Password save alert dismissed");
        } catch (Exception e) {
            // No alert - this is normal, browser didn't show save password prompt
        }
        return this;
    }

    /**
     * Logout from the application
     */
    public RegistrationPage logout() {
        try {
            handlePasswordSaveAlert(); // Check for alert before logout
            waits.click(logoutLink);
            System.out.println("✓ Logged out successfully");
        } catch (Exception e) {
            System.out.println("⚠ Logout link not found or already logged out");
        }
        return this;
    }

    // ============= Validation Methods =============

    public boolean isRegistrationSuccessful() {
        try {
            String message = waits.visible(successMessage).getText();
            return message.contains("Your registration completed");
        } catch (Exception e) {
            return false;
        }
    }

    public String getSuccessMessage() {
        return waits.visible(successMessage).getText();
    }

    public boolean isErrorSummaryDisplayed() {
        try {
            return waits.visible(errorSummary).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorSummary() {
        return waits.visible(errorSummary).getText();
    }

    // ============= Error Message Getters =============

    public String getFirstNameError() {
        try {
            return waits.visible(firstNameError).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getLastNameError() {
        try {
            return waits.visible(lastNameError).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getEmailError() {
        try {
            return waits.visible(emailError).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getPasswordError() {
        try {
            return waits.visible(passwordError).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getConfirmPasswordError() {
        try {
            return waits.visible(confirmPasswordError).getText();
        } catch (Exception e) {
            return "";
        }
    }

}