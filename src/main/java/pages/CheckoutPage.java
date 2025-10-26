package pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {

    // Terms of Service Modal
    private final By termsOfServiceCheckbox = By.id("termsofservice");
    private final By termsOfServiceModal = By.cssSelector(".ui-dialog");
    private final By closeModalButton = By.cssSelector(".ui-dialog-titlebar-close");

    // Checkout as guest
    private final By checkoutButton = By.id("checkout");
    private final By checkoutAsGuestButton = By.cssSelector("button.checkout-as-guest-button");

    // Billing Address
    private final By firstNameField = By.id("BillingNewAddress_FirstName");
    private final By lastNameField = By.id("BillingNewAddress_LastName");
    private final By emailField = By.id("BillingNewAddress_Email");
    private final By countryDropdown = By.id("BillingNewAddress_CountryId");
    private final By cityField = By.id("BillingNewAddress_City");
    private final By address1Field = By.id("BillingNewAddress_Address1");
    private final By zipCodeField = By.id("BillingNewAddress_ZipPostalCode");
    private final By phoneField = By.id("BillingNewAddress_PhoneNumber");

    // Continue buttons
    private final By continueButtonBilling = By.cssSelector("button.new-address-next-step-button");
    private final By continueButtonShipping = By.cssSelector("button.shipping-method-next-step-button");
    private final By continueButtonPayment = By.cssSelector("button.payment-method-next-step-button");
    private final By continueButtonPaymentInfo = By.cssSelector("button.payment-info-next-step-button");

    // Confirm button
    private final By confirmButton = By.cssSelector("button.confirm-order-next-step-button");

    // Order completion
    private final By orderSuccessMessage = By.cssSelector(".section.order-completed .title");
    private final By orderNumber = By.cssSelector(".order-number");

    // Error messages
    private final By firstNameError = By.id("BillingNewAddress_FirstName-error");
    private final By lastNameError = By.id("BillingNewAddress_LastName-error");
    private final By emailError = By.id("BillingNewAddress_Email-error");
    private final By cityError = By.id("BillingNewAddress_City-error");
    private final By address1Error = By.id("BillingNewAddress_Address1-error");
    private final By zipCodeError = By.id("BillingNewAddress_ZipPostalCode-error");
    private final By phoneError = By.id("BillingNewAddress_PhoneNumber-error");

    // Validation notification/alert
    private final By validationNotification = By.cssSelector(".bar-notification.error, .message-error");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Handle terms of service modal and checkbox
     * Steps: 1) Close modal (X), 2) Check terms checkbox, 3) Click checkout button
     */
    public CheckoutPage handleTermsAndCheckout() {
        try {
            // Step 1: Close the modal if it appears
            if (waits.visible(termsOfServiceModal) != null) {
                System.out.println("Terms modal detected, closing it...");
                waits.click(closeModalButton);
                Thread.sleep(500);
            }
        } catch (Exception e) {
            System.out.println("No modal to close or already closed");
        }

        try {
            // Step 2: Check the terms of service checkbox
            if (waits.visible(termsOfServiceCheckbox) != null) {
                if (!driver.findElement(termsOfServiceCheckbox).isSelected()) {
                    System.out.println("Checking terms of service checkbox...");
                    waits.click(termsOfServiceCheckbox);
                    Thread.sleep(500);
                }
            }
        } catch (Exception e) {
            System.out.println("Terms checkbox not found or already checked");
        }

        try {
            // Step 3: Click the checkout button
            System.out.println("Clicking checkout button...");
            waits.click(checkoutButton);
            Thread.sleep(1500);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * Click checkout as guest
     */
    public CheckoutPage checkoutAsGuest() {
        waits.click(checkoutAsGuestButton);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Fill billing address
     */
    public CheckoutPage fillBillingAddress(String firstName, String lastName,
                                           String email, String city,
                                           String address, String zipCode,
                                           String phone) {
        waits.type(firstNameField, firstName);
        waits.type(lastNameField, lastName);
        waits.type(emailField, email);

        // Select country - try different methods
        try {
            // First try: select by visible text
            waits.selectByVisibleText(countryDropdown, "United States");
        } catch (Exception e1) {
            try {
                // Second try: select by value (USA usually has value 1)
                waits.selectByValue(countryDropdown, "1");
            } catch (Exception e2) {
                try {
                    // Third try: select by index (usually index 1 or 2)
                    waits.selectByIndex(countryDropdown, 1);
                } catch (Exception e3) {
                    System.out.println("WARNING: Could not select country, using default");
                }
            }
        }

        waits.type(cityField, city);
        waits.type(address1Field, address);
        waits.type(zipCodeField, zipCode);
        waits.type(phoneField, phone);
        return this;
    }

    /**
     * Click continue button on billing address step
     */
    public CheckoutPage clickContinueBilling() {
        waits.click(continueButtonBilling);
        try {
            Thread.sleep(2000); // Wait for validation or page transition
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Click continue button on shipping method step
     */
    public CheckoutPage clickContinueShipping() {
        waits.click(continueButtonShipping);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Click continue button on payment method step
     */
    public CheckoutPage clickContinuePayment() {
        waits.click(continueButtonPayment);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Click continue button on payment info step
     */
    public CheckoutPage clickContinuePaymentInfo() {
        waits.click(continueButtonPaymentInfo);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Click confirm button
     */
    public CheckoutPage clickConfirm() {
        waits.click(confirmButton);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Complete checkout process
     */
    public CheckoutPage completeCheckout(String firstName, String lastName,
                                         String email, String city,
                                         String address, String zipCode,
                                         String phone) {
        fillBillingAddress(firstName, lastName, email, city, address, zipCode, phone);
        clickContinueBilling();
        clickContinueShipping();
        clickContinuePayment();
        clickContinuePaymentInfo();
        clickConfirm();
        return this;
    }

    /**
     * Check if order is completed successfully
     */
    public boolean isOrderCompleted() {
        try {
            return waits.visible(orderSuccessMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get order success message
     */
    public String getOrderSuccessMessage() {
        try {
            return waits.visible(orderSuccessMessage).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get order number
     */
    public String getOrderNumber() {
        try {
            return waits.visible(orderNumber).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get first name error
     */
    public String getFirstNameError() {
        try {
            return waits.visible(firstNameError).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get last name error
     */
    public String getLastNameError() {
        try {
            return waits.visible(lastNameError).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get email error
     */
    public String getEmailError() {
        try {
            return waits.visible(emailError).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get city error
     */
    public String getCityError() {
        try {
            return waits.visible(cityError).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get address error
     */
    public String getAddress1Error() {
        try {
            return waits.visible(address1Error).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get zip code error
     */
    public String getZipCodeError() {
        try {
            return waits.visible(zipCodeError).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get phone error
     */
    public String getPhoneError() {
        try {
            return waits.visible(phoneError).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Check if any mandatory field error exists
     */
    public boolean hasMandatoryFieldErrors() {
        // Wait a bit for errors to appear
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check for JavaScript alert first
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            System.out.println("  Alert detected: " + alertText);
            alert.accept(); // Click OK to close the alert
            return true;
        } catch (Exception e) {
            // No alert present, continue checking other validations
        }

        // Check for validation notification
        try {
            if (waits.visible(validationNotification) != null) {
                return true;
            }
        } catch (Exception e) {
            // Continue to check individual errors
        }

        // Check individual field errors
        return !getFirstNameError().isEmpty() ||
                !getLastNameError().isEmpty() ||
                !getEmailError().isEmpty() ||
                !getCityError().isEmpty() ||
                !getAddress1Error().isEmpty() ||
                !getZipCodeError().isEmpty() ||
                !getPhoneError().isEmpty();
    }

    /**
     * Get validation notification message
     */
    public String getValidationNotification() {
        try {
            return waits.visible(validationNotification).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get validation alert message
     */
    public String getValidationAlert() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            alert.accept(); // Close the alert
            return alertText;
        } catch (Exception e) {
            return "";
        }
    }
}