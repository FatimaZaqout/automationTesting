package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    // Locators
    private final By emailField = By.id("Email");
    private final By passwordField = By.id("Password");
    private final By rememberMeCheckbox = By.id("RememberMe");
    private final By loginButton = By.cssSelector("button.button-1.login-button");

    // Error messages
    private final By errorSummary = By.cssSelector(".message-error.validation-summary-errors");
    private final By emailError = By.id("Email-error");

    // Success indicators
    private final By myAccountLink = By.cssSelector("a.ico-account");
    private final By logoutLink = By.cssSelector("a.ico-logout");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigate to login page
     */
    public LoginPage navigateToLogin() {
        driver.get("https://demo.nopcommerce.com/login");
        return this;
    }

    /**
     * Enter email
     */
    public LoginPage enterEmail(String email) {
        waits.type(emailField, email);
        return this;
    }

    /**
     * Enter password
     */
    public LoginPage enterPassword(String password) {
        waits.type(passwordField, password);
        return this;
    }

    /**
     * Check remember me checkbox
     */
    public LoginPage checkRememberMe() {
        if (!waits.visible(rememberMeCheckbox).isSelected()) {
            waits.click(rememberMeCheckbox);
        }
        return this;
    }

    /**
     * Click login button
     */
    public LoginPage clickLoginButton() {
        waits.click(loginButton);
        return this;
    }

    /**
     * Complete login operation
     */
    public LoginPage login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
        return this;
    }

    /**
     * Complete login with remember me
     */
    public LoginPage loginWithRememberMe(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        checkRememberMe();
        clickLoginButton();
        return this;
    }

    /**
     * Check if login is successful
     */
    public boolean isLoginSuccessful() {
        try {
            return waits.visible(myAccountLink).isDisplayed() &&
                    waits.visible(logoutLink).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if error summary is displayed
     */
    public boolean isErrorSummaryDisplayed() {
        try {
            return waits.visible(errorSummary).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get error summary text
     */
    public String getErrorSummary() {
        try {
            return waits.visible(errorSummary).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get email error message
     */
    public String getEmailError() {
        try {
            return waits.visible(emailError).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Check if My Account link is visible
     */
    public boolean isMyAccountVisible() {
        try {
            return waits.visible(myAccountLink).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if logout link is visible
     */
    public boolean isLogoutLinkVisible() {
        try {
            return waits.visible(logoutLink).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Logout from application
     */
    public LoginPage logout() {
        try {
            waits.click(logoutLink);
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("Logout failed or user not logged in");
        }
        return this;
    }
}