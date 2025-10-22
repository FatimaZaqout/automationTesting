package framework;

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

    public void selectByVisibleText(By locator, String text) {
        new Select(visible(locator)).selectByVisibleText(text);
    }
}
