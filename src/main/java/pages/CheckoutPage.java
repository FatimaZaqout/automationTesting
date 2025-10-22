package pages;

import org.openqa.selenium.*;

public class CheckoutPage extends BasePage {
    // step headers
    private final By checkoutTitle = By.cssSelector("div.page-title h1");

    // sign-in step
    private final By checkoutAsGuestBtn = By.cssSelector("button.checkout-as-guest-button");

    // billing
    private final By firstName = By.id("BillingNewAddress_FirstName");
    private final By lastName = By.id("BillingNewAddress_LastName");
    private final By email = By.id("BillingNewAddress_Email");
    private final By country = By.id("BillingNewAddress_CountryId");
    private final By city = By.id("BillingNewAddress_City");
    private final By addr1 = By.id("BillingNewAddress_Address1");
    private final By zip = By.id("BillingNewAddress_ZipPostalCode");
    private final By phone = By.id("BillingNewAddress_PhoneNumber");
    private final By billingContinue = By.cssSelector("button.new-address-next-step-button, button[name='save']");

    // shipping method
    private final By shippingMethodContinue = By.cssSelector("button.shipping-method-next-step-button");

    // payment method
    private final By paymentCheckMoney = By.id("paymentmethod_0"); // Check / Money Order (usually)
    private final By paymentMethodContinue = By.cssSelector("button.payment-method-next-step-button");

    // payment info
    private final By paymentInfoContinue = By.cssSelector("button.payment-info-next-step-button");

    // confirm
    private final By confirmOrderBtn = By.cssSelector("button.confirm-order-next-step-button");

    // success
    private final By successTitle = By.cssSelector("div.section.order-completed h1, div.order-completed .title strong");
    private final By orderNumber = By.cssSelector("div.order-completed .order-number");

    public CheckoutPage(WebDriver driver) { super(driver); }

    public boolean isCheckoutPage() {
        return waits.visible(checkoutTitle).getText().toLowerCase().contains("checkout");
    }

    public CheckoutPage checkoutAsGuest() {
        if (driver.findElements(checkoutAsGuestBtn).size() > 0) {
            waits.click(checkoutAsGuestBtn);
        }
        return this;
    }

    public CheckoutPage fillBilling(String f, String l, String em, String ctry, String cty, String address, String postal, String phoneNo) {
        waits.type(firstName, f);
        waits.type(lastName, l);
        waits.type(email, em);
        waits.selectByVisibleText(country, ctry);
        waits.type(city, cty);
        waits.type(addr1, address);
        waits.type(zip, postal);
        waits.type(phone, phoneNo);
        waits.click(billingContinue);
        return this;
    }

    public CheckoutPage selectShippingAnyAndContinue() {
        // default selection is often the first radio; just continue
        waits.click(shippingMethodContinue);
        return this;
    }

    public CheckoutPage selectPaymentCheckMoneyAndContinue() {
        if (driver.findElements(paymentCheckMoney).size() > 0) {
            waits.click(paymentCheckMoney);
        }
        waits.click(paymentMethodContinue);
        return this;
    }

    public CheckoutPage continuePaymentInfo() {
        waits.click(paymentInfoContinue);
        return this;
    }

    public CheckoutPage confirmOrder() {
        waits.click(confirmOrderBtn);
        return this;
    }

    public boolean isSuccess() {
        return driver.findElements(successTitle).size() > 0 &&
               driver.findElement(successTitle).getText().toLowerCase().contains("successfully processed");
    }

    public String getOrderNumber() {
        return driver.findElements(orderNumber).isEmpty() ? "" : driver.findElement(orderNumber).getText();
    }
}
