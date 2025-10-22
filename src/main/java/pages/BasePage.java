package pages;

import framework.Waits;
import org.openqa.selenium.WebDriver;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final Waits waits;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.waits = new Waits(driver);
    }
}
