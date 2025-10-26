package framework;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class Waits {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public Waits(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(12));
        this.wait.ignoring(StaleElementReferenceException.class);
    }

    public WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement clickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean invisible(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void click(By locator) {
        clickable(locator).click();
    }

    public void type(By locator, String text) {
        WebElement el = visible(locator);
        el.clear();
        el.sendKeys(text);
    }
    /**
     * Select dropdown option by index
     */
    public void selectByIndex(By locator, int index) {
        Select select = new Select(visible(locator));
        select.selectByIndex(index);
    }
    public void selectByVisibleText(By locator, String text) {
        new Select(visible(locator)).selectByVisibleText(text);
    }
    public void selectByValue(By locator, String value) {
        Select select = new Select(visible(locator));
        select.selectByValue(value);
    }

    public Alert alert() {
        return wait.until(ExpectedConditions.alertIsPresent());
    }

    /**
     * Accept any alert that might be present
     */
    public void acceptAnyAlert() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofMillis(500));
            Alert alert = shortWait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
            Thread.sleep(200);
        } catch (Exception e) {
            // No alert present
        }
    }
    /**
     * Quick check and dismiss any alert (500ms timeout)
     * Use this when you suspect an alert might appear
     */
    public void dismissAnyAlert() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofMillis(500));
            Alert alert = shortWait.until(ExpectedConditions.alertIsPresent());
            alert.dismiss();
            Thread.sleep(200);
        } catch (Exception e) {
            // No alert present - this is normal and expected
        }
    }
    public void scrollIntoView(WebElement element) {
        if (element == null) return;
        try {
            ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", element);
        } catch (JavascriptException e) {
            try {
                new org.openqa.selenium.interactions.Actions(driver)
                    .moveToElement(element)
                    .perform();
            } catch (Exception ignore) {  
        }
        try { Thread.sleep(150); } catch (InterruptedException ignored) {}
    }
    }
}
